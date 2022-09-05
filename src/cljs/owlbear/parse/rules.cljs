(ns owlbear.parse.rules
  "General utility tooling around any Tree-sitter tree"
  (:require [owlbear.utilities :as obu]))

(defn range-in-node?
  "Given a node, a start offset, and a stop offset, 
   returns true if the range is within the given node (inclusive)"
  ([^js node start]
   (range-in-node? node start start))
  ([^js node start stop]
   {:pre [(<= start stop)]}
   (if node
     (<= (.-startIndex node)
         start
         stop
         (dec (.-endIndex node)))
     false)))

(defn node->descendants
  "Given a node, 
   returns a flattened, depth-first traversed, lazy sequence 
   of all of the node's descendants"
  [^js node]
  (tree-seq #(.-children %) #(.-children %) node))

(defn node->ancestors
  "Given a node, 
   returns a list of the node's ancestors 
   i.e. parents and parents of parents etc."
  [^js node]
  (when node
    (some->> (.-parent node) ; Start at parent
             (iterate #(.-parent (or % #js {})))
             (take-while some?))))

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
  (filter-current-nodes (node->descendants node) offset))

(defn node->backward-sibling-nodes
  "Given a node, 
   returns a lazy sequence of the node's backward sibling nodes"
  [^js node]
  (when node
    (some->> (.-previousSibling node) ; Start at previous sibling
             (iterate #(.-previousSibling (or % #js {})))
             (take-while some?))))

(defn node->forward-sibling-nodes
  "Given a node, 
   returns a lazy sequence of the node's forward sibling nodes"
  [^js node]
  (when node
    (some->> (.-nextSibling node) ; Start at next sibling
             (iterate #(.-nextSibling (or % #js {})))
             (take-while some?))))

(defn some-forward-sibling-node
  "Given a predicate function, `pred`, and a `node`, 
   returns the first forward sibling node that fulfills the predicate function"
  [pred node]
  {:pre [(fn? pred)]}
  (some pred (node->forward-sibling-nodes node)))

(defn some-backward-sibling-node
  "Given a predicate function, `pred`, and a `node`, 
   returns the first backward sibling node that fulfills the predicate function"
  [pred node]
  {:pre [(fn? pred)]}
  (some pred (node->backward-sibling-nodes node)))

(defn some-child-node
  "Given a predicate function, `pred`, and a `node`, 
   returns the first child node that fulfills the predicate function"
  [pred ^js node]
  {:pre [(fn? pred)]}
  (when node
    (some pred (.-children node))))

(defn some-descendant-node
  "Given a predicate function, `pred`, and a `node`, 
   returns the first truthy value of the predicate function 
   applied to the node's descendants"
  [pred node]
  {:pre [(fn? pred)]}
  (some pred (rest (node->descendants node))))

(defn filter-children
  "Given a predicate function, `pred`, and a `node`, 
   returns a lazy seq of all the child nodes 
   that fulfill the predicate function"
  [pred ^js node]
  {:pre [(fn? pred)]}
  (when node
    (filter pred (.-children node))))

(defn filter-descendants
  "Given a predicate function, `pred`, and a `node`, 
   returns a lazy seq of all the descendant nodes 
   that fulfill the predicate function"
  [pred node]
  {:pre [(fn? pred)]}
  (filter pred (rest (node->descendants node))))

(defn every-descendant?
  "Given a predicate function, `pred`, and a `node`, 
   returns true if every descendant node fulfills the predicate function"
  [pred node]
  {:pre [(fn? pred)]}
  (every? pred (rest (node->descendants node))))

(defn some-ancestor-node
  "Given a predicate function, `pred`, and a `node`, 
   returns the first truthy value of the predicate 
   function applied to the node's descendants"
  [pred node]
  {:pre [(fn? pred)]}
  (some pred (node->ancestors node)))

(defn all-white-space-chars
  "Given a `node`, 
   returns the `node` if the node contains only whitespace chars"
  [^js node]
  (when (some-> node
                .-text
                (->> (re-matches #"\s+")))
    node))

(defn distinct-by-start-index
  "Given a sequence of nodes, 
   returns a sequence of the nodes 
   deduped by their start indices"
  [nodes]
  (vals
   (reduce
    (fn [coll ^js node]
      (if node
        (assoc coll (.-startIndex node) node)
        node))
    {}
    nodes)))

(defn node->same-start-ancestors
  "Given a `node`, 
   returns a lazy sequence of the `node`'s ancestors 
   that have the same start index"
  [^js node]
  (let [start-index (.-startIndex node)]
    (->> node
         (iterate #(.-parent (or % #js {})))
         (take-while
          #(= (.-startIndex ^js %) start-index))
         rest)))