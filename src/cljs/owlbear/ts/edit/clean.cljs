(ns owlbear.ts.edit.clean
  "Shared TypeScript text cleaning utilities, used after Owlbear-edit operations"
  (:require [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :as obu]))

(defn escaped-comment-backslash-offsets
  "Given a string, `text`, 
  returns the offsets of backslashes of escaped comments 
  as a vector 
  
  An important note is that nested escaped comment backslashes 
  will *not* be included in the result"
  [text]
  (loop [[{:keys [offset text]} :as delims] (obu/re-pos #"\\/\*|\*\\/" text)
         opening-delim-count 0
         offsets []]
    (cond
      (= text "*\\/") (if (zero? opening-delim-count)
                        (recur (rest delims)
                               opening-delim-count
                               offsets)
                        (let [match? (= 1 opening-delim-count)]
                          (recur (rest delims)
                                 (dec opening-delim-count)
                                 (cond-> offsets
                                   match? (conj (inc offset))))))
      (= text "\\/*") (recur (rest delims)
                             (inc opening-delim-count)
                             (cond-> offsets
                               (zero? opening-delim-count) (conj offset)))
      (empty? delims) offsets)))

(defn escape-offsets
  "Given a context map, `ctx`, and a seq of `offsets`, 
   returns the the context map with its src updated 
   with backslashes inserted at the given offsets
   
   Also updates the offset and edit-history of the 
   given context"
  [{og-offset :offset :as ctx} offsets]
  (reduce
   (fn [c insert-offset]
     (-> c
         (update :src obu/str-insert \\ insert-offset)
         (update :edit-history conj {:type :insert
                                     :text "\\"
                                     :offset insert-offset})
         (cond-> (>= og-offset insert-offset) (update :offset inc))))
   ctx
   offsets))

(defn unescape-offsets
  "Given a context map, `ctx`, and a seq of `offsets`, 
   returns the the context map with its src updated 
   with backslashes removed at the given offsets
   
   Also updates the offset and edit-history of the 
   given context"
  [{og-offset :offset :as ctx} offsets]
  (reduce
   (fn [c rm-offset]
     (-> c
         (update :src obu/str-remove rm-offset)
         (update :edit-history conj {:type :delete
                                     :text "\\"
                                     :offset rm-offset})
         (cond-> (>= og-offset rm-offset) (update :offset dec))))
   ctx
   offsets))

(defn unescape-escape-sequence
  "Given a context, `ctx`, containing
   a target node, `target-node`, 
   returns an updated context with 
   escape sequence backslash in the 
   target node removed from the src 
   -if applicable, else returns 
   the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [offset edit-history target-node src]
    :or {edit-history []}
    :as ctx}]
  (let [rm-offset (when (ts-rules/ts-escape-sequence-node target-node)
                    (obu/update-offset (obu/noget+ target-node :?startIndex)
                                       edit-history))]
    (if rm-offset
      (-> ctx
          (update :src obu/str-remove rm-offset)
          (update :edit-history conj {:type :delete
                                      :text "\\"
                                      :offset rm-offset
                                      :src src})
          (cond-> (>= offset rm-offset) (update :offset dec)))
      ctx)))

(defn unescape-comments
  "Given a context, `ctx`, containing
   an ancestor node, `ancestor-node`, 
   and a child node, `child-node`, 
   returns an updated context with 
   the escaped comment backslashes of 
   the child node removed 
   from the src -if applicable, 
   else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [edit-history child-node ancestor-node]
    :or {edit-history []}
    :as ctx}]
  (if-let [rm-offsets (when (ts-rules/ts-comment-block-node ancestor-node)
                        (some->> (obu/noget+ child-node :?text)
                                 escaped-comment-backslash-offsets
                                 not-empty
                                 (obu/dec-offsets (-> child-node
                                                      (obu/noget+ :?startIndex)
                                                      (obu/update-offset edit-history)))))]
    (unescape-offsets ctx rm-offsets)
    ctx))

(defn escape-template-string
  "Given a context, `ctx`, that contains 
   a ancestor node, `ancestor-node`, 
   and a child node, `child-node`, 
   returns an updated context with the 
   template strings of the child node 
   contained in template substitutions 
   escaped in the src 
   -if applicable, else returns the given `ctx`
   
   Also updates the offset and edit-history of the given context 
   if updates were made"
  [{:keys [edit-history ancestor-node child-node]
    :or {edit-history []}
    :as ctx}]
  (if-let [insert-offsets (and (= ts-rules/ts-template-substitution (obu/noget+ ancestor-node :?type))
                               (ts-rules/ts-template-string-node (obu/noget+ ancestor-node :?parent))
                               (let [child-start-offset (obu/noget+ child-node :?startIndex)]
                                 (some->> (obu/noget+ child-node :?text)
                                          (obu/re-pos #"(?<!\\)`")
                                          not-empty
                                          (map :offset)
                                          (remove (into #{}
                                                        (mapcat (juxt #(- (obu/noget+ % :?startIndex)
                                                                          child-start-offset)
                                                                      #(- (dec (obu/noget+ % :?endIndex))
                                                                          child-start-offset)))
                                                        (ts-rules/node->template-string-nodes-in-substitutions child-node)))
                                          (obu/inc-offsets (obu/update-offset child-start-offset edit-history)))))]
    (escape-offsets ctx insert-offsets)
    ctx))