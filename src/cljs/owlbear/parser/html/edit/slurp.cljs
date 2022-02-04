(ns owlbear.parser.html.edit.slurp
  (:require  [oops.core :refer [oget]]
             [owlbear.parser.html :as obp-html]
             [owlbear.parser.html.rules :as obp-html-rules]
             [owlbear.parser.html.edit.rules :as obp-html-edit-rules]
             [owlbear.parser.utilities :as obpu]
             [owlbear.utilities :as obu]))

(defn node->forward-slurp-subjects
  "Given a `node`, 
   returns all nodes within the node, including the given node itself, 
   that are forward-slurp subjects"
  [node]
  (filter (comp not-empty
                #(filter obp-html-edit-rules/object-node %)
                obpu/node->forward-sibling-nodes
                obp-html-edit-rules/subject-node)
          (obpu/flatten-children node)))

(defn forward-slurp
  "Given a `src` string and character `offset`, 
   returns a new src string with the forward slurp operation applied at the `offset`

   e.g.
   ```html
     <div>📍</div><h1></h1> => <div>📍<h1></h1></div>
   ```"
  [src offset]
  {:pre [(string? src) (<= 0 offset)]}
  (when-let [{:keys [forward-object-node
                     current-node]} (obp-html-edit-rules/node->current-forward-object-ctx
                                     (oget (obp-html/src->tree src) :?rootNode)
                                     offset)]
    (when-let [current-node-end-tag (obp-html-rules/node->end-tag-node current-node)]
      (let [current-node-end-tag-start-index (oget current-node-end-tag :?startIndex)
            current-node-end-tag-end-index (oget current-node-end-tag :?endIndex)
            current-node-end-tag-text (oget current-node-end-tag :?text)
            forward-object-node-end-index (oget forward-object-node :?endIndex)
            end-tag-insert-offset (- forward-object-node-end-index (count current-node-end-tag-text))]
        #js {:src (-> src
                      (obu/str-remove current-node-end-tag-start-index current-node-end-tag-end-index)
                      (obu/str-insert current-node-end-tag-text end-tag-insert-offset))
             :offset (if (obpu/range-in-node? current-node-end-tag offset)
                       (+ (- offset current-node-end-tag-start-index)
                          end-tag-insert-offset)
                       offset)}))))

(comment
  (forward-slurp "<div><h1></h1>hello</div><div></div>" 6)
  (forward-slurp "<table><table></table><table></table></table>" 8))