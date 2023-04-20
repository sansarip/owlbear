(ns owlbear.html.edit.delete
  (:require [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :as obu]
            [owlbear.html.parse.rules :as html-rules]))

(defn start-of-start-tag? [node offset]
  (cond
    (html-rules/comment-start-tag-node node) (obpr/range-in-node? node offset)

    (or (html-rules/start-tag-node node)
        (html-rules/self-closing-tag-node node)) (= (obu/noget+ node :?startIndex) offset)))

(defn end-of-end-tag? [node offset]
  (let [dec-end-index (dec (obu/noget+ node :?endIndex))]
    (cond
      (html-rules/comment-end-tag-node node) (obpr/range-in-node? node offset)

      (or (html-rules/end-tag-node node)
          (html-rules/self-closing-tag-node node)) (= dec-end-index offset)

      (and
       (html-rules/start-tag-node node)
       (->  node
            (obu/noget+ :?parent)
            html-rules/implicit-node)) (= dec-end-index offset))))

(defn backspace [src offset]
  {:src (obu/str-remove src (dec offset) offset)
   :offset (dec offset)
   :delete-start-offset (dec offset)
   :delete-end-offset offset})

(defn delete-forward-char [src offset]
  {:src (obu/str-remove src offset)
   :offset offset
   :delete-start-offset offset
   :delete-end-offset (inc offset)})

(defn delete [src node]
  (let [node-start (obu/noget+ node :?startIndex)
        node-end (obu/noget+ node :?endIndex)]
    {:src (obu/str-remove src node-start node-end)
     :offset node-start
     :delete-start-offset node-start
     :delete-end-offset node-end}))

(defn delete-quoted-attribute [src node offset direction]
  (when (html-rules/quoted-attribute-value-node node)
    (let [{:keys [del-char
                  delete-offset
                  move-offset]} (case direction
                                  :backward {:del-char backspace
                                             :delete-offset (dec offset)
                                             :move-offset (dec offset)}
                                  :forward {:del-char delete-forward-char
                                            :delete-offset offset
                                            :move-offset (inc offset)})
          empty-quotes? (= "\"\"" (obu/noget+ node :?text))
          at-bounds? (contains? #{(obu/noget+ node :?startIndex)
                                  (dec (obu/noget+ node :?endIndex))}
                                delete-offset)
          at-bound? (case direction
                      :backward (= (obu/noget+ node :?startIndex) delete-offset)
                      :forward (= (dec (obu/noget+ node :?endIndex)) delete-offset))]
      (cond
        (and at-bound? empty-quotes?) (delete src node)
        at-bounds? {:offset move-offset}
        :else (del-char src offset)))))

(defn empty-implicit-nameless-element-node? [node]
  (boolean (and (html-rules/implicit-nameless-element-node node)
                (not (html-rules/text-node (obu/noget+ node :?children.?1))))))

(defn empty-element? [tag-node]
  (let [self-closing-node? (html-rules/self-closing-tag-node tag-node)
        element-node (obu/noget+ tag-node :?parent)]
    (or (every? html-rules/tag-node (obu/noget+ element-node :?children))
        self-closing-node?
        (empty-implicit-nameless-element-node? element-node))))

(defn no-attributes? [element-node]
  (let [start-tag-children (some-> element-node
                                   (obu/noget+ :?firstChild)
                                   html-rules/start-tag-node
                                   (obu/noget+ :?children)
                                   seq)]
    (if (seq? start-tag-children)
      (not (some html-rules/attribute-node start-tag-children))
      true)))

(defn delete-element [src tag-node offset direction]
  (when (html-rules/tag-node tag-node)
    (let [{:keys [at-bound?
                  del-char
                  delete-offset
                  move-offset]} (case direction
                                  :backward {:at-bound? start-of-start-tag?
                                             :del-char backspace
                                             :delete-offset (dec offset)
                                             :move-offset (dec offset)}
                                  :forward {:at-bound? end-of-end-tag?
                                            :del-char delete-forward-char
                                            :delete-offset offset
                                            :move-offset (inc offset)})
          element-node (obu/noget+ tag-node :?parent)
          at-bounds? (html-rules/at-tag-node-bounds? tag-node delete-offset)]
      (cond
        (and (at-bound? tag-node delete-offset)
             (empty-element? tag-node)
             (no-attributes? element-node)) (delete src element-node)
        at-bounds? {:offset move-offset}
        :else (del-char src offset)))))

