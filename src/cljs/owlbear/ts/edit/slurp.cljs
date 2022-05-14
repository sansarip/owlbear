(ns owlbear.ts.edit.slurp
  (:require [clojure.string :as str]
            [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.parse.rules :as ob-ts-rules]
            [owlbear.utilities :as obu]))

(defn node->forward-slurp-subjects
  "Given a `node`, 
   returns all nodes within the node, including the given node itself, 
   that are forward-slurp subjects"
  [node]
  (filter (comp #(some ob-ts-rules/object-node %)
                obpr/node->forward-sibling-nodes
                ob-ts-rules/subject-container-node)
          (obpr/flatten-children node)))

(defn end-node-prefix
  "Given a `node`, 
   returns a string that should go 
   just before the end node in a slurp

   This is different from the item separator!
   
   e.g.
   ```typescript
   interface foo {a: string;}
   b
   // The end-node prefix should be :
   // e.g. after a slurp      ⬇️
   interface foo {a: string; b:}
   ```"
  [node]
  (letfn [(object-type-needs-semicolon? []
            (and (ob-ts-rules/ts-object-type-node node)
                 (not= ";" (some-> node
                                   ob-ts-rules/end-nodes
                                   last
                                   (obu/noget+ :?type)))))]
    (cond
      ;; type foo = {a: string}
      (and (ob-ts-rules/ts-object-type-ends-with-pair node)
           (object-type-needs-semicolon?))
      ":;"
      ;; const foo = {} | const foo = {a: 1} | type foo = {a: string;}
      (or (ob-ts-rules/empty-ts-object-node node)
          (ob-ts-rules/ts-object-ends-with-pair node)
          (ob-ts-rules/ts-object-type-ends-with-pair node))
      ":"
      ;; type = {}
      (ob-ts-rules/empty-ts-object-type-node node)
      ":;"
      ;; type = {a:}
      (object-type-needs-semicolon?)
      ";")))

(defn update-offset
  "Given an initial offset, `initial-offset`, and a `history`, 
   returns the given offset with the arithmetic operations from 
   the given history applied to it"
  [initial-offset history]
  (if (empty? history)
    initial-offset
    (reduce
     (fn [offset {event-type :type event-offset :offset :keys [text]}]
       (if (>= offset event-offset)
         (cond (= :insert event-type) (+ offset (count text))
               (= :delete event-type) (max (- offset (count text)) event-offset)
               :else offset)
         offset))
     initial-offset
     history)))

(defn inc-offsets
  "Given an `addend` and a sequence of `offsets`, 
   returns lazy seq of updated offsets, 
   each updated offset being a sum of itself, 
   the given addend, and its index position"
  [addend offsets]
  (map-indexed
   (partial + addend)
   (sort offsets)))

(defn escape-offsets
  "Given a context map, `ctx`, and a seq of `offsets`, 
   returns the the context map with its src updated 
   with backslashes inserted at the given offsets
   
   Also updates the offset and edit-history of the 
   given context"
  [{og-offset :offset :as ctx} offsets]
  (reduce
   (fn [c insert-offset]
     (-> c
         (update :src obu/str-insert \\ insert-offset)
         (update :edit-history conj {:type :insert
                                     :text "\\"
                                     :offset insert-offset})
         (cond-> (>= og-offset insert-offset) (update :offset inc))))
   ctx
   offsets))

(defn escape-string
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with strings escaped in the src 
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [edit-history] :or {edit-history []} :as ctx} current-node forward-node]
  (if-let [insert-offsets (and (contains? #{ob-ts-rules/ts-string ob-ts-rules/ts-template-string}
                                          (obu/noget+ current-node :?type))
                               (some->> (obu/noget+ forward-node :?text)
                                        (obu/re-pos (->> (obu/noget+ current-node :?firstChild.?text) ; Quote char
                                                         (str "(?<!\\\\)")
                                                         re-pattern))
                                        not-empty
                                        (map :offset)
                                        (inc-offsets (-> forward-node
                                                         (obu/noget+ :?startIndex)
                                                         (update-offset edit-history)))))]
    (escape-offsets ctx insert-offsets)
    ctx))

(defn escape-comment-block
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with comment blocks escaped 
   in the src -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [edit-history] :or {edit-history []} :as ctx} current-node forward-node]
  (if-let [insert-offsets (and (ob-ts-rules/ts-comment-block-node current-node)
                               (some->> (obu/noget+ forward-node :?text)
                                        (obu/re-pos #"(?<!\\)(/)(?=\*)|(?<=\*)(?<!\\)(/)")
                                        not-empty
                                        (map :offset)
                                        (inc-offsets (-> forward-node
                                                         (obu/noget+ :?startIndex)
                                                         (update-offset edit-history)))))]
    (escape-offsets ctx insert-offsets)
    ctx))

