(ns owlbear.parse.rules
  "General utility tooling around any Tree-sitter tree"
  (:require [oops.core :refer [oget]]))

(defn range-in-node?
  "Given a node, a start offset, and a stop offset, 
   returns true if the node is within the given range (inclusive)"
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

(defn node->forward-sibling-nodes
  "Given a node, 
   returns a lazy sequence of the node's forward sibling nodes"
  [node]
  (when node
    (some->> (oget node :?nextSibling) ; Start at next sibling
             (iterate #(oget (or % #js {}) :?nextSibling))
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
  (some pred (flatten-children node)))