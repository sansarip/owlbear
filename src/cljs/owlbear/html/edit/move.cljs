(ns owlbear.html.edit.move
  (:require [owlbear.html.parse.rules :as html-rules]
            [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :as obu]))

(defn node->current-node
  "Given a `node`, 
   and an `offset`, 
   returns the most specific 
   fragment or object node 
   that contains the offset
   
   returns nil if no such nodes are found"
  [node offset]
  (-> node
      obpr/node->descendants
      (obpr/filter-current-nodes offset)
      (->> (filter #(or (html-rules/fragment-node %)
                        (html-rules/object-node %))))
      last))

(defn child-offset
  "Given a `node`, 
   returns the offset of the first non-white-space/syntax node 
   in the given `direction` if applicable, else nil"
  [node offset direction]
  (let [backward? (= direction :backward)
        operator (if backward? >= <=)
        at-doc-end? (and (html-rules/fragment-node node) (= offset (obu/noget+ node :?endIndex)))]
    (when (or at-doc-end? (html-rules/insignicantly-in-node? node offset))
      (let [descendants (cond-> (html-rules/node->child-object-nodes node)
                          backward? reverse)]
        (some-> (some
                 #(when (operator offset (obu/noget+ % :?startIndex))
                    (html-rules/object-node %))
                 descendants)
                (obu/noget+ :?startIndex))))))

(defn sibling-offset
  "Given a `node`, 
   returns the offset of 
   the sibling object node 
   of the given `node` or
   the offset of the sibling object node 
   of one of the ancestors of the given `node` 
   in the given `direction`
   
   Returns nil if there is no such node"
  [node offset direction]
  (let [backward? (= direction :backward)
        sibling-fn (if backward?
                     (comp :backward-object-node html-rules/node->current-backward-object-ctx)
                     (comp :forward-object-node html-rules/node->current-forward-object-ctx))]
    (obu/noget+ (sibling-fn
                 node
                 offset
                 {:object-nodes? true})
                :?startIndex)))

(defn forward-move
  "Given a src string and an offset, 
   returns the offset of the next, sibling object node 
   from the offset"
  [src offset]
  {:pre [(<= 0 offset)]}
  (let [root-node (obu/noget+ (obp/src->tree src obp/html-lang-id) :?rootNode)
        root-node-start (obu/noget+ root-node :?startIndex)]
    (when-let [current-node (or (node->current-node root-node offset)
                                (when (<= offset root-node-start) root-node))] ; The root-node (a fragment node) is not always the full document
      (some->> (or (when (< offset root-node-start) root-node-start)
                   (child-offset current-node offset :forward)
                   (sibling-offset root-node offset :forward))
               (hash-map :offset)))))

(defn backward-move
  "Given a src string and an offset, 
   returns the offset of the previous, sibling object node 
   from the offset"
  [src offset]
  {:pre [(<= 0 offset)]}
  (let [root-node (obu/noget+ (obp/src->tree src obp/html-lang-id) :?rootNode)]
    (when-let [current-node (or (node->current-node root-node offset)
                                (when (= offset (obu/noget+ root-node :?endIndex)) root-node))] ; The root-node (a fragment node) is not always the full document
      (some->> (or (child-offset current-node offset :backward)
                   (sibling-offset root-node offset :backward))
               (hash-map :offset)))))

(comment
  ;; Examples
  (forward-move "\n\n<!-- Beep, boop! -->" 0)
  (forward-move "<header></header>\n \n<body></body>" 18)
  (forward-move "<header></header><body></body>" 0)
  (backward-move "<header></header><body></body>" 17))
