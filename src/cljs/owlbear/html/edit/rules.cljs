(ns owlbear.html.edit.rules
  "HTML-editing rules"
  (:require [oops.core :refer [oget]]
            [owlbear.html.parse.rules :as obp-html-rules]
            [owlbear.html.parse.utilities :as obp-html-util]
            [owlbear.parse.rules :as obpr]))

(defn subject-node
  "Returns the given `node`
   if edit operations can be run from within the node 
   i.e. the node doing the slurping or barfing"
  [node]
  {:pre [(some? node)]}
  (condp = (oget node :?type)
    obp-html-rules/html-comment node
    obp-html-rules/html-element (when (not= (oget node :?children.?0.?type)
                                            obp-html-rules/html-self-closing-tag)
                                  node)
    nil))

(defn object-node
  "Returns the given `node` 
   if edit operations can be run against the node 
   i.e. the node being slurped or barfed"
  [node]
  (when node
    (when-let [node-type (oget node :?type)]
      (cond
        (contains? #{obp-html-rules/html-comment
                     obp-html-rules/html-element
                     obp-html-rules/html-script
                     obp-html-rules/html-style}
                   node-type) node
        (= obp-html-rules/html-text node-type) (when-not (obp-html-util/all-white-space-chars* node)
                                                 node)
        :else nil))))

(defn node->current-subject-nodes
  "Given a node and an offset, 
   returns a lazy seq of all the subject nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/flatten-children
          (->> (filter subject-node))
          (obpr/filter-current-nodes offset)))

(defn next-forward-object-node [node]
  (obpr/some-forward-sibling-node object-node node))

(defn node->current-forward-object-ctx
  "Given a `node` and character `offset`, 
   returns a map of the deepest node (containing the `offset`)
   with a forward sibling object context"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some->> (node->current-subject-nodes node offset)
           (keep (fn [current-node]
                   (when-let [forward-object-node (next-forward-object-node current-node)]
                     {:forward-object-node forward-object-node
                      :current-node current-node})))
           last))

(defn node->child-object-nodes [node]
  (when node
    (if (obp-html-rules/comment-node node)
      nil
      (filter object-node (oget node :?children)))))

(defn node->current-last-child-object-ctx
  "Given a `node` and character `offset`, 
   returns a map of the deepest context (containing the offset)
   with the last child object node"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some->> (node->current-subject-nodes node offset)
           (keep (fn [current-node]
                   (when-let [last-child-object-node (last (node->child-object-nodes current-node))]
                     {:last-child-object-node last-child-object-node
                      :current-node current-node})))
           last))