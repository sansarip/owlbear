(ns owlbear.ts.edit.kill
  (:require [owlbear.parse :as obp]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :as obu]))

(defn kill
  "Given a `src` string and a character `offset`, 
   returns a map containing a new `src` string 
   with the kill operation applied at the offset 
   and a new `offset` containing where the cursor 
   position should be after the delete 
   
   Accepts an optional third `tree-id` argument which 
   specifies the ID of an existing Tree-sitter tree that 
   should be used

   Accepts an optional fourth `tsx?` argument which 
   specifies if the `src` should be parsed as TSX
   
   e.g.
   ```tsx
   <>📍<div>Hello, World!</div></>
   =>
   <></>
   ```"
  ([src offset] (kill src offset nil))
  ([src offset tree-id] (kill src offset tree-id false))
  ([src offset tree-id tsx?]
   {:pre [(string? src) (>= offset 0)]}
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src (if tsx? obp/tsx-lang-id obp/ts-lang-id)))]
     (when-let [current-nodes (some-> tree
                                      (obu/noget+ :?rootNode)
                                      (ts-rules/node->current-object-nodes offset)
                                      (->> (filter (comp not ts-rules/ts-member-expression-node)))
                                      not-empty)]
       (let [last-node-start-index (obu/noget+ (last current-nodes) :?startIndex)
             current-node (->> current-nodes
                               reverse
                               (take-while #(= (obu/noget+ % :?startIndex)
                                               last-node-start-index))
                               last)
             current-node-end-index (let [parent-node (obu/noget+ current-node :?parent)
                                          next-sibling (obu/noget+ current-node :?nextSibling)
                                          next-sibling-type (obu/noget+ next-sibling :?type)
                                          next-sibling-end-index (obu/noget+ next-sibling :?endIndex)]
                                      (cond
                                        ;; foo().▌bar() => foo().  
                                        (ts-rules/ts-member-expression-node parent-node)
                                        (obu/noget+ (obu/noget+ parent-node :?nextSibling) :?endIndex)
                                        
                                        ;; [▌1, 2] => [ 2]
                                        (= "," next-sibling-type)
                                        next-sibling-end-index

                                        ;; {▌a:;} => {}
                                        (and (contains? #{ts-rules/ts-property-signature
                                                          ts-rules/ts-incomplete-property-signature}
                                                        (obu/noget+ current-node :?type))
                                             (= ";" next-sibling-type))
                                        next-sibling-end-index

                                        :else (obu/noget+ current-node :?endIndex)))
             current-node-start-index (let [parent (obu/noget+ current-node :?parent)
                                            prev-sibling (obu/noget+ current-node :?previousSibling)
                                            prev-sibling-type (obu/noget+ prev-sibling :?type)
                                            prev-sibling-start-index (obu/noget+ prev-sibling :?startIndex)]
                                        (cond
                                          ;; foo.▌bar() => foo
                                          (and (ts-rules/ts-member-expression-node (obu/noget+ current-node :?parent))
                                               (= "." prev-sibling-type))
                                          prev-sibling-start-index

                                          ;; [1, ▌2] => [1]
                                          (and (= "," prev-sibling-type)
                                               (not= "," (obu/noget+ current-node :?nextSibling.?type)))
                                          prev-sibling-start-index

                                          ;; [...▌foo] => []
                                          (ts-rules/ts-spread-element-node parent)
                                          (obu/noget+ parent :?startIndex)

                                          :else  (obu/noget+ current-node :?startIndex)))]
         {:src (obu/str-remove src current-node-start-index current-node-end-index)
          :offset current-node-start-index
          :removed-text (obu/noget+ current-node :?text)})))))

(comment
  (kill "<><h1></h1></>" 2 :tsx)
  (kill "type foo = {a: string;}" 12 :tsx)
  (kill "type foo = {abc: string;}" 17 :tsx)
  (kill "const {a, b} = foo;" 7 :tsx))