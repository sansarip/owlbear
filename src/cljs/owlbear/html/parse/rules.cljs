(ns owlbear.html.parse.rules
  "Tree-sitter rule (type) definitions"
  (:require [clojure.string :as str]
            [oops.core :refer [oget ocall]]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :as obu]))

(def html-attribute "attribute")
(def html-comment "comment")
(def html-comment-start-tag "comment_start_tag")
(def html-comment-end-tag "comment_end_tag")
(def html-doctype "doctype")
(def html-element "element")
(def html-end-tag "end_tag")
(def html-error "ERROR")
(def html-erroneous-end-tag "erroneous_end_tag")
(def html-fragment "fragment")
(def html-quoted-attribute-value "quoted_attribute_value")
(def html-missing "MISSING")
(def html-script "script_element")
(def html-self-closing-tag "self_closing_tag")
(def html-style "style_element")
(def html-start-tag "start_tag")
(def html-tag-name "tag_name")
(def html-text "text")
(def rule-types #{html-comment html-element html-text html-end-tag html-erroneous-end-tag})

(defn attribute-node
  "Given a node, 
   returns the node if it is an attribute node"
  [node]
  (when (= html-attribute (oget node :?type))
    node))

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

(defn quoted-attribute-value-node
  "Given a node, 
   returns the node if it is an quoted attribute value"
  [node]
  (when (= html-quoted-attribute-value (oget node :?type))
    node))

(defn missing-node
  "Given a node, 
   returns the node if it is a missing node"
  [node]
  (when (or (= html-missing (oget node :?type)) (ocall node :?isMissing))
    node))

(defn start-tag-node
  "Given a node, 
   returns the node if it is a start-tag node"
  [node]
  (when (contains? #{html-comment-start-tag html-start-tag}
                   (oget node :?type))
    node))

(defn text-node
  "Given a node, 
   returns the node if it is a text node"
  [node]
  (when (= html-text (oget node :?type))
    node))

(defn element-node
  "Given a node, 
   returns the node if it is an element node"
  [node]
  (when (= html-element (oget node :?type))
    node))

(defn implicit-nameless-element-node
  "Given a node, 
   returns the node if it is an implicit nameless element node
   
   This implementation is more of a hack; ideally these nodes would be identified via the grammar"
  [node]
  (when (and (element-node node)
             (some-> (oget node :?firstChild)
                     start-tag-node
                     (oget :?text)
                     (= "<>"))
             (some-> (oget node :?lastChild)
                     end-tag-node
                     (oget :?text)
                     (not= "</>")))
    node))

(defn comment-node
  "Given a node, 
   returns the node if it is a comment node"
  [node]
  (when (= html-comment (oget node :?type))
    node))

(defn self-closing-tag-node
  "Given a node, 
   returns the node if it is a self-closing tag node"
  [node]
  (when (= html-self-closing-tag (oget node :?type))
    node))

(defn tag-name-node
  "Given a node, 
   returns the node if it is a tag-name node"
  [node]
  (when (= html-tag-name (oget node :?type))
    node))

(defn doctype
  "Given a node, 
   return the node if it is a doctype node"
  [node]
  (when (= html-doctype (oget node :?type))
    node))

(defn tag-node
  "Given a node, 
   returns the node if it is any kind of tag node"
  [node]
  (when (or (start-tag-node node)
            (end-tag-node node)
            (self-closing-tag-node node))
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
                     html-error
                     html-script
                     html-style}
                   node-type) node
        (= html-text node-type) (when-not (obpr/all-white-space-chars node)
                                  node)
        :else nil))))

(defn node->current-subject-nodes
  "Given a `node` and an `offset`, 
   returns a lazy seq of all the subject nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/node->descendants
          (->> (filter subject-node))
          (obpr/filter-current-nodes offset)))

(defn node->current-object-nodes
  "Given a `node` and an `offset`, 
   returns a lazy seq of all the object nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/node->descendants
          (->> (filter object-node))
          (obpr/filter-current-nodes offset)))

(defn node->forward-object-node
  "Given a `node`, 
   returns the first sibling node in the forward direction 
   that is an object node"
  [node]
  (obpr/some-forward-sibling-node object-node node))

(defn node->backward-object-node
  "Given a `node`, 
   returns the first sibling node in the backward direction 
   that is an object node"
  [node]
  (obpr/some-backward-sibling-node object-node node))

(defn node->current-forward-object-ctx
  "Given a `node` and character `offset`, 
   returns a map of the deepest node (containing the `offset`)
   with a forward sibling object context"
  ([node offset]
   (node->current-forward-object-ctx node offset {}))
  ([node offset {:keys [object-nodes?]}]
   {:pre [(<= 0 offset)]}
   (let [current-nodes-fn (if object-nodes?
                            node->current-object-nodes
                            node->current-subject-nodes)]
     (some->> (current-nodes-fn node offset)
              (keep (fn [current-node]
                      (when-let [forward-object-node (node->forward-object-node current-node)]
                        {:forward-object-node forward-object-node
                         :current-node current-node})))
              last))))

(defn node->current-backward-object-ctx
  "Given a `node` and character `offset`, 
   returns a map of the deepest node (containing the `offset`)
   with a forward sibling object context"
  ([node offset]
   (node->current-forward-object-ctx node offset {}))
  ([node offset {:keys [object-nodes?]}]
   {:pre [(<= 0 offset)]}
   (let [current-nodes-fn (if object-nodes?
                            node->current-object-nodes
                            node->current-subject-nodes)]
     (some->> (current-nodes-fn node offset)
              (keep (fn [current-node]
                      (when-let [backward-object-node (node->backward-object-node current-node)]
                        {:backward-object-node backward-object-node
                         :current-node current-node})))
              last))))

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

(defn content-range
  "Given a `node`, 
   returns the start and end indices
   of the content area within a node"
  [node]
  (let [node-start (obu/noget+ node :?startIndex)
        node-end (obu/noget+ node :?endIndex)
        start-node (node->start-tag-node node)
        end-node (node->end-tag-node node)]
    (if (and start-node end-node)
      [(obu/noget+ start-node :?endIndex)
       (obu/noget+ end-node :?startIndex)]
      [node-start node-end])))

(defn insignicantly-in-node?
  "Returns true of the `offset` is 
   an insignificant area of the given `node`"
  [node offset]
  (let [node-start (obu/noget+ node :?startIndex)
        [content-start content-end] (content-range node)]
    (and (<= content-start offset content-end)
         (boolean (some-> node
                          (obu/noget+ :?text)
                          (get (- offset node-start))
                          str/blank?)))))

(defn at-tag-node-bounds?
  "Returns true if the `offset` is 
   at the start or end of the given `tag-node`"
  [tag-node offset]
  (let [self-closing-node? (self-closing-tag-node tag-node)
        has-forward-slash? (or self-closing-node? (end-tag-node tag-node))
        tag-node-start (obu/noget+ tag-node :?startIndex)
        tag-node-end (obu/noget+ tag-node :?endIndex)]
    (contains? #{tag-node-start
                 (dec tag-node-end)
                 (when has-forward-slash? (inc tag-node-start))}
               offset)))
