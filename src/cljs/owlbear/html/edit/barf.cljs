(ns owlbear.html.edit.barf
  (:require  [oops.core :refer [oget]]
             [owlbear.html.parse :as obp-html]
             [owlbear.html.edit.rules :as obp-html-edit-rules]
             [owlbear.html.parse.rules :as obp-html-rules]
             [owlbear.parse.rules :as obpr]
             [owlbear.utilities :as obu]))

(defn node->forward-barf-subjects
  "Given a `node`, 
   returns all nodes within the node, including the given node itself, 
   that are forward-barf subjects"
  [node]
  (filter (comp not-empty
                #(filter obp-html-edit-rules/object-node %)
                #(when % (oget % :?children))
                obp-html-edit-rules/subject-node)
          (obpr/flatten-children node)))

(defn forward-barf
  "Given a `src` string and character `offset`, 
   returns a new src string with the forward barf operation applied at the offset
  
   e.g.
   ```html
     <div>📍<h1></h1></div> => <div>📍</div><h1></h1>
   ```"
  [src offset]
  (when-let [{:keys [last-child-object-node
                     current-node]} (obp-html-edit-rules/node->current-last-child-object-ctx
                                     (oget (obp-html/src->tree src) :?rootNode)
                                     offset)]
    (when-let [current-node-end-tag (obp-html-rules/node->end-tag-node current-node)]
      (let [current-node-end-tag-start-index (oget current-node-end-tag :?startIndex)
            current-node-end-tag-end-index (oget current-node-end-tag :?endIndex)
            current-node-end-tag-text (oget current-node-end-tag :?text)
            ;; TODO: When comment node gets implemented, need to add in its tag start because we need to parse comment content separately 
            end-tag-insert-offset (oget last-child-object-node :?startIndex)]
        #js {:src (-> src
                      (obu/str-remove current-node-end-tag-start-index current-node-end-tag-end-index)
                      (obu/str-insert current-node-end-tag-text end-tag-insert-offset))
             :offset (if (obpr/range-in-node? current-node-end-tag offset)
                       (+ (- offset current-node-end-tag-start-index)
                          end-tag-insert-offset)
                       offset)}))))