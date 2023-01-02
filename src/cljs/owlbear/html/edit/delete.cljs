(ns owlbear.html.edit.delete
  (:require [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :as obu]
            [owlbear.html.parse.rules :as html-rules]))

(defn backspace [src offset]
  {:src (obu/str-remove src (dec offset) offset)
   :offset (dec offset)
   :delete-start-offset (dec offset)
   :delete-end-offset offset})


(defn delete [src node]
  (let [node-start (obu/noget+ node :?startIndex)
        node-end (obu/noget+ node :?endIndex)]
    {:src (obu/str-remove src node-start node-end)
     :offset node-start
     :delete-start-offset node-start
     :delete-end-offset node-end}))

(defn delete-quoted-attribute [src node offset]
  (when (html-rules/quoted-attribute-value-node node)
    (let [delete-offset (dec offset)
          empty-quotes? (= "\"\"" (obu/noget+ node :?text))
          at-bounds? (contains? #{(obu/noget+ node :?startIndex)
                                  (dec (obu/noget+ node :?endIndex))}
                                delete-offset)]
      (cond
        empty-quotes? (delete src node)
        at-bounds? {:offset delete-offset}
        :else (backspace src offset)))))

(defn empty-implicit-nameless-element-node? [node]
  (boolean (and (html-rules/implicit-nameless-element-node node)
                (not (html-rules/text-node (obu/noget+ node :?children.?1))))))

(defn empty-element? [tag-node]
  (let [self-closing-node? (html-rules/self-closing-tag-node tag-node)
        element-node (obu/noget+ tag-node :?parent)]
    (or (every? html-rules/tag-node (obu/noget+ element-node :?children))
        self-closing-node?
        (empty-implicit-nameless-element-node? element-node))))

(defn delete-element [src tag-node offset]
  (when (html-rules/tag-node tag-node)
    (let [delete-offset (dec offset)
          element-node (obu/noget+ tag-node :?parent)
          no-attributes? (not (obpr/some-child-node html-rules/attribute-node tag-node))
          at-bounds? (html-rules/at-tag-node-bounds? tag-node delete-offset)]
      (cond
        (and at-bounds?
             (empty-element? tag-node)
             no-attributes?) (delete src element-node)
        at-bounds? {:offset delete-offset}
        :else (backspace src offset)))))

(defn delete-implicit-nameless-element [src tag-node offset]
  (when (html-rules/tag-node tag-node)
    (when-let [element-node (obu/noget+ tag-node :?parent)]
      (when (html-rules/implicit-nameless-element-node element-node)
        (let [delete-offset (dec offset)
              no-attributes? (not (obpr/some-child-node html-rules/attribute-node tag-node))
              at-bounds? (html-rules/at-tag-node-bounds? tag-node delete-offset)]
          (cond 
            (and at-bounds?
                 (empty-element? tag-node)
                 no-attributes?) (delete src tag-node)
            at-bounds? {:offset delete-offset}
            :else (backspace src offset)))))))

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
           (or (delete-implicit-nameless-element src current-node offset)
               (delete-element src current-node offset)
               (delete-quoted-attribute src current-node offset))))
       (backspace src offset))))

(comment
  (backward-delete "<d></d>" 2)
  (backward-delete "<div></div>" 1)
  (backward-delete "<div>5678</div>" 14)
  (backward-delete "<div></div>" 2)
  (backward-delete "<!---->" 1)
  (backward-delete "<input/>" 1)
  (backward-delete "<input/>" 2)
  (backward-delete "<i/>" 2)
  (backward-delete "</>" 1)
  (backward-delete "<V />" 3)
  (backward-delete "<V a=\"adasd\"/>" 8)
  (backward-delete "<V a=\"\"/>" 6)
  (backward-delete "<p><p>Hello</p>" 1)
  (backward-delete "<mm />" 6))