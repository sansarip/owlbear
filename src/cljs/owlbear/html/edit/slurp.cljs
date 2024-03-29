(ns owlbear.html.edit.slurp
  (:require  [oops.core :refer [oget]]
             [owlbear.parse :as obp]
             [owlbear.html.parse.rules :as html-rules]
             [owlbear.parse.rules :as obpr]
             [owlbear.utilities :as obu]))

(defn node->forward-slurp-subjects
  "Given a `node`, 
   returns all nodes within the node, including the given node itself, 
   that are forward-slurp subjects"
  [node]
  (filter (comp not-empty
                #(filter html-rules/object-node %)
                obpr/node->forward-sibling-nodes
                html-rules/subject-node)
          (obpr/node->descendants node)))

(defn forward-slurp
  "Given a `src` string and character `offset`, 
   returns a map containing a new `src` string 
   with the forward slurp operation applied at the offset 
   and a new `offset` containing where the cursor position 
   should be after the slurp

   Accepts an optional third `tree-id` argument which 
   specifies the ID of an existing Tree-sitter tree that 
   should be used

   e.g.
   ```html
     <div>📍</div><h1></h1> => <div>📍<h1></h1></div>
   ```"
  ([src offset]
   (forward-slurp src offset nil))
  ([src offset tree-id]
   {:pre [(string? src) (<= 0 offset)]}
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src obp/html-lang-id))]
     (when-let [{:keys [forward-object-node
                        current-node]} (html-rules/node->current-forward-object-ctx
                                        (obu/noget+ tree :?rootNode)
                                        offset)]
       (when-let [current-node-end-tag (html-rules/node->end-tag-node current-node)]
         (let [current-node-end-tag-start-index (oget current-node-end-tag :?startIndex)
               current-node-end-tag-end-index (oget current-node-end-tag :?endIndex)
               current-node-end-tag-text (oget current-node-end-tag :?text)
               forward-object-node-end-index (oget forward-object-node :?endIndex)
               end-tag-insert-offset (- forward-object-node-end-index (count current-node-end-tag-text))]
           {:src (-> src
                     (obu/str-remove current-node-end-tag-start-index current-node-end-tag-end-index)
                     (obu/str-insert current-node-end-tag-text end-tag-insert-offset))
            :offset (if (obpr/range-in-node? current-node-end-tag offset)
                      (+ (- offset current-node-end-tag-start-index)
                         end-tag-insert-offset)
                      offset)}))))))

(comment
  ;; Examples
  (forward-slurp "<div></div><h1></h1>" 0)
  ;; => {:src "<div><h1></h1></div>" :offset 0}
  (forward-slurp "<div><h1>Hello</h1></div><h2>World</h2>" 7)
  ;; => {:src "<div><h1>Hello</h1><h2>World</h2></div>" :offset 7}
  )