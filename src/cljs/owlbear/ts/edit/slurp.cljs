(ns owlbear.ts.edit.slurp
  (:require [oops.core :refer [oget]]
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
                                      (oget (obp/src->tree src (if tsx? :tsx :typescript)) :?rootNode)
                                      offset)]
     (when-let [current-end-node (ob-ts-rules/node->first-child current-node)]
       (let [current-end-node-start-index (oget current-end-node :?startIndex)
             current-end-node-end-index (oget current-end-node :?endIndex)
             current-end-node-text (oget current-end-node :?text)
             forward-object-node-end-index (oget forward-object-node :?endIndex)
             end-node-insert-offset (- forward-object-node-end-index (count current-end-node-text))]
         {:src (-> src
                   (obu/str-remove current-end-node-start-index current-end-node-end-index)
                   (obu/str-insert current-end-node-text end-node-insert-offset))
          :offset (if (obpr/range-in-node? current-end-node offset)
                    (+ (- offset current-end-node-start-index)
                       end-node-insert-offset)
                    offset)})))))

(comment
  ;; Examples
  (forward-slurp "const a = 1 + 1; <><div>hello</div><h1>world</h1></>" 26 :tsx))