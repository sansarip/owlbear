(ns owlbear.ts.edit.move
  (:require [oops.core :refer [ocall]]
            [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :as obu]))

(defn next-child-offset
  "Given a `node`, 
   returns the offset of the first non-white-space/syntax node 
   in the forward direction if applicable, else nil"
  [node offset]
  (let [node-start (obu/noget+ node :?startIndex)
        [content-start content-end] (ts-rules/content-range node)]
    (when (and (< (dec content-start) offset content-end)
               (some-> node
                       (obu/noget+ :?text)
                       (get (- offset node-start))
                       ts-rules/insignificant-str?))
      (some-> (obpr/some-child-node
               #(and (<= offset (obu/noget+ % :?startIndex))
                     (ts-rules/object-node %))
               node)
              (obu/noget+ :?startIndex)))))

(defn next-sibling-offset
  "Given a `node`, 
   returns the offset of 
   the next sibling object node 
   of the given `node` or one of the 
   ancestors of the given `node`
   
   Returns nil if there is no such node"
  [node]
  (let [current-node (cond-> node
                       (or (ts-rules/expression-node (obu/noget+ node :?parent))
                           (ts-rules/expression-node node)
                           (ts-rules/subject-node node))
                       (ts-rules/subject-container-node
                        {:for-any-node? true
                         :container-type-greenlist #{ts-rules/ts-lexical-declaration}
                         :container-type-redlist #{ts-rules/ts-member-expression}}))
        current-parent-node (obu/noget+ current-node :?parent)
        ancestor-signature (obpr/some-ancestor-node
                            #(when (contains? #{ts-rules/ts-incomplete-pair
                                                ts-rules/ts-property-signature
                                                ts-rules/ts-required-parameter}
                                              (obu/noget+ % :?type))
                               %)
                            current-node)
        forward-object-node (ts-rules/next-forward-object-node
                             (cond
                               (when current-parent-node
                                 (or
                                  ;; const foo = {a: ▌1, b: 2} => const foo = {a: 1, ▌b: 2}
                                  (some-> (or (ocall current-parent-node :?childForFieldName "type")
                                              (ocall current-parent-node :?childForFieldName "value"))
                                          (obu/noget+ :?id)
                                          (= (obu/noget+ current-node :?id)))

                                  ;; [...▌foo, bar] => [...foo, ▌bar]
                                  ;; /* ▌foo */ bar(); => /* foo */ ▌bar();
                                  (contains? #{ts-rules/ts-spread-element
                                               ts-rules/ts-comment-block}
                                             (obu/noget+ current-parent-node :?type))))
                               current-parent-node

                               ;; type foo = {:a ▌number; b: string} => type foo = {:a number; ▌b: string}
                               (and (-> current-node
                                        (obu/noget+ :?nextSibling)
                                        ts-rules/object-node
                                        not)
                                    ancestor-signature)
                               ancestor-signature

                               :else current-node))]
    (if (ts-rules/ts-type-annotation-node forward-object-node)
      (obu/noget+ forward-object-node :?children.?1.?startIndex)
      (obu/noget+ forward-object-node :?startIndex))))

(defn forward-move
  "Given a `src` string and an `offset`, 
   returns the a map containing the 
   offset of the next object node 
   from the offset
   
   Accepts an optional third `tsx?` argument which specifies 
   if the `src` should be parsed as TSX"
  ([src offset]
   (forward-move src offset false))
  ([src offset tsx?]
   {:pre [(<= 0 offset)]}
   (let [root-node (obu/noget+ (obp/src->tree src (if tsx? obp/tsx-lang-id obp/ts-lang-id)) :?rootNode)
         root-node-start (obu/noget+ root-node :?startIndex)]
     (when-let [current-node (-> root-node
                                 obpr/node->descendants
                                 (obpr/filter-current-nodes offset)
                                 (->> (filter #(or (ts-rules/ts-program-node %)
                                                   (ts-rules/object-node %))))
                                 last
                                 ;; This is because the root-node (a program node) is not always the full text
                                 (or (when (<= offset root-node-start) root-node)))]
       (some->> (or (when (< offset root-node-start) root-node-start)
                    (next-child-offset current-node offset)
                    (next-sibling-offset current-node))
                (hash-map :offset))))))

(comment
  (forward-move "<div><h1></h1><h2></h2></div>" 5 :tsx)
  (forward-move "type foo = {a: number; b: string;}" 15 :tsx)
  (forward-move "const foo = await bar();\nbaz();" 12 :tsx)
  (forward-move "const foo = {a, b: 2}" 13 :tsx)
  (forward-move "function foo (a= foo(), b, c) {}" 17 :tsx))
