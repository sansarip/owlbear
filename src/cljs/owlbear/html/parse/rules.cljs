(ns owlbear.html.parse.rules
  "Tree-sitter rule (type) definitions"
  (:require [oops.core :refer [oget]]
            [owlbear.html.utilities :as ob-html-util]
            [owlbear.parse.rules :as obpr]))

(def html-comment "comment")
(def html-comment-start-tag "comment_start_tag")
(def html-comment-end-tag "comment_end_tag")
(def html-doctype "doctype")
(def html-element "element")
(def html-end-tag "end_tag")
(def html-fragment "fragment")
(def html-erroneous-end-tag "erroneous_end_tag")
(def html-script "script_element")
(def html-self-closing-tag "self_closing_tag")
(def html-style "style_element")
(def html-start-tag "start_tag")
(def html-text "text")
(def rule-types #{html-comment html-element html-text html-end-tag html-erroneous-end-tag})

(defn fragment-node
  "Given a node, 
   returns the node if it is an end-tag node"
  [node]
  (when (= html-fragment (oget node :?type))
    node))

(defn end-tag-node
  "Given a node, 
   returns the node if it is an end-tag node, 
   regardless of whether the end-tag is erroneous or not"
  [node]
  (when (contains? #{html-comment-end-tag html-end-tag html-erroneous-end-tag}
                   (oget node :?type))
    node))

(defn start-tag-node
  "Given a node, 
   returns the node if it is a start-tag node"
  [node]
  (when (contains? #{html-comment-start-tag html-start-tag}
                   (oget node :?type))
    node))

(defn comment-node
  "Given a node, 
   returns the node if it is a comment node"
  [node]
  (when (= html-comment (oget node :?type))
    node))

(defn doctype
  "Given a node, 
   return the node if it is a doctype node"
  [node]
  (when (= html-doctype (oget node :?type))
    node))

(defn node->end-tag-node
  "Given a node, 
   returns the end-tag node for that node if available"
  [node]
  (some end-tag-node (oget node :?children)))

(defn node->start-tag-node
  "Given a node, 
   returns the start-tag node for that node if available"
  [node]
  (some start-tag-node (oget node :?children)))

(defn subject-node
  "Returns the given `node`
   if edit operations can be run from within the node 
   i.e. the node doing the slurping or barfing"
  [node]
  {:pre [(some? node)]}
  (condp = (oget node :?type)
    html-comment node
    html-element (when (not= (oget node :?children.?0.?type)
                             html-self-closing-tag)
                   node)
    nil))

(defn object-node
  "Returns the given `node` 
   if edit operations can be run against the node 
   i.e. the node being slurped or barfed"
  [node]
  (when node
    (when-let [node-type (oget node :?type)]
      (cond
        (contains? #{html-comment
                     html-doctype
                     html-element
                     html-script
                     html-style}
                   node-type) node
        (= html-text node-type) (when-not (ob-html-util/all-white-space-chars* node)
                                  node)
        :else nil))))

(defn node->current-subject-nodes
  "Given a `node` and an `offset`, 
   returns a lazy seq of all the subject nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/flatten-children
          (->> (filter subject-node))
          (obpr/filter-current-nodes offset)))

(defn node->current-object-nodes
  "Given a `node` and an `offset`, 
   returns a lazy seq of all the object nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/flatten-children
          (->> (filter object-node))
          (obpr/filter-current-nodes offset)))

(defn next-forward-object-node [node]
  (obpr/some-forward-sibling-node object-node node))

(defn node->current-forward-object-ctx
  "Given a `node` and character `offset`, 
   returns a map of the deepest node (containing the `offset`)
   with a forward sibling object context"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some->> (node->current-subject-nodes node offset)
           (keep (fn [current-node]
                   (when-let [forward-object-node (next-forward-object-node current-node)]
                     {:forward-object-node forward-object-node
                      :current-node current-node})))
           last))

(defn node->child-object-nodes [node]
  (when node
    (filter object-node (oget node :?children))))

(defn node->current-last-child-object-ctx
  "Given a `node` and character `offset`, 
   returns a map containing the deepest
   node containing the offset with object-node children (`current-node`) 
   and the last child-object node of that `current-node` (`last-child-object-node`)"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some->> (node->current-subject-nodes node offset)
           (keep (fn [current-node]
                   (when-let [last-child-object-node (last (node->child-object-nodes current-node))]
                     {:last-child-object-node last-child-object-node
                      :current-node current-node})))
           last))