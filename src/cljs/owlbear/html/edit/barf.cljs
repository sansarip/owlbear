(ns owlbear.html.edit.barf
  (:require  [oops.core :refer [oget]]
             [owlbear.parse :as obp]
             [owlbear.html.parse.rules :as html-rules]
             [owlbear.parse.rules :as obpr]
             [owlbear.utilities :as obu]))

(defn node->forward-barf-subjects
  "Given a `node`, 
   returns all nodes within the node, including the given node itself, 
   that are forward-barf subjects"
  [node]
  (filter (comp not-empty
                #(filter html-rules/object-node %)
                #(when % (oget % :?children))
                html-rules/subject-node)
          (obpr/node->descendants node)))

(defn forward-barf
  "Given a `src` string and character `offset`, 
   returns a map containing a new `src` string 
   with the forward barf operation applied at the offset 
   and a new `offset` containing where the cursor position 
   should be after the barf
  
   Accepts an optional third `tree-id` argument which 
   specifies the ID of an existing Tree-sitter tree that 
   should be used
   
   e.g.
   ```html
     <div>📍<h1></h1></div> => <div>📍</div><h1></h1>
   ```"
  ([src offset]
   (forward-barf src offset nil))
  ([src offset tree-id]
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src obp/html-lang-id))]
     (when-let [{:keys [last-child-object-node
                        current-node]} (html-rules/node->current-last-child-object-ctx
                                        (obu/noget+ tree :?rootNode)
                                        offset)]
       (when-let [current-node-end-tag (html-rules/node->end-tag-node current-node)]
         (let [current-node-end-tag-start-index (oget current-node-end-tag :?startIndex)
               current-node-end-tag-end-index (oget current-node-end-tag :?endIndex)
               current-node-end-tag-text (oget current-node-end-tag :?text)
               end-tag-insert-offset (oget last-child-object-node :?startIndex)]
           {:src (-> src
                     (obu/str-remove current-node-end-tag-start-index current-node-end-tag-end-index)
                     (obu/str-insert current-node-end-tag-text end-tag-insert-offset))
            :offset (if (obpr/range-in-node? current-node-end-tag offset)
                      (+ (- offset current-node-end-tag-start-index)
                         end-tag-insert-offset)
                      offset)}))))))

(comment
  ;; Examples
  (forward-barf "<div><h1></h1></div>" 16)
  ;; => {:src "<div></div><h1></h1>" :offset 7}
  (forward-barf "<div><h1>Hello</h1><h2>World</h2></div>" 0)
  ;; => {:src "<div><h1>Hello</h1></div><h2>World</h2>" :offset 0}
  )