(ns owlbear.parse.rules
  "General utility tooling around any Tree-sitter tree"
  (:require [oops.core :refer [oget]]
            [owlbear.utilities :as obu]))

(defn range-in-node?
  "Given a node, a start offset, and a stop offset, 
   returns true if the range is within the given node (inclusive)"
  ([node start]
   (range-in-node? node start start))
  ([node start stop]
   {:pre [(<= start stop)]}
   (<= (oget node :startIndex)
       start
       stop
       (dec (oget node :endIndex)))))

(defn flatten-children
  "Given a node, 
   returns a flattened, depth-first traversed, lazy sequence 
   of all of the node's children and their children"
  [node]
  (tree-seq #(oget % :?children) #(oget % :?children) node))

(defn node->ancestors
  "Given a node, 
   returns a list of the node's ancestors 
   i.e. parents and parents of parents etc."
  [node]
  (some->> (obu/noget+ node :?parent) ; Start at parent
           (iterate #(obu/noget+ (or % #js {}) :?parent))
           (take-while some?)))

(defn nodes->current-nodes
  "Given a list of nodes and a character offset, 
   returns a lazy sequence of only the nodes containing the given offset"
  [offset nodes]
  {:pre [(<= 0 offset)]}
  (filter #(range-in-node? % offset) nodes))

(defn filter-current-nodes
  "Given a sequence of nodes and a character offset, 
   returns the lazy sequence of nodes that contain the given offset"
  [nodes offset]
  {:pre [(<= 0 offset)]}
  (filter #(range-in-node? % offset) nodes))

(defn node->current-nodes
  "Given a node and a character offset, 
   return a lazy sequence of the child nodes containing the given offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (filter-current-nodes (flatten-children node) offset))

(defn node->backward-sibling-nodes
  "Given a node, 
   returns a lazy sequence of the node's backward sibling nodes"
  [node]
  (when node
    (some->> (obu/noget+ node :?previousSibling) ; Start at previous sibling
             (iterate #(obu/noget+ (or % #js {}) :?previousSibling))
             (take-while some?))))

(defn node->forward-sibling-nodes
  "Given a node, 
   returns a lazy sequence of the node's forward sibling nodes"
  [node]
  (when node
    (some->> (obu/noget+ node :?nextSibling) ; Start at next sibling
             (iterate #(obu/noget+ (or % #js {}) :?nextSibling))
             (take-while some?))))

(defn some-forward-sibling-node
  "Given a predicate function, `pred`, and a `node`, 
   returns the first forward sibling node that fulfills the predicate function"
  [pred node]
  {:pre [(fn? pred)]}
  (some pred (node->forward-sibling-nodes node)))

(defn some-child-node
  "Given a predicate function, `pred`, and a `node`, 
   returns the first child that fulfills the predicate function"
  [pred node]
  {:pre [(fn? pred)]}
  (some pred (rest (flatten-children node))))

(defn every-child-node?
  "Given a predicate function, `pred`, and a `node`, 
   returns true if every child that fulfills the predicate function"
  [pred node]
  {:pre [(fn? pred)]}
  (every? pred (rest (flatten-children node))))

(defn some-parent-node
  "Given a predicate function, `pred`, and a `node`, 
   returns the first parent that fulfills the predicate function"
  [pred node]
  {:pre [(fn? pred)]}
  (some pred (node->ancestors node)))

(defn all-white-space-chars
  "Given a `node`, 
   returns the `node` if the node contains only whitespace chars"
  [node]
  (when (some-> node
                (obu/noget+ :?text)
                (->> (re-matches #"\s+")))
    node))