(defn delete-implicit-nameless-element [src tag-node offset direction]
  (when (html-rules/tag-node tag-node)
    (when-let [element-node (obu/noget+ tag-node :?parent)]
      (when (html-rules/implicit-nameless-element-node element-node)
        (let [{:keys [at-bound?
                      del-char
                      delete-offset
                      move-offset]} (case direction
                                      :backward {:at-bound? start-of-start-tag?
                                                 :del-char backspace
                                                 :delete-offset (dec offset)
                                                 :move-offset (dec offset)}
                                      :forward {:at-bound? end-of-end-tag?
                                                :del-char delete-forward-char
                                                :delete-offset offset
                                                :move-offset (inc offset)})
              no-attributes? (not (obpr/some-child-node html-rules/attribute-node tag-node))
              at-bounds? (html-rules/at-tag-node-bounds? tag-node delete-offset)]
          (cond
            (and (at-bound? tag-node delete-offset)
                 (empty-element? tag-node)
                 no-attributes?) (delete src tag-node)
            at-bounds? {:offset move-offset}
            :else (del-char src offset)))))))

(defn backward-delete
  ([src offset]
   (backward-delete src offset nil))
  ([src offset tree-id]
   {:pre [(<= 0 offset)]}
   (or (let [tree (or (obp/get-tree tree-id)
                      (obp/src->tree! src obp/html-lang-id))
             delete-offset (max (dec offset) 0)]
         (when-let [current-node (some-> tree
                                         (obu/noget+ :?rootNode)
                                         (obpr/node->current-nodes delete-offset)
                                         (->> (filter #(or (html-rules/tag-node %)
                                                           (html-rules/quoted-attribute-value-node %))))
                                         last)]
           (or (delete-implicit-nameless-element src current-node offset :backward)
               (delete-element src current-node offset :backward)
               (delete-quoted-attribute src current-node offset :backward))))
       (backspace src offset))))

(defn forward-delete
  ([src offset]
   (forward-delete src offset nil))
  ([src offset tree-id]
   {:pre [(<= 0 offset)]}
   (or (let [tree (or (obp/get-tree tree-id)
                      (obp/src->tree! src obp/html-lang-id))]
         (when-let [current-node (some-> tree
                                         (obu/noget+ :?rootNode)
                                         (obpr/node->current-nodes offset)
                                         (->> (filter #(or (html-rules/tag-node %)
                                                           (html-rules/quoted-attribute-value-node %))))
                                         last)]
           (or (delete-implicit-nameless-element src current-node offset :forward)
               (delete-element src current-node offset :forward)
               (delete-quoted-attribute src current-node offset :forward))))
       (delete-forward-char src offset))))

(comment
  (backward-delete "<d></d>" 2)
  (backward-delete "<div></div>" 1)
  (backward-delete "<div>5678</div>" 14)
  (backward-delete "<div></div>" 2)
  (backward-delete "<></>" 1)
  (backward-delete "<><p></p>" 1)
  (backward-delete "<!-- -->" 4)
  (backward-delete "<!---->" 6)
  (backward-delete "<!-- adasd -->" 6)
  (backward-delete "<input/>" 1)
  (backward-delete "<input/>" 2)
  (backward-delete "<i/>" 2)
  (backward-delete "</>" 1)
  (backward-delete "<V />" 3)
  (backward-delete "<V a=\"adasd\"/>" 8)
  (backward-delete "<V a=\"\"/>" 6)
  (backward-delete "<V a=\"\"/>" 7)
  (backward-delete "<p><p>Hello</p>" 1)
  (backward-delete "<mm />" 6)
  (backward-delete "<p id=\"foo\"></p>" 14)
  (backward-delete "<></>" 1)

  (forward-delete "<div></div>" 0)
  (forward-delete "<div></div>" 10)
  (forward-delete "<div>s</div>" 11)
  (forward-delete "<div id=\"\"></div>" 9)
  (forward-delete "<p><p></p>" 2)
  (forward-delete "<><p></p>" 1))