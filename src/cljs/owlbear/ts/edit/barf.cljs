(ns owlbear.ts.edit.barf
  (:require [clojure.string :as str]
            [oops.core :refer [ocall]]
            [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.edit.clean :as ts-clean]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :as obu]))

(defn node->forward-barf-subjects
  "Given a `node`, 
   returns all nodes within the node, including the given node itself, 
   that are forward-barf subjects"
  [node]
  (filter (comp not-empty
                #(filter ts-rules/object-node %)
                #(when % (obu/noget+ % :?children))
                ts-rules/subject-node)
          (obpr/node->descendants node)))

(defn remove-superfluous-syntax
  "Given a context, `ctx`, a ancestor node, `ancestor-node`, 
   and a child node, `child-node`, 
   returns an updated context with superfluous syntax removed i.e. 
   commas or semicolons removed from empty collections/types
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [offset ancestor-node child-node src] :as ctx}]
  (let [rm-syntax? (and (or (ts-rules/ts-arguments-node ancestor-node)
                            (ts-rules/incomplete-ts-object-node ancestor-node)
                            (ts-rules/not-empty-ts-collection-node ancestor-node)
                            (ts-rules/ts-object-type-node ancestor-node))
                        ;; Compensates for the hackery of resolving pair nodes to their values
                        ;; A test will fail if this is removed.
                        (-> child-node
                            (obu/noget+ :?parent)
                            ts-rules/ts-pair-node
                            not)
                        (ts-rules/ts-syntax-node (obu/noget+ ancestor-node :?lastChild.?previousSibling)))
        [rm-start-offset
         syntax-nodes-text] (when rm-syntax?
                              (->> (obu/noget+ ancestor-node :?lastChild)
                                   obpr/node->backward-sibling-nodes
                                   (take-while #(and (ts-rules/ts-syntax-node %)
                                                     (not= (obu/noget+ ancestor-node :?firstChild.?id)
                                                           (obu/noget+ % :?id))))
                                   ((juxt (comp #(obu/noget+ % :?previousSibling.?startIndex) last)
                                          (comp str/join
                                                (fn [nodes] (map #(obu/noget+ % :?text) nodes)))))))] 
    (cond-> ctx
      rm-syntax? (-> (update :src obu/str-remove rm-start-offset (+ rm-start-offset (count syntax-nodes-text)))
                     (update :edit-history conj {:type :delete
                                                 :text syntax-nodes-text
                                                 :offset rm-start-offset
                                                 :src src}))
      (and rm-syntax? (>= offset rm-start-offset)) (update :offset - (- offset rm-start-offset)))))

(defn remove-computed-property
  "Given a context, `ctx`, containing
   a ancestor node, `ancestor-node`, 
   and a child node, `child-node`, 
   returns an updated context with computed property 
   brackets removed from the src -if applicable, 
   else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [offset edit-history src ancestor-node child-node]
    :or {edit-history []}
    :as ctx}]
  (let [computed-prop-node (when (and child-node (ts-rules/incomplete-ts-object-node ancestor-node))
                             (ts-rules/ts-computed-property-name-node
                              (ocall child-node :?childForFieldName "key")))
        remove-brackets? (boolean computed-prop-node)
        [opening-remove-offset
         closing-remove-offset] (when remove-brackets?
                                  (let [opening-offset (obu/update-offset
                                                        (obu/noget+ computed-prop-node :?startIndex)
                                                        edit-history)
                                        closing-offset (obu/update-offset
                                                        (obu/noget+ computed-prop-node :?endIndex)
                                                        edit-history)]
                                    [opening-offset (dec closing-offset)]))]
    (cond-> ctx
      remove-brackets? (assoc :src (-> src
                                       (obu/str-remove closing-remove-offset)
                                       (obu/str-remove opening-remove-offset))
                              :edit-history (conj edit-history
                                                  {:type :delete
                                                   :offset closing-remove-offset
                                                   :text "]"}
                                                  {:type :delete
                                                   :offset opening-remove-offset
                                                   :text "["}))
      (and remove-brackets? (>= offset closing-remove-offset)) (update :offset - 2)
      (and remove-brackets? (> closing-remove-offset offset) (>= offset opening-remove-offset)) (update :offset dec))))

(defn remove-pair-separator
  "Given a context, `ctx`, containing
   a ancestor node, `ancestor-node`, 
   and a child node, `child-node`, 
   returns an updated context with pair separators i.e. 
   colons removed from the src -if applicable, 
   else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [offset edit-history ancestor-node child-node src]
    :or {edit-history []}
    :as ctx}]
  (let [rm-separator? (or (ts-rules/incomplete-ts-object-node ancestor-node)
                          (ts-rules/incomplete-ts-object-type-node ancestor-node))
        rm-offset (when rm-separator?
                    (obu/update-offset
                     (obu/noget+ child-node :?lastChild.?startIndex)
                     edit-history))]
    (if rm-separator?
      (-> ctx
          (update :src obu/str-remove rm-offset)
          (update :edit-history conj {:type :delete
                                      :text ":"
                                      :offset rm-offset
                                      :src src})
          (cond-> (>= offset rm-offset) (update :offset dec)))
      ctx)))

(defn insert-semicolon
      "Given a context, `ctx`, containing
       a ancestor node, `ancestor-node`, 
       and a child node, `child-node`, 
       returns an updated context with a semicolon inserted in the src -if applicable, 
       else returns the given `ctx`

       e.g.
       ```ts
        const foo = {a: ▌{b: number};}; => const foo = {a: ▌{};b: number;};
        //--------------------------------------------------------------^
       ```
       
       Also updates the offset and edit-history of the given context 
       if updates were made"
      [{:keys [src offset edit-history ancestor-node child-node]
        :or {edit-history []}
        :as ctx}]
      (let [insert-semicolon? (and (ts-rules/ts-object-type-node ancestor-node)
                                   (cond
                                    (and (ts-rules/ts-type-annotation-node (obu/noget+ ancestor-node :?parent))
                                         (-> ancestor-node
                                             (obu/noget+ :?parent.?parent.?nextSibling.?type)
                                             (#{";" ","})
                                             not)) true
                                    (not (ts-rules/ts-type-annotation-node (obu/noget+ ancestor-node :?parent))) true
                                    :else false))
            insert-offset (when insert-semicolon?
                                (obu/update-offset
                                 (obu/noget+ child-node :?endIndex)
                                 edit-history))]
        (if insert-semicolon?
          (-> ctx
              (update :src obu/str-insert ";" insert-offset)
              (update :edit-history conj {:type :insert
                                          :offset insert-offset
                                          :text ";"
                                          :src src})
              (cond-> (>= offset insert-offset) (update :offset inc)))
          ctx)))

(defn insert-item-separator
  "Given a context, `ctx`, containing
   a ancestor node, `ancestor-node`, 
   and a forward node, `forward-node`, 
   returns an updated context with an [item] separator i.e. 
   comma or semicolon inserted in the src -if applicable, 
   else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [src offset edit-history ancestor-node child-node]
    :or {edit-history []}
    :as ctx}]
  (let [separator (cond (and (or (ts-rules/not-empty-ts-arguments-node ancestor-node)
                                 (ts-rules/not-empty-ts-collection-node ancestor-node)
                                 (ts-rules/ts-statement-block-node ancestor-node))
                             (some #(contains? #{ts-rules/ts-array ts-rules/ts-arguments ts-rules/ts-pair} %)
                                   [(obu/noget+ ancestor-node :?parent.?type)
                                    (obu/noget+ ancestor-node :?parent.?parent.?type)]))
                        \,
                        (and (ts-rules/ts-object-type-node ancestor-node)
                             (not (ts-rules/ts-syntax-node (obu/noget+ ancestor-node :?nextSibling)))) 
                        \;
                        :else nil)
        insert-offset (when separator
                        (obu/update-offset
                         (obu/noget+ child-node :?startIndex)
                         edit-history))]
    (cond-> ctx
      separator (assoc :src (obu/str-insert src separator insert-offset)
                               :edit-history (conj edit-history {:type :insert
                                                                 :offset insert-offset
                                                                 :text (str separator)
                                                                 :src src}))
      (and separator (>= offset insert-offset)) (update :offset inc))))

(defn remove-item-separators
  "Given a context, `ctx`, a ancestor node, `ancestor-node`, 
   and a child node, `child-node`, 
   returns an updated context with [item] separators i.e. 
   commas or semicolons removed from the src -if applicable, 
   else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [offset edit-history ancestor-node child-node]
    :or {edit-history []}
    :as ctx}]
  (let [rm-separators? (and (or (ts-rules/ts-arguments-node ancestor-node)
                                (ts-rules/not-empty-ts-array-node ancestor-node)
                                (ts-rules/incomplete-ts-object-node ancestor-node)
                                (and (ts-rules/ts-object-ends-with-pair ancestor-node)
                                     (ts-rules/ts-pair-node (obu/noget+ ancestor-node :?parent))))
                            (ts-rules/ts-syntax-node (obu/noget+ child-node :?previousSibling)))
        separators (when rm-separators?
                     (take-while #(and (ts-rules/ts-syntax-node %)
                                       (not= (obu/noget+ ancestor-node :?firstChild.?id)
                                             (obu/noget+ % :?id)))
                                 (obpr/node->backward-sibling-nodes child-node)))]
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
  "Given a context, `ctx` containing
   a ancestor node, `ancestor-node`, 
   and a child node, `child-node`, 
   returns an updated context with the end node(s) of the 
   given ancestor node moved to the start of the given child
   node; these changes are reflected in the src of the `ctx`
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [src offset edit-history ancestor-node child-node]
    :or {edit-history []} :as ctx}]
  (let [current-end-nodes (ts-rules/end-nodes ancestor-node)
        current-end-node-text (->> current-end-nodes
                                   (map #(obu/noget+ % :?text))
                                   str/join)
        current-end-node-start-index (-> current-end-nodes
                                         first
                                         (obu/noget+ :?startIndex)
                                         (obu/update-offset edit-history))
        current-end-node-end-index (-> current-end-nodes
                                       last
                                       (obu/noget+ :?endIndex)
                                       (obu/update-offset edit-history))
        end-node-insert-offset (obu/update-offset
                                (obu/noget+ child-node :?startIndex)
                                edit-history)
        end-node-removed (obu/str-remove src
                                         current-end-node-start-index
                                         current-end-node-end-index)]
    (cond-> (assoc ctx
                   :src (obu/str-insert end-node-removed
                                        current-end-node-text
                                        end-node-insert-offset)
                   :edit-history (conj edit-history
                                       {:type :delete
                                        :offset current-end-node-start-index
                                        :text current-end-node-text
                                        :src src}
                                       {:type :insert
                                        :offset end-node-insert-offset
                                        :text current-end-node-text
                                        :src end-node-removed}))
      (<= current-end-node-start-index
          offset
          (dec current-end-node-end-index)) (assoc :offset (+ (- offset current-end-node-start-index)
                                                              end-node-insert-offset)))))