(defn insert-computed-property-brackets
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with brackets inserted around 
   [what should be] computed properties in the src 
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [src offset edit-history] :or {edit-history []} :as ctx} current-node forward-node]
  (let [insert-brackets? (and (or (ob-ts-rules/ts-object-ends-with-pair current-node)
                                  (ob-ts-rules/empty-ts-object-node current-node))
                              (and (not (ob-ts-rules/expression-statement-of #{ob-ts-rules/ts-string} forward-node))
                                   (not (ob-ts-rules/ts-string-node forward-node))))
        [opening-insert-offset
         closing-insert-offset] (when insert-brackets?
                                  (let [opening-insert-offset (update-offset
                                                               (obu/noget+ forward-node :?startIndex)
                                                               edit-history)
                                        forward-node-text-len (count (obu/noget+ forward-node :?text))
                                        closing-insert-offset (inc (+ opening-insert-offset forward-node-text-len))]
                                    [opening-insert-offset closing-insert-offset]))]
    (cond-> ctx
      insert-brackets? (assoc :src (-> src
                                       (obu/str-insert "[" opening-insert-offset)
                                       (obu/str-insert "]" closing-insert-offset))
                              :edit-history (conj edit-history
                                                  {:type :insert
                                                   :offset opening-insert-offset
                                                   :text "["}
                                                  {:type :insert
                                                   :offset closing-insert-offset
                                                   :text "]"}))
      (and insert-brackets? (>= offset closing-insert-offset)) (update :offset + 2)
      (and insert-brackets? (> closing-insert-offset offset) (>= offset opening-insert-offset)) (update :offset inc))))

(defn remove-statement-semicolon
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with a semicolon removed from 
   a statement in the src -if applicable, 
   else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [src offset edit-history] :or {edit-history []} :as ctx} current-node forward-node]
  (let [rm-semicolon? (and (not (ob-ts-rules/ts-statement-block-node current-node))
                           (not (ob-ts-rules/ts-comment-block-node current-node))
                           (= ";" (obu/noget+ forward-node :?lastChild.?type)))
        insert-offset (when rm-semicolon?
                        (update-offset
                         (obu/noget+ forward-node :?lastChild.?startIndex)
                         edit-history))]
    (cond-> ctx
      rm-semicolon? (assoc :src (obu/str-remove src insert-offset)
                           :edit-history (conj edit-history {:type :delete
                                                             :offset insert-offset
                                                             :text ";"}))
      (and rm-semicolon? (>= offset insert-offset)) (update :offset dec))))

(defn insert-item-separator
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with an [item] separator i.e. 
   comma or semicolon inserted in the src -if applicable, 
   else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [src offset edit-history] :or {edit-history []} :as ctx} current-node]
  (let [separator (cond (and (or (ob-ts-rules/not-empty-ts-array-node current-node)
                                 (ob-ts-rules/ts-object-ends-with-pair current-node))
                             ;; e.g. [[], 1]
                             (not= "," (obu/noget+ current-node :?nextSibling.?type)))
                        ","
                        (ob-ts-rules/ts-object-type-ends-with-pair current-node)
                        ";"
                        :else nil)
        insert-offset (when separator
                        (update-offset
                         (obu/noget+ current-node :?lastChild.?startIndex)
                         edit-history))]
    (cond-> ctx
      separator (assoc :src (obu/str-insert src separator insert-offset)
                       :edit-history (conj edit-history {:type :insert
                                                         :offset insert-offset
                                                         :text separator
                                                         :src src}))
      (and separator (>= offset insert-offset)) (update :offset inc))))

(defn move-end-nodes
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with the end node(s) of the 
   given current node moved to the end of the given forward 
   node; these changes are reflected in the src of the `ctx`
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [src offset edit-history] :or {edit-history []} :as ctx} current-node forward-node]
  (let [current-end-nodes (reverse (ob-ts-rules/end-nodes current-node))
        end-node-prefix (end-node-prefix current-node)
        current-end-node-text (->> current-end-nodes
                                   (map #(obu/noget+ % :?text))
                                   str/join
                                   (str end-node-prefix))
        forward-object-node-end-index (update-offset
                                       (obu/noget+ forward-node :?endIndex)
                                       edit-history)
        current-end-node-text-len (count current-end-node-text)
        end-node-prefix-len (count end-node-prefix)
        current-end-node-start-index (-> current-end-nodes
                                         first
                                         (obu/noget+ :?startIndex)
                                         (update-offset edit-history))
        current-end-node-end-index (-> current-end-nodes
                                       last
                                       (obu/noget+ :?endIndex)
                                       (update-offset edit-history))
        end-node-insert-offset (- forward-object-node-end-index
                                  (- current-end-node-text-len
                                     end-node-prefix-len))]
    (cond-> (assoc ctx
                   :src (-> src
                            (obu/str-remove current-end-node-start-index current-end-node-end-index)
                            (obu/str-insert current-end-node-text end-node-insert-offset))
                   :edit-history (conj edit-history
                                       {:type :delete
                                        :offset current-end-node-start-index
                                        :text (cond-> current-end-node-text
                                                (< 0 end-node-prefix-len) (-> rest str/join)) ; Remove the prefix
                                        :src src}
                                       {:type :insert
                                        :offset end-node-insert-offset
                                        :text current-end-node-text
                                        :src (obu/str-remove src current-end-node-start-index current-end-node-end-index)}))
      (<= current-end-node-start-index
          offset
          (dec current-end-node-end-index)) (assoc :offset (+ (- offset current-end-node-start-index)
                                                              end-node-insert-offset)))))

