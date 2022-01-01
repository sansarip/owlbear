(ns owlbear.parser.utilities
  (:require [oops.core :refer [oget]]))

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

