(ns owlbear.parser.html.rules
  "Tree-sitter rule (type) definitions"
  (:require [oops.core :refer [oget]]))

(def html-comment "comment")
(def html-element "element")
(def html-end-tag "end_tag")
(def html-erroneous-end-tag "erroneous_end_tag")
(def html-script "script_element")
(def html-self-closing-tag "self_closing_tag")
(def html-style "style_element")
(def html-start-tag "start_tag")
(def html-text "text")
(def rule-types #{html-comment html-element html-text html-end-tag html-erroneous-end-tag})

(defn end-tag-node
  "Given a node, 
   returns the node if it is an end-tag node, 
   regardless of whether the end-tag is erroneous or not"
  [node]
  (when (contains? #{html-end-tag html-erroneous-end-tag} (oget node :?type))
    node))

(defn start-tag-node
  "Given a node, 
   returns the node if it is a start-tag node"
  [node]
  (when (= html-start-tag (oget node :?type))
    node))

(defn comment-node
  "Given a node, 
   returns the node if it is a comment node"
  [node]
  (when (= html-comment (oget node :?type))
    node))

(defn node->end-tag-node
  "Given a node, 
   returns the end-tag node for that node if available
   
   If the node is a comment, 
   then a pseudo node is returned 
   representing the comment's end tag"
  [node]
  (if (comment-node node)
    (let [end-index (oget node :?endIndex)]
      #js {:startIndex (- end-index 3)
           :endIndex end-index
           :text "-->"})
    (some end-tag-node (oget node :?children))))

(defn node->start-tag-node
  "Given a node, 
   returns the start-tag node for that node if available
   
   If the node is a comment, 
   then a pseudo node is returned 
   representing the comment's start tag"
  [node]
  (if (comment-node node)
    (let [start-index (oget node :?startIndex)]
      #js {:startIndex start-index
           :endIndex (+ start-index 4)
           :text "<!--"})
    (some start-tag-node (oget node :?children))))