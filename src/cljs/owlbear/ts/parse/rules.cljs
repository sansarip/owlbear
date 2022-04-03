(ns owlbear.ts.parse.rules
  (:require [oops.core :refer [oget]]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :as obu]))

(def jsx-closing-element "jsx_closing_element")
(def jsx-element "jsx_element")
(def jsx-expression "jsx_expression")
(def jsx-fragment "jsx_fragment")
(def jsx-opening-element "jsx_opening_element")
(def jsx-self-closing-element "jsx_self_closing_element")
(def jsx-text "jsx_text")
(def ts-array "array")
(def ts-assignment-expression "assignment_expression")
(def ts-binary-expression "binary_expression")
(def ts-class-body "class_body")
(def ts-comment "comment")
(def ts-comment-block "comment-block")
(def ts-expression-statement "expression_statement")
(def ts-identifier "identifier")
(def ts-labeled-statement "labeled_statement")
(def ts-lexical-declaration "lexical_declaration")
(def ts-number "number")
(def ts-object "object")
(def ts-parenthesized-expression "parenthesized_expression")
(def ts-regex "regex")
(def ts-spread-element "spread_element")
(def ts-string "string")
(def ts-statement-block "statement_block")
(def ts-structural-body "structural_body")
(def ts-syntax "syntax")
(def ts-template-string "template_string")
(def ts-template-substitution "template_substitution")
(def ts-update-expression "update_expression")

(defn subject-node
  "Returns the given `node`
   if edit operations can be run from within the node 
   i.e. the node doing the slurping or barfing"
  [node]
  (when (contains? #{ts-array
                     jsx-element
                     jsx-expression
                     jsx-fragment
                     ts-class-body
                     ts-comment-block
                     ts-statement-block
                     ts-structural-body
                     ts-string
                     ts-template-string
                     ts-template-substitution}
                   (obu/noget+ node :?type))
    node))

(defn object-node
  "Returns the given `node` 
   if edit operations can be run against the node 
   i.e. the node being slurped or barfed"
  [node]
  (or (subject-node node)
      (when (contains? #{jsx-self-closing-element
                         jsx-text
                         ts-assignment-expression
                         ts-binary-expression
                         ts-comment
                         ts-expression-statement
                         ts-identifier
                         ts-labeled-statement
                         ts-lexical-declaration
                         ts-number
                         ts-object
                         ts-parenthesized-expression
                         ts-regex
                         ts-spread-element
                         ts-string
                         ts-template-string
                         ts-update-expression}
                       (obu/noget+ node :?type))
        node)))

(defn node->current-subject-nodes
  "Given a `node` and an `offset`, 
   returns a lazy seq of all the subject nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/flatten-children
          (->> (filter subject-node))
          (obpr/filter-current-nodes offset)))

(defn node->current-object-nodes
  "Given a `node` and an `offset`, 
   returns a lazy seq of all the object nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/flatten-children
          (->> (filter object-node))
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
    (filter object-node (oget node :?children))))

(defn node->current-last-child-object-ctx
  "Given a `node` and character `offset`, 
   returns a map containing the deepest
   node containing the offset with object-node children (`current-node`) 
   and the last child-object node of that `current-node` (`last-child-object-node`)"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some->> (node->current-subject-nodes node offset)
           (keep (fn [current-node]
                   (when-let [last-child-object-node (last (node->child-object-nodes current-node))]
                     {:last-child-object-node last-child-object-node
                      :current-node current-node})))
           last))

(defn node->end-node
  "Given a node, 
   returns the end-tag node for that node if available"
  [node]
  (last (obu/noget+ node :?children)))

(defn node->start-node
  "Given a node, 
   returns the start-tag node for that node if available"
  [node]
  (first (obu/noget+ node :?children)))