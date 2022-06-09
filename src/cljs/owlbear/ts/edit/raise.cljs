(ns owlbear.ts.edit.raise
  (:require [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.parse.rules :as ob-ts-rules]
            [owlbear.utilities :as obu]))

(defn raise
  ([src offset]
   (raise src offset false))
  ([src offset tsx?]
   (when-let [current-node (some-> src
                                   (obp/src->tree (if tsx? obp/tsx-lang-id obp/ts-lang-id))
                                   (obu/noget+ :?rootNode)
                                   (ob-ts-rules/node->current-object-nodes offset)
                                   last)]
     (let [current-node-text (obu/noget+ current-node :?text)
           current-node-start (obu/noget+ current-node :?startIndex)
           current-ancestor-node (ob-ts-rules/ancestor-container-node current-node)
           current-ancestor-node-start (obu/noget+ current-ancestor-node :?startIndex)
           current-ancestor-node-end (obu/noget+ current-ancestor-node :?endIndex)]
       (when current-ancestor-node
         {:src (-> src
                   (obu/str-remove current-ancestor-node-start current-ancestor-node-end)
                   (obu/str-insert current-node-text current-ancestor-node-start))
          :offset (+ current-ancestor-node-start (- offset current-node-start))})))))

(comment
  (raise "const foo = 1;" 12))
