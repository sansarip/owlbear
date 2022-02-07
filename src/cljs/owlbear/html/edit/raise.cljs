(ns owlbear.html.edit.raise
  (:require [oops.core :refer [oget]]
            [owlbear.html.parse :as ob-html]
            [owlbear.html.parse.rules :as ob-html-rules]
            [owlbear.utilities :as obu]))

(defn raise
  "Given a `src` string and character `offset`, 
   returns a new src string with the raise operation applied at the `offset`

   e.g.
   ```html
     <div><📍h1></h1></div> => <📍h1></h1>
   ```"
  [src offset]
  (when-let [current-node (-> src
                              ob-html/src->tree
                              (oget :?rootNode)
                              (ob-html-rules/node->current-object-nodes offset)
                              last)]
    (let [current-node-text (oget current-node :?text)
          current-node-start (oget current-node :?startIndex)
          current-parent-node (oget current-node :?parent)
          current-parent-node-start (oget current-parent-node :?startIndex)
          current-parent-node-end (oget current-parent-node :?endIndex)]
      #js{:src (-> src
                   (obu/str-remove current-parent-node-start current-parent-node-end)
                   (obu/str-insert current-node-text current-parent-node-start))
          :offset (dec (- current-node-start (- offset current-node-start)))})))