(defn forward-barf
  "Given a `src` string and character `offset`, 
   returns a map containing a new `src` string 
   with the forward barf operation applied at the offset 
   and a new `offset` containing where the cursor position 
   should be after the barf

   Accepts an optional third `tree-id` argument which 
   specifies the ID of an existing Tree-sitter tree that 
   should be used

   Accepts an optional fourth `tsx?` argument which specifies 
   if the `src` should be parsed as TSX
  
   e.g.
   ```tsx
   <><div>📍Hello, World!</div></>
   =>
   <><div>📍</div>Hello, World!</>
   ```"
  ([src offset] (forward-barf src offset nil))
  ([src offset tree-id] (forward-barf src offset tree-id false))
  ([src offset tree-id tsx?]
   {:pre [(string? src) (>= offset 0)]}
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src (if tsx? obp/tsx-lang-id obp/ts-lang-id)))]
     (when-let [{:keys [last-child-object-node
                        current-node]} (some-> tree
                                               (obu/noget+ :?rootNode)
                                               (ts-rules/node->current-last-child-object-ctx offset)
                                               (update :last-child-object-node
                                                       #(cond 
                                                          ;; const foo = ▌{a: 1} => const foo = ▌{a: } 1 
                                                          ;; const foo = {a: ▌{b: c}} => const foo = {a: ▌{}, b: c}
                                                          (and (ts-rules/ts-pair-node %)
                                                               (not (ts-rules/ts-pair-node (obu/noget+ % :?parent.?parent))))
                                                          (ocall % :?childForFieldName "value")

                                                          :else %)))]
       (-> {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node
            :target-node last-child-object-node}
           move-end-nodes
           remove-superfluous-syntax
           ts-clean/unescape-comments
           ts-clean/escape-template-string
           ts-clean/unescape-escape-sequence
           remove-computed-property
           remove-pair-separator
           remove-item-separators
           insert-item-separator
           insert-semicolon
           (select-keys [:src :offset]))))))

