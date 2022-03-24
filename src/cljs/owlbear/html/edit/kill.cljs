(ns owlbear.html.edit.kill
  (:require [oops.core :refer [oget]]
            [owlbear.parse :as ob-html]
            [owlbear.html.parse.rules :as ob-html-rules]
            [owlbear.utilities :as obu]))

(defn kill [src offset]
  (when-let [current-node (-> src
                              (ob-html/src->tree :html)
                              (oget :?rootNode)
                              (ob-html-rules/node->current-object-nodes offset)
                              last)]
    (let [current-node-start (oget current-node :?startIndex)
          current-node-end (oget current-node :?endIndex)]
      {:src (obu/str-remove src current-node-start current-node-end)
       :offset current-node-start})))

(comment
  ;; Kill example
  (ob-html/init-tree-sitter! "./resources/tree-sitter-html.wasm")
  (let [src "<div><h1></h1></div>"
        offset 7
        result (kill src offset)]
    (assert (= {:src "<div></div>" :offset 5} result))))