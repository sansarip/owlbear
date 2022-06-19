(ns owlbear.html.edit.kill
  (:require [oops.core :refer [oget]]
            [owlbear.parse :as obp]
            [owlbear.html.parse.rules :as html-rules]
            [owlbear.utilities :as obu]))

(defn kill
  "Given a `src` string and character `offset`, 
   returns a new src string with the kill operation applied at the `offset`

   e.g.
   ```html
     <div><📍h1></h1></div> => <div></div> 
   ```"
  [src offset]
  {:pre [(string? src) (>= offset 0)]}
  (when-let [current-node (some-> src
                                  (obp/src->tree obp/html-lang-id)
                                  (obu/noget+ :?rootNode)
                                  (html-rules/node->current-object-nodes offset)
                                  last)]
    (let [current-node-start (oget current-node :?startIndex)
          current-node-end (oget current-node :?endIndex)]
      {:src (obu/str-remove src current-node-start current-node-end)
       :offset current-node-start
       :removed-text (obu/noget+ current-node :?text)})))

(comment
  ;; Kill example
  (obp/init-tree-sitter! "./resources/tree-sitter-html.wasm")
  (let [src "<div><h1></h1></div>"
        offset 7
        result (kill src offset)]
    (assert (= {:src "<div></div>" :offset 5} result))))