(ns owlbear.html.edit.delete
  (:require [clojure.string :as str]
            [owlbear.parse :as obp]
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
   {:pre [(<= 0 offset)]}
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src obp/html-lang-id))
         delete-offset (max (dec offset) 0)]
     (if-let [current-tag (some-> tree
                                  (obu/noget+ :?rootNode)
                                  (obpr/node->current-nodes delete-offset)
                                  (->> (filter html-rules/tag-node))
                                  last)]
       (let [parent (obu/noget+ current-tag :?parent)
             self-closing-tag? (html-rules/self-closing-tag-node current-tag)
             tag-name (obpr/some-child-node html-rules/tag-name-node current-tag)
             tag-name-len (count (obu/noget+ tag-name :?text))
             tag-name-start (obu/noget+ tag-name :?startIndex)
             in-tag-name? (obpr/range-in-node? tag-name delete-offset)
             start-of-len-1-tag-name? (and (= tag-name-start delete-offset)
                                           (= tag-name-len 1))
             no-attributes? (not (obpr/some-child-node html-rules/attribute-node current-tag))
             no-significant-children? (if self-closing-tag? 
                                        true                           
                                        (not (obpr/some-child-node html-rules/object-node parent)))
             offset-in-tag (- delete-offset (obu/noget+ current-tag :?startIndex))
             deleting-white-space? (-> current-tag
                                       (obu/noget+ :?text)
                                       (get offset-in-tag)
                                       str/blank?)
             parent-start (obu/noget+ parent :?startIndex)
             parent-end (obu/noget+ parent :?endIndex)] 
         (println "BAH" no-attributes?)
         (cond
           ;; <d▌></d>  
           ;; <div>▌</div> 
           ;; <▌i/>
           ;; <i▌/>
           (and no-significant-children?
                no-attributes?
                (not deleting-white-space?)
                (or start-of-len-1-tag-name?
                    (not in-tag-name?)))
           {:src (obu/str-remove src parent-start parent-end)
            :offset parent-start
            :delete-start-offset parent-start
            :delete-end-offset parent-end}

           ;; <div▌></div>
           ;; <i▌/>
           (or in-tag-name? deleting-white-space?)
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
  (backward-delete "<V />" 3)
  (backward-delete "<V a=\"adasd\"/>" 8)
  (backward-delete "<p><p>Hello</p>" 1)
  (backward-delete "<mm />" 6))