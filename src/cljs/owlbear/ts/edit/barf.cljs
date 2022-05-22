(ns owlbear.ts.edit.barf
  (:require [clojure.string :as str]
            [owlbear.parse :as obp]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.parse.rules :as ob-ts-rules]
            [owlbear.utilities :as obu]))

(defn node->forward-barf-subjects
  "Given a `node`, 
   returns all nodes within the node, including the given node itself, 
   that are forward-barf subjects"
  [node]
  (filter (comp not-empty
                #(filter ob-ts-rules/object-node %)
                #(when % (obu/noget+ % :?children))
                ob-ts-rules/subject-node)
          (obpr/flatten-children node)))

(defn move-end-nodes
  "Given a context, `ctx`, a current node, `current-node`, 
   and a child node, `child-node`, 
   returns an updated context with the end node(s) of the 
   given current node moved to the start of the given child
   node; these changes are reflected in the src of the `ctx`
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [src offset edit-history] :or {edit-history []} :as ctx} current-node child-node]
  (let [current-end-nodes (reverse (ob-ts-rules/end-nodes current-node))
        current-end-node-text (->> current-end-nodes
                                   (map #(obu/noget+ % :?text))
                                   str/join)
        current-end-node-start-index (-> current-end-nodes
                                         first
                                         (obu/noget+ :?startIndex)
                                         (obu/update-offset edit-history))
        current-end-node-end-index (-> current-end-nodes
                                       last
                                       (obu/noget+ :?endIndex)
                                       (obu/update-offset edit-history))
        end-node-insert-offset (obu/update-offset
                                (obu/noget+ child-node :?startIndex)
                                edit-history)
        end-node-removed (obu/str-remove src
                                         current-end-node-start-index
                                         current-end-node-end-index)]
    (cond-> (assoc ctx
                   :src (obu/str-insert end-node-removed
                                        current-end-node-text
                                        end-node-insert-offset)
                   :edit-history (conj edit-history
                                       {:type :delete
                                        :offset current-end-node-start-index
                                        :text current-end-node-text
                                        :src src}
                                       {:type :insert
                                        :offset end-node-insert-offset
                                        :text current-end-node-text
                                        :src end-node-removed}))
      (<= current-end-node-start-index
          offset
          (dec current-end-node-end-index)) (assoc :offset (+ (- offset current-end-node-start-index)
                                                              end-node-insert-offset)))))

(defn forward-barf
  "Given a `src` string and character `offset`, 
   returns a map containing a new `src` string 
   with the forward barf operation applied at the offset 
   and a new `offset` containing where the cursor position 
   should be after the barf

   Accepts an optional third `tsx?` argument which specifies 
   if the `src` should be parsed as TSX
  
   e.g.
   ```tsx
   <><div>📍Hello, World!</div></>
   =>
   <><div>📍</div>Hello, World!</>
   ```"
  ([src offset] (forward-barf src offset false))
  ([src offset tsx?]
   (when-let [{:keys [last-child-object-node
                      current-node]} (-> src
                                         (obp/src->tree (if tsx? obp/tsx-lang-id obp/ts-lang-id))
                                         (obu/noget+ :?rootNode)
                                         (ob-ts-rules/node->current-last-child-object-ctx offset))]

     (-> {:src src
          :offset offset}
         (move-end-nodes current-node last-child-object-node)))))

(comment
  ;; Examples
  (-> "<div><></></div>"
      (obp/src->tree obp/tsx-lang-id)
      (obu/noget+ :?rootNode)
      (ob-ts-rules/node->current-last-child-object-ctx 1))
  (let [src "<><div><input/></div></>"
        offset 0
        {:keys [last-child-object-node
                current-node]} (-> src
                                   (obp/src->tree obp/tsx-lang-id)
                                   (obu/noget+ :?rootNode)
                                   (ob-ts-rules/node->current-last-child-object-ctx offset))
        ctx {:src src
             :offset offset}]
    (move-end-nodes ctx current-node last-child-object-node)
    [(obu/noget+ current-node :?text)]))