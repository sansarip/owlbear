(ns owlbear.ts.edit.move
  (:require [oops.core :refer [ocall]]
            [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :as obu]))

(defn node->current-node
  "Given a `node`, 
   and an `offset`, 
   returns the most specific 
   program or object node 
   that contains the offset
   
   returns nil if no such nodes are found"
  [node offset]
  (-> node
      obpr/node->descendants
      (obpr/filter-current-nodes offset)
      (->> (filter #(or (ts-rules/ts-program-node %)
                        (ts-rules/object-node %))))
      last))

(defn child-offset
  "Given a `node`, 
   returns the offset of the first non-white-space/syntax node 
   in the given `direction` if applicable, else nil"
  [node offset direction]
  (let [backward? (= direction :backward)
        operator (if backward? >= <=)
        at-program-end? (and (ts-rules/ts-program-node node) (= offset (obu/noget+ node :?endIndex)))]
    (when (or at-program-end? (ts-rules/insignicantly-in-node? node offset))
      (let [descendants (cond-> (ts-rules/node->child-object-nodes node)
                          backward? reverse)]
        (some-> (some
                 #(when (operator offset (obu/noget+ % :?startIndex))
                    (ts-rules/object-node %))
                 descendants)
                (obu/noget+ :?startIndex))))))

(defn forward-sibling-offset
  "Given a `node`, 
   returns the offset of 
   the sibling object node 
   of the given `node` or
   the offset of the sibling object node 
   of one of the ancestors of the given `node` 
   in the forward direction
   
   Returns nil if there is no such node"
  [node offset]
  (some-> node
          (ts-rules/node->current-forward-object-ctx
           offset
           {:object-nodes? true
            :from-subject-container? false})
          :forward-object-node
          (as-> $
                ;; Avoids the colon and colon in type-annotations      
                (if (ts-rules/ts-type-annotation-node $)
                  (obu/noget+ $ :?children.?1)
                  $))
          (obu/noget+ :?startIndex)))

(defn backward-sibling-offset
  "Given a `node`, 
   returns the offset of 
   the sibling object node 
   of the given `node` or
   the offset of the sibling object node 
   of one of the ancestors of the given `node` 
   in the backward direction
   
   Returns nil if there is no such node"
  [node offset]
  (some-> node
          (ts-rules/node->current-backward-object-ctx
           offset
           {:object-nodes? true
            :from-subject-container? false})
          :backward-object-node
          (as-> $
                (cond
                  ;; Avoids the colon and colon in type-annotations      
                  (ts-rules/ts-type-annotation-node $) (obu/noget+ $ :?children.?1)

                  ;; Use type or value field if possible
                  :else (or  (obu/noget+ (ocall $ :?childForFieldName "type") :?children.?1)
                             (ocall $ :?childForFieldName "value")
                             $)))
          (obu/noget+ :?startIndex)))

(defn forward-move
  "Given a `src` string and an `offset`, 
   returns the a map containing the 
   offset of the next, movable 
   object node from the offset
   
   Accepts an optional third `tsx?` argument which specifies 
   if the `src` should be parsed as TSX"
  ([src offset]
   (forward-move src offset false))
  ([src offset tsx?]
   {:pre [(<= 0 offset)]}
   (let [root-node (obu/noget+ (obp/src->tree src (if tsx? obp/tsx-lang-id obp/ts-lang-id)) :?rootNode)
         root-node-start (obu/noget+ root-node :?startIndex)]
     (when-let [current-node (or (node->current-node root-node offset) ; The root-node (a program node) is not always the full text
                                 (when (<= offset root-node-start) root-node))]
       (some->> (or (when (< offset root-node-start) root-node-start)
                    (child-offset current-node offset :forward)
                    (forward-sibling-offset root-node offset))
                (hash-map :offset))))))

(defn backward-move
  "Given a `src` string and an `offset`, 
   returns the a map containing the 
   offset of the previous, movable
   object node from the offset
   
   Accepts an optional third `tsx?` argument which specifies 
   if the `src` should be parsed as TSX"
  ([src offset]
   (backward-move src offset false))
  ([src offset tsx?]
   {:pre [(<= 0 offset)]}
   (let [root-node (obu/noget+ (obp/src->tree src (if tsx? obp/tsx-lang-id obp/ts-lang-id)) :?rootNode)]
     (when-let [current-node (or (node->current-node root-node offset)
                                 (when (= offset (obu/noget+ root-node :?endIndex)) root-node))]
       (some->> (or (child-offset current-node offset :backward)
                    (backward-sibling-offset root-node offset))
                (hash-map :offset))))))

(comment
  (forward-move "<div><h1></h1><h2></h2></div>" 5 :tsx)
  (forward-move "type foo = {a: number; b: string;}" 15 :tsx)
  (forward-move "const foo = await bar();\nbaz();" 12 :tsx)
  (forward-move "const foo = {a, b: 2}" 13 :tsx)
  (forward-move "function foo (a= foo(), b, c) {}" 17 :tsx)
  (forward-move "type foo = \"hello\" | \"world\" | \"!\"" 11 :tsx)
  (forward-move "function foo (a = bar(), b, c) {}" 14 :tsx)
  (backward-move "function foo (a = bar(), b, c) {}" 18 :tsx)
  (backward-move "() => {};\n(a) => {};\n{H: \"\", aB:};\nimport O3 from \"M/O3\";" 10 :tsx)
  (backward-move "type D = any;\nimport a from \"a\";\n{interface U {A: object; D: any;}}\n() => {return [];};" 58 :tsx)
  (backward-move "<h1>Hello, World!</h1>\n\n" 24 :tsx))