(defn forward-slurp
  "Given a `src` string and character `offset`, 
   returns a map containing a new `src` string 
   with the forward slurp operation applied at the offset 
   and a new `offset` containing where the cursor position 
   should be after the slurp
   
   Accepts an optional third `tsx?` argument which specifies 
   if the `src` should be parsed as TSX

   e.g.
   ```typescript
   for (let i = 0; i < cars.length; i++) {📍} text += cars[i] + \"<br>\";
   =>
   for (let i = 0; i < cars.length; i++) {📍 text += cars[i] + \"<br>\"};
   ```"
  ([src offset]
   (forward-slurp src offset false))
  ([src offset tsx?]
   {:pre [(string? src) (<= 0 offset)]}
   (when-let [{:keys [forward-object-node
                      current-node]} (ob-ts-rules/node->current-forward-object-ctx
                                      (obu/noget+ (obp/src->tree src (if tsx? obp/tsx-lang-id obp/ts-lang-id)) :?rootNode)
                                      offset)]
     (-> {:src src
          :offset offset}
         (move-end-nodes current-node forward-object-node)
         (insert-item-separator current-node)
         (escape-string current-node forward-object-node)
         (escape-comment-block current-node forward-object-node)
         (insert-computed-property-brackets current-node forward-object-node)
         (remove-statement-semicolon current-node forward-object-node)
         (select-keys [:src :offset])))))

(comment
  ;; Examples
  (forward-slurp "const a = 1 + 1; <><div>hello</div><h1>world</h1></>" 26 obp/tsx-lang-id)
  (let [src "[]\n1"
        root-node (obu/noget+ (obp/src->tree src obp/tsx-lang-id) :?rootNode)
        current-node (obu/noget+ root-node :?children.?0.?children.?0)
        forward-node (obu/noget+ root-node :?children.?1)
        ctx {:src src :offset 1}]
    (move-end-nodes ctx current-node forward-node))
  (let [src "[1]\n2"
        root-node (obu/noget+ (obp/src->tree src obp/tsx-lang-id) :?rootNode)
        current-node (obu/noget+ root-node :?children.?0.?children.?0)
        forward-node (obu/noget+ root-node :?children.?1)
        ctx {:src src :offset 1}]
    (-> ctx
        (move-end-nodes current-node forward-node)
        (insert-item-separator current-node)))
  (let [src "const foo = {};\na"
        root-node (obu/noget+ (obp/src->tree src obp/tsx-lang-id) :?rootNode)
        current-node (obu/noget+ root-node :?children.?0.?children.?1.?children.?2)
        forward-node (obu/noget+ root-node :?children.?1)
        ctx {:src src :offset 14}]
    (-> ctx
        (move-end-nodes current-node forward-node)
        (insert-computed-property-brackets current-node forward-node)))
  (let [src "\"\"\n\"\""
        root-node (obu/noget+ (obp/src->tree src obp/tsx-lang-id) :?rootNode)
        current-node (obu/noget+ root-node :?children.?0.?children.?0)
        forward-node (obu/noget+ root-node :?children.?1)
        ctx {:src src :offset 1}]
    (-> ctx
        (move-end-nodes current-node forward-node)
        (escape-string current-node forward-node)))
  (let [src "/**/\n/**/"
        root-node (obu/noget+ (obp/src->tree src obp/tsx-lang-id) :?rootNode)
        current-node (obu/noget+ root-node :?children.?0)
        forward-node (obu/noget+ root-node :?children.?1)
        ctx {:src src :offset 1}]
    (-> ctx
        (move-end-nodes current-node forward-node)
        (escape-comment-block current-node forward-node)))
  (let [src "[]\na;"
        root-node (obu/noget+ (obp/src->tree src obp/tsx-lang-id) :?rootNode)
        current-node (obu/noget+ root-node :?children.?0.?children.?0)
        forward-node (obu/noget+ root-node :?children.?1)
        ctx {:src src :offset 1}]
    (-> ctx
        (move-end-nodes current-node forward-node)
        (remove-statement-semicolon current-node forward-node))))