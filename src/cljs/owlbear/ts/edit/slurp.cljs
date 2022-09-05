(ns owlbear.ts.edit.slurp
  (:require [clojure.string :as str]
            [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.edit.clean :as ts-clean]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :as obu]))

(defn node->forward-slurp-subjects
  "Given a `node`, 
   returns all nodes within the node, including the given node itself, 
   that are forward-slurp subjects"
  [node]
  (filter (comp #(some ts-rules/object-node %)
                obpr/node->forward-sibling-nodes
                ts-rules/subject-container-node)
          (obpr/node->descendants node)))

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
  [current-node forward-node]
  (letfn [(object-type-needs-semicolon? []
            (and (ts-rules/ts-object-type-node current-node)
                 (not= ";" (some-> current-node
                                   ts-rules/end-nodes
                                   first
                                   (obu/noget+ :?type)))))]
    (cond
      ;; type foo = {a: string;▌} b => type foo = {a: string; b:;▌} 
      (and (ts-rules/complete-ts-object-node current-node)
           (object-type-needs-semicolon?))
      ":;"
      ;; const foo = {} | const foo = {a: 1} | type foo = {a: string;}
      (or (ts-rules/empty-ts-object-node current-node)
          (ts-rules/ts-object-ends-with-pair current-node)
          (ts-rules/complete-ts-object-node current-node))
      ":"
      ;; type foo = {▌} a => type foo = {a:;▌} 
      (ts-rules/empty-ts-object-type-node current-node)
      ":;"
      ;; type foo = {a: ▌} b => type = {a: b;▌}
      (object-type-needs-semicolon?)
      ";"

      ;; {▌} // hello, world => {▌//hello, wordld\n}
      (ts-rules/ts-comment-node forward-node)
      "\n")))

(defn escape-string
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with strings escaped in the src 
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [edit-history] :or {edit-history []} :as ctx} current-node forward-node]
  (if-let [insert-offsets (and (contains? #{ts-rules/ts-string ts-rules/ts-template-string}
                                          (obu/noget+ current-node :?type))
                               (let [forward-node-start-offset (obu/noget+ forward-node :?startIndex)
                                     quote-char (obu/noget+ current-node :?firstChild.?text)]
                                 (some-> (->> (obu/noget+ forward-node :?text)
                                              (obu/re-pos (->> quote-char
                                                               (str "(?<!\\\\)")
                                                               re-pattern))
                                              not-empty
                                              (map :offset))
                                         (cond->>
                                          (= quote-char "`") (remove (into #{}
                                                                           (mapcat (juxt #(- (obu/noget+ % :?startIndex)
                                                                                             forward-node-start-offset)
                                                                                         #(- (dec (obu/noget+ % :?endIndex))
                                                                                             forward-node-start-offset)))
                                                                           (ts-rules/node->template-string-nodes-in-substitutions forward-node))))
                                         (->> (obu/inc-offsets (obu/update-offset forward-node-start-offset edit-history))))))]
    (ts-clean/escape-offsets ctx insert-offsets)
    ctx))

