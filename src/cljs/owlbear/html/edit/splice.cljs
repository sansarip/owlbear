(ns owlbear.html.edit.splice
  (:require [oops.core :refer [oget]]
            [owlbear.html.parse.rules :as html-rules]
            [owlbear.parse :as obp]
            [owlbear.utilities :as obu]))

(defn node->splice-content-ctx [node offset]
  (let [abs-node-start (oget node :?startIndex)
        abs-node-end (oget node :?endIndex)
        rel-node-start 0
        rel-node-end (- abs-node-end abs-node-start)
        content-nodes (->> (oget node :?children)
                           (filter #(and (not (html-rules/start-tag-node %))
                                         (not (html-rules/end-tag-node %)))))
        abs-content-start (oget (first content-nodes) :?startIndex)
        abs-content-end (oget (last content-nodes) :?endIndex)
        rel-content-start (- abs-content-start abs-node-start)
        rel-content-end (- abs-content-end abs-node-start)
        content (subs (oget node :?text) rel-content-start rel-content-end)
        edit-history (as-> [{:type :delete
                             :text (subs (obu/noget+ node :?text)
                                         rel-node-start
                                         rel-content-start)
                             :offset abs-node-start}] $
                       (conj $ {:type :delete
                                :text (subs (obu/noget+ node :?text)
                                            rel-content-end
                                            rel-node-end)
                                :offset (obu/update-offset abs-content-end $)}))]
    {:content-start abs-content-start
     :content-end abs-content-end
     :content content
     :offset  (obu/update-offset offset edit-history)}))

(defn splice
  "Given a `src` string and character `offset`, 
   returns a new src string with the splice operation applied at the `offset`

   Accepts an optional third `tree-id` argument which 
   specifies the ID of an existing Tree-sitter tree that 
   should be used

   e.g.
   ```html
     <div📍><h1></h1></div> => 📍<h1></h1>
   ```"
  ([src offset]
   (splice src offset nil))
  ([src offset tree-id]
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src obp/html-lang-id))
         root-node (oget tree :?rootNode)]
     (when-let [current-node (some->> offset
                                      (html-rules/node->current-subject-nodes root-node)
                                      (filter (complement html-rules/empty-node?))
                                      last)]
       (let [current-node-start (oget current-node :?startIndex)
             current-node-end (oget current-node :?endIndex)
             {:keys [content] offset* :offset} (node->splice-content-ctx current-node offset)]
         {:src (-> src
                   (obu/str-remove current-node-start current-node-end)
                   (obu/str-insert content current-node-start))
          :offset offset*})))))

(comment
  (splice "<div><h1></h1></div>" 0)
  (splice "<div><h1></h1></div>" 1)
  (splice "<div><h1></h1></div>" 16)
  (splice "<!DOCTYPE html>\n<html>\n  <body>\n    <h1>My First Heading</h1>\n\n    <p>My first paragraph.</p>\n  </body>\n</html>" 68))
