(ns owlbear.ts.edit.splice
  (:require [owlbear.parse :as obp]
            [owlbear.ts.edit.delete :as ts-delete]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :as obu]
            [owlbear.ts.edit.clean :as ts-clean]))

(defn splice-node
  "Given a context, `ctx` containing
   a current node, `current-node`, 
   returns an updated context with the current node's
   contents, other than it's boundary nodes/container node, replacing itself
   
   These changes are reflected in the src of the `ctx`
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{node :current-node
    :keys [src offset edit-history]
    :or {edit-history []}
    :as ctx}]
  (let [{:keys [boundary-nodes start-nodes end-nodes]} (ts-delete/boundary-nodes node)
        boundary-node? (fn [node] (some #(.equals ^js % node) boundary-nodes))
        container-node (ts-rules/subject-container-node node)
        container-start (obu/update-offset
                         (obu/noget+ container-node :?startIndex)
                         edit-history)
        container-end (obu/update-offset
                       (obu/noget+ container-node :?endIndex)
                       edit-history)
        content-start (obu/update-offset
                       (obu/noget+ (last start-nodes) :?endIndex)
                       edit-history)
        content-end (obu/update-offset
                     (obu/noget+ (first end-nodes) :?startIndex)
                     edit-history)
        content (subs src
                      (obu/update-offset content-start edit-history)
                      (obu/update-offset content-end edit-history))
        edit-history* (-> edit-history
                          (conj {:type :delete
                                 :text (subs src container-start content-start)
                                 :offset container-start})
                          (as-> $ (conj $ {:type :delete
                                           :text (subs src content-end container-end)
                                           :offset (obu/update-offset content-end $)})))
        spliced-children (remove boundary-node? (obu/noget+ node :?children))]
    (assoc ctx
           :children spliced-children
           :edit-history edit-history*
           :content-start (obu/update-offset content-start edit-history*)
           :content-end (obu/update-offset content-end edit-history*)
           :offset (obu/update-offset offset edit-history*)
           :src (-> src
                    (obu/str-remove container-start container-end)
                    (obu/str-insert content container-start)))))

(defn clean
  "Given a context returned from `splice-node`, 
   returns an updated context with the certain 
   character sequences escaped/unescaped properly 
   e.g. comments"
  [{children :children
    :keys [current-node]
    :as ctx}]
  (letfn [(reduce-children [ctx cleanup-fn]
            (reduce (fn [ctx* child-node]
                      (let [child-ctx (assoc ctx*
                                             :child-node child-node
                                             :target-node child-node)]
                        (cleanup-fn child-ctx)))
                    ctx
                    children))]
    (-> (assoc ctx
               :ancestor-node current-node)
        (reduce-children ts-clean/escape-template-string)
        (reduce-children ts-clean/unescape-comments)
        (reduce-children ts-clean/unescape-escape-sequence))))

(defn splice
  "Given a `src` string and a character `offset`, 
   returns a map containing a new `src` string with 
   the splice operation applied at the offset and a 
   new `offset` containing where the cursor position 
   should be after the splice
   
   Accepts an optional third `tree-id` argument which 
   specifies the ID of an existing Tree-sitter tree that 
   should be used

   Accepts an optional fourth `tsx?` argument which 
   specifies if the `src` should be parsed as TSX
   
   e.g.
   ```tsx
   <📍><div></div></>
   =>
   <div></div>
   ```"
  ([src offset]
   (splice src offset nil))
  ([src offset tree-id]
   (splice src offset tree-id false))
  ([src offset tree-id tsx?]
   {:pre [(<= 0 offset)]}
   (let [tree (or (obp/get-tree tree-id)
                  (obp/src->tree! src (if tsx? obp/tsx-lang-id obp/ts-lang-id)))
         root-node (obu/noget+ tree :?rootNode)]
     (when-let [current-node (->> offset
                                  (ts-rules/node->current-subject-nodes root-node)
                                  (filter (complement ts-rules/empty-node?))
                                  last)]
       (-> {:src src
            :offset offset
            :current-node current-node}
           splice-node
           clean
           (select-keys [:src :offset]))))))

(comment
  (splice "const foo = <><div></div></>" 13 nil :tsx)
  (splice "const foo = <><div></div></>" 15 nil :tsx)
  (splice "const foo = [[1, 2, 3]]" 12 nil :tsx)
  (splice "const foo;" 0 nil :tsx)
  (splice "const foo = ''" 13 nil :tsx)
  (splice "<>\n  <h1>Hello, World!</h1>\n</>" 16 nil :tsx)
  (splice "{\n  a()\n}" 4 nil :tsx)
  (splice "foo(a, 1, b);" 10 nil :tsx)
  (splice "const foo = <><div></div></>" 27 nil :tsx))