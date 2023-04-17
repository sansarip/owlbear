(ns owlbear.ts.edit.delete
  (:require [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :as obu]
            [owlbear.ts.parse.rules :as ts-rules]))

(defn backspace [src offset]
  {:src (obu/str-remove src (dec offset) offset)
   :offset (dec offset)
   :delete-start-offset (dec offset)
   :delete-end-offset offset})

(defn delete [src node]
  (let [node-start (obu/noget+ node :?startIndex)
        node-end (obu/noget+ node :?endIndex)]
    {:src (obu/str-remove src node-start node-end)
     :offset node-start
     :delete-start-offset node-start
     :delete-end-offset node-end}))

(defn at-boundaries? [boundary-nodes offset]
  (let [nodes->boundaries (fn [node]
                            (let [offsets (ts-rules/node->boundary-offsets node)]
                              (update offsets (dec (count offsets)) dec)))
        boundary-offsets (into #{} (mapcat nodes->boundaries) boundary-nodes)]
    (contains? boundary-offsets offset)))


(defn no-attributes?
  ([node] (no-attributes? node ts-rules/jsx-element))
  ([node classification]
   (let [self-closing-element? (= ts-rules/jsx-self-closing-element classification)
         tag-children (some-> node
                              (cond->
                               (not self-closing-element?) (obu/noget+ :?firstChild))
                              (obu/noget+ :?children)
                              seq)]
     (if (seq? tag-children)
       (not (some ts-rules/jsx-attribute-node tag-children))
       true))))

(defn delete-node
  [{:keys [current-node delete-offset]
    at-boundaries?* :at-boundaries?
    at-start?* :at-start?}]
  (when at-boundaries?*
    (when-let [src (obu/noget+ current-node :?tree.?rootNode.?text)]
      (if (and at-start?* (ts-rules/empty-node? current-node))
        (delete src current-node)
        {:offset delete-offset}))))

(defn delete-element
  [{:keys [current-node delete-offset]
    at-boundaries?* :at-boundaries?
    at-start?* :at-start?}]
  (when (and (ts-rules/jsx-element-node current-node) at-boundaries?*)
    (when-let [src (obu/noget+ current-node :?tree.?rootNode.?text)]
      (if (and at-start?* (no-attributes? current-node) (ts-rules/empty-node? current-node))
        (delete src current-node)
        {:offset delete-offset}))))

(defn delete-self-closing-element
  [{:keys [classification current-node delete-offset]
    at-boundaries?* :at-boundaries?
    at-start?* :at-start?}]
  (when (and (= ts-rules/jsx-self-closing-element classification) at-boundaries?*)
    (when-let [src (obu/noget+ current-node :?tree.?rootNode.?text)]
      (if (and at-start?* (no-attributes? current-node classification))
        (delete src current-node)
        {:offset delete-offset}))))

(defn group-syntax-node [node]
  (when (re-matches #"[`'/\"]" (obu/noget+ node :?type))
    node))

(defn group-start-syntax-node [node]
  (when (re-matches #"[\(\{\[\<]" (obu/noget+ node :?type))
    node))

(defn group-end-syntax-node [node]
  (when (re-matches #"[`\)\}\]\>]" (obu/noget+ node :?type))
    node))

(defn start-node [node]
  (or (group-syntax-node node) (group-start-syntax-node node) (ts-rules/start-node node)))

(defn end-node [node]
  (or (group-syntax-node node) (group-end-syntax-node node) (ts-rules/end-node node)))

(defn ->delete-ctx [root-node offset]
  (let [delete-offset (dec offset)
        {:keys [current-node
                classification]} (some-> root-node
                                         (obpr/node->current-nodes delete-offset)
                                         (->> (keep #(some-> (cond
                                                               (ts-rules/jsx-element-node %) {:classification ts-rules/jsx-element}
                                                               (ts-rules/jsx-self-closing-element-node %) {:classification ts-rules/jsx-self-closing-element}
                                                               (ts-rules/ts-computed-property-name-node %) {:classification ts-rules/ts-computed-property-name}
                                                               (ts-rules/ts-formal-parameters-node %) {:classification ts-rules/ts-formal-parameters}
                                                               (ts-rules/subject-node %) {:classification :subject}
                                                               :else nil)
                                                             (assoc :current-node %))))
                                         last)]
    (when current-node
      (let [start-nodes (ts-rules/start-nodes current-node start-node)
            ;; Below can be done in a recursive manner to get the deepest start nodes, 
            ;; but this works for now
            one-lvl-deeper-start-nodes (into []
                                             (mapcat (fn [sn]
                                                       (if-let [snodes (ts-rules/start-nodes sn start-node)]
                                                         snodes
                                                         [sn])))
                                             start-nodes)
            end-nodes (ts-rules/end-nodes current-node end-node)
            boundary-nodes (into start-nodes end-nodes)]
        {:current-node current-node
         :boundary-nodes boundary-nodes
         :classification classification
         :delete-offset delete-offset
         :at-boundaries? (at-boundaries? boundary-nodes delete-offset)
         :at-start? (at-boundaries? one-lvl-deeper-start-nodes delete-offset)}))))

(defn backward-delete
  "Given a `src` string and a character `offset`, 
   returns:
   
   * a map containing a new `src` string 
   with the backward-delete operation applied at the offset, 
   a new `offset` containing where the cursor 
   position should be after the delete, 
   a `delete-start-offset`,
   and a `delete-end-offset` 
   -if deletion took place
   * a map containing a decremented offset 
   -if deletion was not possible
   
   Accepts an optional third `tree-id` argument which 
   specifies the ID of an existing Tree-sitter tree that 
   should be used

   Accepts an optional fourth `tsx?` argument which 
   specifies if the `src` should be parsed as TSX
   
   e.g.
   ```tsx
   <><div>📍</div></>
   =>
   <></>
   ```"
  ([src offset]
   (backward-delete src offset nil))
  ([src offset tree-id]
   (backward-delete src offset tree-id false))
  ([src offset tree-id tsx?]
   {:pre [(<= 0 offset)]}
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src (if tsx? obp/tsx-lang-id obp/ts-lang-id)))
         root-node (obu/noget+ tree :?rootNode)]
     (or (when-let [{:keys [classification]
                     :as delete-ctx} (->delete-ctx root-node offset)]
           (condp contains? classification
             #{ts-rules/jsx-element} (delete-element delete-ctx)
             #{ts-rules/jsx-self-closing-element} (delete-self-closing-element delete-ctx)
             #{ts-rules/ts-computed-property-name
               ts-rules/ts-formal-parameters
               :subject} (delete-node delete-ctx)
             nil))
         (backspace src offset)))))

(comment
  (backward-delete "function foo () {}" 17)
  (backward-delete "function foo () {return <></>}" 26 nil :tsx)
  (backward-delete "function foo () {return <p></p>}" 27 nil :tsx)
  (backward-delete "function foo () {return <p></p>}" 28 nil :tsx)
  (backward-delete "function foo () {return <p id=\"\"></p>}" 34 nil :tsx)
  (backward-delete "function foo () {return <></>}" 29 nil :tsx)
  (backward-delete "function foo () {return <>\n\n</>}" 26 nil :tsx)
  (backward-delete "function foo () {return <p>\n\n</p>}" 31 nil :tsx)
  (backward-delete "/**/" 2)
  (backward-delete "function foo () {return <p/>}" 25 nil :tsx)
  (backward-delete "function foo () {return <p id=\"\"/>}" 33 nil :tsx)
  (backward-delete "{[a]: 1}" 2 nil :tsx)
  (backward-delete  "`${sa}`" 3 nil :tsx)
  (backward-delete  "interface K {}" 14 nil :tsx)
  (backward-delete "const foo = \"\";" 14 nil :tsx)
  (backward-delete "const foo = <></>;" 14 nil :tsx))