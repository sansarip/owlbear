(ns owlbear.html.edit.move
  (:require [owlbear.html.parse.rules :as html-rules]
            [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :as obu]))

(defn forward-move
  "Given a src string and an offset, 
   returns the offset of the next, sibling object node 
   from the offset"
  [src offset]
  {:pre [(<= 0 offset)]}
  (let [root-node (obu/noget+ (obp/src->tree src obp/html-lang-id) :?rootNode)
        root-node-start (obu/noget+ root-node :?startIndex)]
    (when-let [current-node (-> root-node
                                obpr/node->descendants
                                (obpr/filter-current-nodes offset)
                                (->> (filter #(or (html-rules/fragment-node %)
                                                  (html-rules/object-node %))))
                                last
                                ;; This is because the root-node (a fragment node) is not always the full document
                                (or (when (<= offset root-node-start) root-node)))]
      (let [current-node-end-tag (html-rules/node->end-tag-node current-node)
            current-node-start-tag (html-rules/node->start-tag-node current-node)
            tagged-node? (and current-node-start-tag current-node-end-tag)
            offset-in-tag? (and tagged-node? (or (obpr/range-in-node? current-node-start-tag offset)
                                                 (obpr/range-in-node? current-node-end-tag offset)))
            forward-object-node (html-rules/next-forward-object-node current-node)
            forward-object-node-start (obu/noget+ forward-object-node :?startIndex)
            move-laterally? (and (or offset-in-tag? (not tagged-node?))
                                 forward-object-node)]
        (cond
          move-laterally? {:offset forward-object-node-start}
          :else (some-> (obpr/some-child-node
                         #(and (<= offset (obu/noget+ % :?startIndex))
                               (html-rules/object-node %))
                         current-node)
                        (obu/noget+ :?startIndex)
                        (->> (hash-map :offset))))))))

(comment
  ;; Examples
  (forward-move "\n\n<!-- Beep, boop! -->" 0)
  (forward-move "<header></header>\n \n<body></body>" 18)
  (obu/noget+ (obp/src->tree "\n\n<!-- Beep, boop! -->" obp/html-lang-id) :?rootNode.?children))
