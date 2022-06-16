(ns owlbear.ts.edit.raise
  (:require [owlbear.parse :as obp]
            [owlbear.ts.edit.clean :as ts-clean]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :as obu]))

(defn raise-ctx
  "Given a `node`, and an `offset`, 
   returns a map containing a node 
   to raise, `current-node`, and an 
   ancestor node to replace, `ancestor-node`"
  [node offset]
  (let [[first-current-node :as current-nodes] (reverse (ts-rules/node->current-object-nodes node offset))
        [current-node-index
         current-node] (loop [i 0
                              [node & rest-nodes] current-nodes
                              prev-result [0 node]]
                         (if (and node
                                  (not (contains?
                                        #{ts-rules/ts-incomplete-pair
                                          ts-rules/ts-incomplete-property-signature
                                          ts-rules/ts-pair
                                          ts-rules/ts-property-signature}
                                        (obu/noget+ node :?type)))
                                  (= (obu/noget+ first-current-node :?startIndex)
                                     (obu/noget+ node :?startIndex)))
                           (recur (inc i) rest-nodes [i node])
                           prev-result))
        current-ancestor-node (some #(or (when-let [sc-node (ts-rules/subject-container-node %)]
                                           (let [parent-node (obu/noget+ % :?parent)]
                                             (cond
                                               ;; const a = foo.bar(a).qux(▌b); -targets full left-expression
                                               (ts-rules/ts-call-expression-node parent-node)
                                               parent-node

                                               ;; type foo = {▌a:} -targets declaration value
                                               (contains? #{ts-rules/ts-type-alias-declaration
                                                            ts-rules/ts-lexical-declaration}
                                                          (obu/noget+ sc-node :?type))
                                               %

                                               ;; const foo = ▌bar(); -preserves semicolon
                                               (ts-rules/ts-expression-statement-node sc-node)
                                               (obu/noget+ sc-node :?firstChild)

                                               :else sc-node)))
                                         (ts-rules/top-level-node %))
                                    (-> current-nodes
                                        vec
                                        (subvec current-node-index)
                                        rest))]
    (and current-node
         current-ancestor-node
         {:current-node current-node
          :current-ancestor-node current-ancestor-node})))

(defn raise-node
  "Given a context, `ctx` containing
   a ancestor node, `ancestor-node`, 
   and a child node, `child-node`, 
   returns an updated context with the child node replacing 
   the given ancestor node; 
   these changes are reflected in the src of the `ctx`
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [src offset edit-history child-node ancestor-node]
    :or {edit-history []}
    :as ctx}]
  (let [child-node-text (obu/noget+ child-node :?text)
        child-node-start (obu/noget+ child-node :?startIndex)
        ancestor-node-start (obu/update-offset
                             (obu/noget+ ancestor-node :?startIndex)
                             edit-history)
        ancestor-node-end (obu/update-offset
                           (obu/noget+ ancestor-node :?endIndex)
                           edit-history)
        ancestor-node-text (obu/noget+ ancestor-node :?text)]
    (assoc ctx
           :src (-> src
                    (obu/str-remove ancestor-node-start ancestor-node-end)
                    (obu/str-insert child-node-text ancestor-node-start))
           :offset (+ ancestor-node-start (- offset child-node-start))
           :edit-history (conj edit-history
                               {:type :delete
                                :offset ancestor-node-start
                                :text ancestor-node-text
                                :src  src}))))

(defn raise
  "Given a `src` string and a character `offset`, 
   returns a map containing a new `src` string 
   with the raise operation applied at the offset 
   and a new `offset` containing where the cursor 
   position should be after the raise 
   
   Accepts an optional third `tsx?` argument which 
   specifies if the `src` should be parsed as TSX
   
   e.g.
   ```tsx
   <><div>📍Hello, World!</div></>
   =>
   <>📍Hello, World!</>
   ```"
  ([src offset]
   (raise src offset false))
  ([src offset tsx?]
   {:pre [(string? src) (>= offset 0)]}
   (when-let [{:keys [current-node
                      current-ancestor-node]} (some-> src
                                                      (obp/src->tree (if tsx? obp/tsx-lang-id obp/ts-lang-id))
                                                      (obu/noget+ :?rootNode)
                                                      (raise-ctx offset))]
     (-> {:src src
          :offset offset
          :target-node current-node
          :child-node current-node
          :ancestor-node current-ancestor-node}
         raise-node
         ts-clean/escape-template-string
         ts-clean/unescape-comments
         ts-clean/unescape-escape-sequence
         (select-keys [:src :offset])))))

(comment
  ;; Examples  
  (raise "const foo = 1;" 12)
  (let [src "<><h1>Hello, World</h1></>"
        offset 5
        {:keys [current-node
                current-ancestor-node]} (some-> src
                                                (obp/src->tree obp/tsx-lang-id)
                                                (obu/noget+ :?rootNode)
                                                (raise-ctx offset))
        ctx {:src src
             :offset offset
             :child-node current-node
             :ancestor-node current-ancestor-node}]
    (raise-node ctx)))
