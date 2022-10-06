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

(defn backward-delete
  ([src offset]
   (backward-delete src offset nil))
  ([src offset tree-id]
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src obp/html-lang-id))
         delete-offset (dec offset)]
     (if-let [current-tag (some-> tree
                                  (obu/noget+ :?rootNode)
                                  (obpr/node->current-nodes delete-offset)
                                  (->> (filter #(or (html-rules/start-tag-node %)
                                                    (html-rules/end-tag-node %)
                                                    (html-rules/self-closing-tag-node %))))
                                  last)]
       (let [parent (obu/noget+ current-tag :?parent)
             self-closing-tag? (html-rules/self-closing-tag-node current-tag)
             tag-name (obpr/some-child-node html-rules/tag-name-node current-tag)
             tag-name-len (count (obu/noget+ tag-name :?text))
             tag-name-start (obu/noget+ tag-name :?startIndex)
             in-tag-name? (obpr/range-in-node? tag-name delete-offset)
             start-of-len-1-tag-name? (and (= tag-name-start delete-offset)
                                           (= tag-name-len 1))
             no-significant-children? (not (if self-closing-tag?
                                             (> tag-name-len 1)
                                             (obpr/some-child-node html-rules/object-node parent)))
             parent-start (obu/noget+ parent :?startIndex)
             parent-end (obu/noget+ parent :?endIndex)]
         (cond
           ;; <d▌></d>  
           ;; <div>▌</div> 
           ;; <▌i/>
           ;; <i▌/>
           (and no-significant-children?
                (or start-of-len-1-tag-name?
                    (not in-tag-name?)))
           {:src (obu/str-remove src parent-start parent-end)
            :offset parent-start
            :delete-start-offset parent-start
            :delete-end-offset parent-end}
           
           ;; <div▌></div>
           ;; <i▌/>
           in-tag-name?
           (backspace src offset)
           
           ;; <div>▌Hello, World!</div>
           :else {:offset delete-offset}))
       ;; <div>Hello, World!▌</div>
       (backspace src offset)))))

(comment
  (backward-delete "<d></d>" 2)
  (backward-delete "<div></div>" 1)
  (backward-delete "<div>5678</div>" 6)
  (backward-delete "<div></div>" 2)
  (backward-delete "<!---->" 1)
  (backward-delete "<input/>" 1)
  (backward-delete "<input/>" 2)
  (backward-delete "<i/>" 2)
  (backward-delete "<p><p>Hello</p>" 1))