(comment
  ;; Examples
 (-> "<div><></></div>"
     (obp/src->tree! obp/tsx-lang-id)
     (obu/noget+ :?rootNode)
     (ts-rules/node->current-last-child-object-ctx 1))
 (let [src "<><div><input/></div></>"
       offset 0
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (move-end-nodes ctx))
 (let [src "[foo(a, b)];"
       offset 5
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          remove-item-separators))
 (let [src "{a: {b: c,}}"
       offset 4
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          insert-item-separator))
 (let [src "[{a: }];"
       offset 2
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          remove-pair-separator))
 (let [src "[{[a]: }];"
       offset 2
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          remove-computed-property))
 (let [src "'\\''"
       offset 1
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :target-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          ts-clean/unescape-escape-sequence))
 (let [src "`${`${``}`}`"
       offset 1
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          ts-clean/escape-template-string))
 (ts-clean/escaped-comment-backslash-offsets "\\/* \\/* *\\/ *\\/")
 (let [src "/* \\/* \\/* *\\/ *\\/ */"
       offset 1
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          ts-clean/unescape-comments)) 
 (let [src "const foo = {a: 1,}"
       offset 12
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          remove-superfluous-syntax))
 (let [src "type foo = {a: string};"
       offset 11
       {:keys [last-child-object-node
               current-node]} (-> src
                                  (obp/src->tree! obp/tsx-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (ts-rules/node->current-last-child-object-ctx offset))
       ctx {:src src
            :offset offset
            :ancestor-node current-node
            :child-node last-child-object-node}]
      (-> ctx
          move-end-nodes
          insert-semicolon)
      (obu/noget+ current-node :?parent)))