(defn escape-comment-block
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with comment blocks escaped 
   in the src -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [edit-history] :or {edit-history []} :as ctx} current-node forward-node]
  (if-let [insert-offsets (and (ts-rules/ts-comment-block-node current-node)
                               (some->> (obu/noget+ forward-node :?text)
                                        (obu/re-pos #"(?<!\\)(/)(?=\*)|(?<=\*)(?<!\\)(/)")
                                        not-empty
                                        (map :offset)
                                        (obu/inc-offsets (-> forward-node
                                                             (obu/noget+ :?startIndex)
                                                             (obu/update-offset edit-history)))))]
    (ts-clean/escape-offsets ctx insert-offsets)
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
  (let [insert-brackets? (and (or (ts-rules/ts-object-ends-with-pair current-node)
                                  (ts-rules/empty-ts-object-node current-node))
                              (and (not (ts-rules/expression-statement-of #{ts-rules/ts-string} forward-node))
                                   (not (ts-rules/ts-string-node forward-node))))
        [opening-insert-offset
         closing-insert-offset] (when insert-brackets?
                                  (let [opening-insert-offset (obu/update-offset
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
  (let [rm-semicolon? (and (not (ts-rules/ts-statement-block-node current-node))
                           (not (ts-rules/ts-comment-block-node current-node))
                           (= ";" (obu/noget+ forward-node :?lastChild.?type)))
        insert-offset (when rm-semicolon?
                        (obu/update-offset
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
  (let [separator (cond (and (or (ts-rules/not-empty-ts-array-node current-node)
                                 (ts-rules/ts-object-ends-with-pair current-node))
                             ;; e.g. [[], 1]
                             (not= "," (obu/noget+ current-node :?nextSibling.?type)))
                        ","
                        (and (ts-rules/not-empty-ts-arguments-node current-node)
                             ;; e.g. [foo(a), 1]
                             (-> current-node
                                 ts-rules/subject-container-node
                                 (obu/noget+ :?nextSibling.?type)
                                 (not= ",")))
                        ","
                        (ts-rules/complete-ts-object-node current-node)
                        ";"
                        :else nil)
        insert-offset (when separator
                        (obu/update-offset
                         (obu/noget+ current-node :?lastChild.?startIndex)
                         edit-history))]
    (cond-> ctx
      separator (assoc :src (obu/str-insert src separator insert-offset)
                       :edit-history (conj edit-history {:type :insert
                                                         :offset insert-offset
                                                         :text separator
                                                         :src src}))
      (and separator (>= offset insert-offset)) (update :offset inc))))

(defn remove-item-separators
  "Given a context, `ctx`, a current node, `current-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with [item] separators i.e. 
   commas or semicolons removed from the src -if applicable, 
   else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [offset edit-history] :or {edit-history []} :as ctx} current-node forward-node]
  (let [rm-separators? (and (or (ts-rules/empty-ts-arguments-node current-node)
                                (ts-rules/empty-ts-collection-node current-node)
                                (ts-rules/ts-statement-block-node current-node)
                                ;; e.g. [{a: }, 1]
                                (and (ts-rules/ts-object-node current-node)
                                     (not (ts-rules/ts-object-ends-with-pair current-node))))
                            (ts-rules/ts-syntax-node (obu/noget+ forward-node :?previousSibling)))
        separators (when rm-separators?
                     (take-while ts-rules/ts-syntax-node
                                 (obpr/node->backward-sibling-nodes forward-node)))]
    (if rm-separators?
      (reduce
       (fn [c separator]
         (let [rm-offset (obu/update-offset (obu/noget+ separator :?startIndex)
                                            edit-history)
               sep-text (obu/noget+ separator :?text)
               sep-text-len (count sep-text)]
           (-> c
               (update :src obu/str-remove rm-offset (+ rm-offset sep-text-len))
               (update :edit-history conj {:type :delete
                                           :text sep-text
                                           :offset rm-offset})
               (cond-> (>= offset rm-offset) (update :offset - sep-text-len)))))
       ctx
       separators)
      ctx)))

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
  (let [current-end-nodes (ts-rules/end-nodes current-node)
        end-node-prefix (end-node-prefix current-node forward-node)
        current-end-node-text (->> current-end-nodes
                                   (map #(obu/noget+ % :?text))
                                   str/join
                                   (str end-node-prefix))
        forward-object-node-end-index (obu/update-offset
                                       (obu/noget+ forward-node :?endIndex)
                                       edit-history)
        current-end-node-text-len (count current-end-node-text)
        end-node-prefix-len (count end-node-prefix)
        current-end-node-start-index (-> current-end-nodes
                                         first
                                         (obu/noget+ :?startIndex)
                                         (obu/update-offset edit-history))
        current-end-node-end-index (-> current-end-nodes
                                       last
                                       (obu/noget+ :?endIndex)
                                       (obu/update-offset edit-history))
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
   
   Accepts an optional third `tree-id` argument which 
   specifies the ID of an existing Tree-sitter tree that 
   should be used

   Accepts an optional fourth `tsx?` argument which specifies 
   if the `src` should be parsed as TSX

   e.g.
   ```typescript
   for (let i = 0; i < cars.length; i++) {📍} text += cars[i] + \"<br>\";
   =>
   for (let i = 0; i < cars.length; i++) {📍 text += cars[i] + \"<br>\"};
   ```"
  ([src offset]
   (forward-slurp src offset nil))
  ([src offset tree-id]
   (forward-slurp src offset tree-id false))
  ([src offset tree-id tsx?]
   {:pre [(string? src) (<= 0 offset)]}
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src (if tsx? obp/tsx-lang-id obp/ts-lang-id)))]
     (when-let [{:keys [forward-object-node
                        current-node]} (ts-rules/node->current-forward-object-ctx
                                        (obu/noget+ tree :?rootNode)
                                        offset)]
       (some-> {:src src
                :offset offset}
               (move-end-nodes current-node forward-object-node)
               (remove-item-separators current-node forward-object-node)
               (insert-item-separator current-node)
               (escape-string current-node forward-object-node)
               (escape-comment-block current-node forward-object-node)
               (insert-computed-property-brackets current-node forward-object-node)
               (remove-statement-semicolon current-node forward-object-node)
               (select-keys [:src :offset]))))))

(comment
  ;; Examples
  (forward-slurp "const a = 1 + 1; <><div>hello</div><h1>world</h1></>" 26 obp/tsx-lang-id)
  (let [src "[]\n1"
        offset 1
        {:keys [current-node
                forward-object-node]} (ts-rules/node->current-forward-object-ctx
                                       (obu/noget+ (obp/src->tree! src obp/ts-lang-id) :?rootNode)
                                       offset)
        ctx {:src src :offset offset}]
    (move-end-nodes ctx current-node forward-object-node))
  (let [src "[[], 1]"
        offset 1
        {:keys [current-node
                forward-object-node]} (ts-rules/node->current-forward-object-ctx
                                       (obu/noget+ (obp/src->tree! src obp/ts-lang-id) :?rootNode)
                                       offset)
        ctx {:src src :offset offset}]
    (-> ctx
        (move-end-nodes current-node forward-object-node)
        (remove-item-separators current-node forward-object-node)))
  (let [src "[1]\n2"
        offset 1
        {:keys [current-node
                forward-object-node]} (ts-rules/node->current-forward-object-ctx
                                       (obu/noget+ (obp/src->tree! src obp/ts-lang-id) :?rootNode)
                                       offset)
        ctx {:src src :offset offset}]
    (-> ctx
        (move-end-nodes current-node forward-object-node)
        (insert-item-separator current-node)))
  (let [src "const foo = {};\na"
        offset 13
        {:keys [current-node
                forward-object-node]} (ts-rules/node->current-forward-object-ctx
                                       (obu/noget+ (obp/src->tree! src obp/ts-lang-id) :?rootNode)
                                       offset)
        ctx {:src src :offset offset}]
    (-> ctx
        (move-end-nodes current-node forward-object-node)
        (insert-computed-property-brackets current-node forward-object-node)))
  (let [src "\"\"\n\"\""
        offset 1
        {:keys [current-node
                forward-object-node]} (ts-rules/node->current-forward-object-ctx
                                       (obu/noget+ (obp/src->tree! src obp/ts-lang-id) :?rootNode)
                                       offset)
        ctx {:src src :offset offset}]
    (-> ctx
        (move-end-nodes current-node forward-object-node)
        (escape-string current-node forward-object-node)))
  (let [src "/**/\n/**/"
        offset 1
        {:keys [current-node
                forward-object-node]} (ts-rules/node->current-forward-object-ctx
                                       (obu/noget+ (obp/src->tree! src obp/ts-lang-id) :?rootNode)
                                       offset)
        ctx {:src src :offset offset}]
    (-> ctx
        (move-end-nodes current-node forward-object-node)
        (escape-comment-block current-node forward-object-node)))
  (let [src "[]\na;"
        offset 1
        {:keys [current-node
                forward-object-node]} (ts-rules/node->current-forward-object-ctx
                                       (obu/noget+ (obp/src->tree! src obp/ts-lang-id) :?rootNode)
                                       offset)
        ctx {:src src :offset offset}]
    (-> ctx
        (move-end-nodes current-node forward-object-node)
        (remove-statement-semicolon current-node forward-object-node)))
  (let [src "foo(() => {}, bar(), baz())"
        offset 11
        {:keys [current-node
                forward-object-node]} (ts-rules/node->current-forward-object-ctx
                                       (obu/noget+ (obp/src->tree! src obp/ts-lang-id) :?rootNode)
                                       offset)
        ctx {:src src :offset offset}]
    (-> ctx
        (move-end-nodes current-node forward-object-node)
        (remove-item-separators current-node forward-object-node))))