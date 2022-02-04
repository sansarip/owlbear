(ns owlbear.parser.utilities
  "General utility tooling around any Tree-sitter tree"
  (:require [oops.core :refer [oget]]))

;; This node multi-method is an attempt at future proofing in the event of a parsing-library change
(defmulti node*
  "Given a node, an an action, 
   performs the implementation of that action with the node"
  (fn [node action]
    action))

(defmethod node* :start-index [node _]
  (oget node :startIndex))

(defmethod node* :stop-index [node _]
  (oget node :endIndex))

(defmethod node* :children [node _]
  (oget node :children))

(defmethod node* :parent [node _]
  (oget node :parent))

(defmethod node* :next-sibling [node _]
  (oget node :nextSibling))

(defmethod node* :prev-sibling [node _]
  (oget node :previousSibling))

(defn ctx->children-seq
  "Given a context, 
   returns a flattened, depth-first traversed, lazy sequence 
   of all of the context's children"
  [ctx]
  (tree-seq #(oget % :?children) #(oget % :children) ctx))

(defn ctx->parent-seq
  "Given a context,
   recursively traverses the context's parents and 
   returns a vector of all the context's parents"
  [ctx]
  (loop [parent-ctx (oget ctx :?parentCtx)
         parent-ctx-coll []]
    (if parent-ctx
      (recur (oget parent-ctx :?parentCtx) (conj parent-ctx-coll parent-ctx))
      parent-ctx-coll)))

(defn sibling-ctxs
  "Given a context, 
   returns the sibling contexts for that context"
  [ctx]
  (rest (concat (oget ctx :?parentCtx.?children)
                ;; Accounts for scenarios where an HTML element context has a parent HTML elements context
                (oget ctx :?parentCtx.?parentCtx.?children))))

(defn some-sibling-ctx
  "Given a context (and optionally a parent context), 
   returns the first sibling context that fulfills the predicate function"
  [pred ctx]
  (some pred (sibling-ctxs ctx)))

(defn range-in-ctx?
  "Given a context, a start offset, and a stop offset,
   returns true if the context is within the given range (inclusive)"
  ([ctx start]
   (range-in-ctx? ctx start start))
  ([ctx start stop]
   (and start
        stop
        (let [ctx-start (oget ctx :?start.?start)
              ctx-stop (oget ctx :?stop.?stop)]
          (and
           ctx-start
           ctx-stop
           (apply <=
                  (concat [ctx-start]
                          (range start (inc stop))
                          [ctx-stop])))))))

(defn filter-current-ctxs
  "Given a list of contexts and a character offset, 
   a lazy sequence of only the contexts containing the given offset"
  [offset ctxs]
  (filter #(range-in-ctx? % offset) ctxs))

(defn ctx->current-ctxs
  "Given a context and a character offset, 
   return a lazy sequence of the contexts containing the given offset"
  [ctx offset]
  (filter-current-ctxs offset (ctx->children-seq ctx)))

(defn forward-ctx?
  "Given a context and a second context, 
   returns true if the second context is positionall ahead of the first context"
  [ctx forward-ctx]
  (< (oget ctx :?stop.?stop) (oget forward-ctx :?start.?start)))

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
  (->> node
       (iterate #(oget (or % #js {}) :?nextSibling))
       (take-while some?)
       rest))

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