(ns owlbear.ts.parse.rules
  (:require [clojure.string :as str]
            [oops.core :refer [oget ocall]]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :as obu]))

(def jsx-closing-element "jsx_closing_element")
(def jsx-element "jsx_element")
(def ts-export-statement "export_statement")
(def jsx-expression "jsx_expression")
(def jsx-fragment "jsx_fragment")
(def jsx-fragment-end-tag "jsx_fragment_end_tag")
(def jsx-fragment-start-tag "jsx_fragment_start_tag")
(def jsx-opening-element "jsx_opening_element")
(def jsx-self-closing-element "jsx_self_closing_element")
(def jsx-text "jsx_text")
(def ts-abstract-class-declaration "abstract_class_declaration")
(def ts-arguments "arguments")
(def ts-array "array")
(def ts-arrow-function "arrow_function")
(def ts-assignment-expression "assignment_expression")
(def ts-await-expression "await_expression")
(def ts-binary-expression "binary_expression")
(def ts-call-expression "call_expression")
(def ts-class-body "class_body")
(def ts-class-declaration "class_declaration")
(def ts-comment "comment")
(def ts-comment-block "comment_block")
(def ts-comment-block-content "comment_block_content")
(def ts-comment-block-end "comment_block_end")
(def ts-comment-block-start "comment_block_start")
(def ts-computed-property-name "computed_property_name")
(def ts-error "ERROR")
(def ts-escape-sequence "escape_sequence")
(def ts-expression-statement "expression_statement")
(def ts-for-statement "for_statement")
(def ts-for-in-statement "for_in_statement")
(def ts-formal-parameters "formal_parameters")
(def ts-generator-function-declaration "generator_function_declaration")
(def ts-function-declaration "function_declaration")
(def ts-identifier "identifier")
(def ts-if-statement "if_statement")
(def ts-import-statement "import_statement")
(def ts-incomplete-pair "incomplete_pair")
(def ts-incomplete-property-signature "incomplete_property_signature")
(def ts-interface-declaration "interface_declaration")
(def ts-labeled-statement "labeled_statement")
(def ts-lexical-declaration "lexical_declaration")
(def ts-literal-type "literal_type")
(def ts-member-expression "member_expression")
(def ts-new-expression "new_expression")
(def ts-null "null")
(def ts-number "number")
(def ts-object "object")
(def ts-object-type "object_type")
(def ts-pair "pair")
(def ts-parenthesized-expression "parenthesized_expression")
(def ts-predefined-type "predefined_type")
(def ts-program "program")
(def ts-property-identifier "property_identifier")
(def ts-property-signature "property_signature")
(def ts-public-field-definition "public_field_definition")
(def ts-regex "regex")
(def ts-required-parameter "required_parameter")
(def ts-return-statement "return_statement")
(def ts-shorthand-property-identifier "shorthand_property_identifier")
(def ts-spread-element "spread_element")
(def ts-statement-block "statement_block")
(def ts-string "string")
(def ts-string-fragment "string_fragment")
(def ts-structural-body "structural_body")
(def ts-switch-body "switch_body")
(def ts-switch-statement "switch_statement")
(def ts-syntax "syntax")
(def ts-template-fragment "template_fragment")
(def ts-template-string "template_string")
(def ts-template-substitution "template_substitution")
(def ts-type-alias-declaration "type_alias_declaration")
(def ts-type-annotation "type_annotation")
(def ts-type-identifier "type_identifier")
(def ts-update-expression "update_expression")
(def ts-variable-declarator "variable_declarator")
(def ts-variable-declaration "variable_declaration")
(def ts-while-statement "while_statement")
(def ts-yield-expression "yield_expression")

(defn ts-arguments-node
  "Given a `node`, 
   returns the `node` 
   if it is a arguments node"
  [node]
  (when (= ts-arguments (obu/noget+ node :?type))
    node))

(defn ts-array-node
  "Given a `node`, 
   returns the `node` 
   if it is an array"
  [node]
  (when (= ts-array (obu/noget+ node :?type))
    node))

(defn ts-await-expression-node
  "Given a `node`, 
   returns the `node` 
   if it is an await expression"
  [node]
  (when (= ts-await-expression (obu/noget+ node :?type))
    node))

(defn ts-escape-sequence-node
  "Given a `node`, 
   returns the `node` 
   if it is an escape sequence"
  [node]
  (when (= ts-escape-sequence (obu/noget+ node :?type))
    node))

(defn ts-member-expression-node
  "Given a `node`, 
   returns the `node` 
   if it is a member-expression node"
  [node]
  (when (= ts-member-expression (obu/noget+ node :?type))
    node))

(defn ts-spread-element-node
  "Given a `node`, 
   returns the `node` 
   if it is a spread-element node"
  [node]
  (when (= ts-spread-element (obu/noget+ node :?type))
    node))

(defn ts-object-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS object"
  [node]
  (when (= ts-object (obu/noget+ node :?type))
    node))

(defn ts-object-type-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS object type"
  [node]
  (when (= ts-object-type (obu/noget+ node :?type))
    node))

(defn ts-type-annotation-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS type annotation"
  [node]
  (when (= ts-type-annotation (obu/noget+ node :?type))
    node))

(defn ts-computed-property-name-node
  "Given a `node`, 
   returns the `node` 
   if it is a computed property name"
  [node]
  (when (= ts-computed-property-name (obu/noget+ node :?type))
    node))

(defn ts-collection-node
  "Given a `node`, 
   returns the `node` 
   if it is a collection 
   i.e. array or TS object"
  [node]
  (when (#{ts-array ts-object} (obu/noget+ node :?type))
    node))

(defn ts-statement-block-node
  "Given a `node`, 
   returns the `node` 
   if it is a statement block"
  [node]
  (when (= ts-statement-block (obu/noget+ node :?type))
    node)) 

(defn syntax-str? 
  "Given a string, `s`,  
   returns true if the string 
   consists entirely of TS syntax chars"
  [s]
  (re-matches #"[:;,/`'\"\(\)\{\}\[\]\<\>\=]" (str s)))

(defn ts-syntax-node
  "Given a `node`, 
   returns the `node` 
   if it is a syntax node"
  [node]
  (when (syntax-str? (obu/noget+ node :?type))
    node))

(defn jsx-closing-element-node
  "given a `node`, 
   returns the `node` 
   if it is an end tag node"
  [node]
  (when (= jsx-closing-element (obu/noget+ node :?type))
    node))

(defn jsx-fragment-end-tag-node
  "given a `node`, 
   returns the `node` 
   if it is a fragment end tag node"
  [node]
  (when (= jsx-fragment-end-tag (obu/noget+ node :?type))
    node))

(defn ts-comment-node
  "given a `node`, 
   returns the `node` 
   if it is a comment node"
  [node]
  (when (= ts-comment (obu/noget+ node :?type))
    node))

(defn ts-comment-block-node
  "given a `node`, 
   returns the `node` 
   if it is a comment-block node"
  [node]
  (when (= ts-comment-block (obu/noget+ node :?type))
    node))

(defn ts-comment-block-end-node
  "given a `node`, 
   returns the `node` 
   if it is a comment-block end node"
  [node]
  (when (= ts-comment-block-end (obu/noget+ node :?type))
    node))

(defn ts-expression-statement-node
  "given a `node`, 
   returns the `node` 
   if it is an expression statement node"
  [node]
  (when (= ts-expression-statement (obu/noget+ node :?type))
    node))

(defn ts-string-node
  "given a `node`, 
   returns the `node` 
   if it is a string node"
  [node]
  (when (= ts-string (obu/noget+ node :?type))
    node))

(defn ts-template-string-node
  "given a `node`, 
   returns the `node` 
   if it is a template string node"
  [node]
  (when (= ts-template-string (obu/noget+ node :?type))
    node))

(defn ts-identifier-node
  "given a `node`, 
   returns the `node` 
   if it is an identifier node"
  [node]
  (when (= ts-identifier (obu/noget+ node :?type))
    node))

(defn ts-program-node
  "given a `node`, 
   returns the `node` 
   if it is a program node"
  [node]
  (when (= ts-program (obu/noget+ node :?type))
    node))

(defn ts-property-signature-node
  "given a `node`, 
   returns the `node` 
   if it is a property signature node"
  [node]
  (when (= ts-property-signature (obu/noget+ node :?type))
    node))

(defn ts-pair-node
  "given a `node`, 
   returns the `node` 
   if it is a pair node"
  [node]
  (when (= ts-pair (obu/noget+ node :?type))
    node))

(defn ts-call-expression-node
  "given a `node`, 
   returns the `node` 
   if it is a call expression node"
  [node]
  (when (= ts-call-expression (obu/noget+ node :?type))
    node))

(defn subject-node
  "Returns the given `node`
   if edit operations can be run from within the node 
   i.e. the node doing the slurping or barfing"
  [node]
  (let [node-type (obu/noget+ node :?type)]
    (cond
      ;; FIXME: predefined TS types have the same grammar type as other nodes 👎
      (= ts-predefined-type (obu/noget+ node :?parent.?type)) nil
      (contains? #{jsx-element
                   jsx-expression
                   jsx-fragment
                   ts-arguments
                   ts-array
                   ts-class-body
                   ts-comment-block
                   ts-object
                   ts-object-type
                   ts-statement-block
                   ts-string
                   ts-structural-body
                   ts-switch-body
                   ts-template-string
                   ts-template-substitution}
                 node-type) node
      :else nil)))

(defn object-node
  "Returns the given `node` 
   if edit operations can be run against the node 
   i.e. the node being slurped or barfed"
  [node]
  (or (let [node-type (obu/noget+ node :?type)]
        (cond
          (contains? #{ts-literal-type ts-predefined-type} (obu/noget+ node :?parent.?type))
          nil
          (contains? #{jsx-self-closing-element
                       ts-abstract-class-declaration
                       ts-arrow-function
                       ts-assignment-expression
                       ts-await-expression
                       ts-binary-expression
                       ts-call-expression
                       ts-class-declaration
                       ts-comment
                       ts-comment-block-content
                       ts-computed-property-name
                       ts-error
                       ts-escape-sequence
                       ts-export-statement
                       ts-expression-statement
                       ts-for-statement
                       ts-for-in-statement
                       ts-formal-parameters
                       ts-function-declaration
                       ts-generator-function-declaration
                       ts-identifier
                       ts-if-statement
                       ts-import-statement
                       ts-incomplete-pair
                       ts-incomplete-property-signature
                       ts-interface-declaration
                       ts-labeled-statement
                       ts-lexical-declaration
                       ts-literal-type
                       ts-member-expression
                       ts-new-expression
                       ts-null
                       ts-number
                       ts-object
                       ts-pair
                       ts-parenthesized-expression
                       ts-predefined-type
                       ts-property-identifier
                       ts-property-signature
                       ts-public-field-definition
                       ts-regex
                       ts-required-parameter
                       ts-return-statement
                       ts-spread-element
                       ts-shorthand-property-identifier
                       ts-string
                       ts-string-fragment
                       ts-switch-statement
                       ts-template-fragment
                       ts-template-string
                       ts-type-alias-declaration
                       ts-type-annotation
                       ts-type-identifier
                       ts-update-expression
                       ts-variable-declaration
                       ts-while-statement
                       ts-yield-expression}
                     node-type) node
          (= node-type jsx-text) (when-not (obpr/all-white-space-chars node)
                                   node)
          :else nil))
      (subject-node node)))

(defn empty-ts-collection-node
  "Given a `node`, 
   returns the `node` 
   if it is an empty collection node"
  [node]
  (when (and (ts-collection-node node)
             (obpr/every-descendant? (complement object-node) node))
    node))

(defn not-empty-ts-collection-node
  "Given a `node`, 
   returns the `node` 
   if it is a not-empty collection node"
  [node]
  (when (and (ts-collection-node node)
             (not (empty-ts-collection-node node)))
    node))

(defn empty-ts-object-node
  "Given a `node`, 
   retuns the `node` 
   if it is an empty TS object node"
  [node]
  (when (and (ts-object-node node)
             (empty-ts-collection-node node))
    node))

(defn empty-ts-object-type-node
  "Given a `node`, 
   retuns the `node` 
   if it is an empty TS object node"
  [node]
  (when (and (ts-object-type-node node)
             (obpr/every-descendant? (complement object-node) node))
    node))

(defn not-empty-ts-array-node
  "Given a `node`, 
   retuns the `node` 
   if it is _not_ an empty array node"
  [node]
  (when (and (ts-array-node node)
             (not-empty-ts-collection-node node))
    node))

(defn empty-ts-arguments-node
  "Given a `node`, 
   returns the `node` 
   if it is an empty arguments node"
  [node]
  (when (and (ts-arguments-node node)
             (obpr/every-descendant? (complement object-node) node))
    node))

(defn expression-node 
  "Returns the given `node` 
   if it is an expression node"
  [node]
  (when (some-> (obu/noget+ node :?type)
                (str/ends-with? "expression"))
    node))

(defn not-empty-ts-arguments-node
  "Given a `node`, 
   retuns the `node` 
   if it is _not_ an empty arguments node"
  [node]
  (when (and (ts-arguments-node node)
             (not (empty-ts-arguments-node node)))
    node))

(defn expression-statement-of
  "Given a set of node types, `node-types`, 
   and an expression-node, `node`, 
   returns the given `node` if it is an 
   expression node of the given node types"
  [node-types node]
  (when (and (= ts-expression-statement (obu/noget+ node :?type))
             (contains? node-types (obu/noget+ node :?children.?0.?type)))
    node))

(defn top-level-node
  "Given a `node`, 
   returns the `node` 
   if it is a top-level node i.e.
   statement or declaration"
  [node]
  (let [node-type (obu/noget+ node :?type)]
    (when (or (contains? #{ts-class-declaration
                           ts-expression-statement
                           ts-for-statement
                           ts-for-in-statement
                           ts-function-declaration
                           ts-generator-function-declaration
                           ts-if-statement
                           ts-interface-declaration
                           ts-lexical-declaration
                           ts-member-expression
                           ts-spread-element
                           ts-switch-statement
                           ts-type-alias-declaration
                           ts-variable-declaration
                           ts-while-statement}
                         node-type)
              (and (contains? #{ts-await-expression
                                ts-call-expression
                                ts-arrow-function}
                              node-type)
                   (not= ts-expression-statement (obu/noget+ node :?parent.?type))
                   (not= ts-variable-declarator (obu/noget+ node :?parent.?type))))
      node)))

(defn subject-container-node
  "Given a [subject] `node`, 
   returns a container node for the given `node` if applicable 
   else returns the node if it's a subject node  
   e.g. for `const a = () => {return \" \";};` the statement block 
   is the subject node and the lexical declaration, while not 
   a subject node itself, is the subject-container node
   
   Accepts an optional argument, `default`, 
   that is returned if a container is not found - 
   defaults to the given `node`"
  ([node] (subject-container-node node {}))
  ([node {:keys [container-type-greenlist
                 container-type-redlist
                 default
                 for-any-node?]
          :or {default node}}]
   (letfn [(top-level-node* [node]
             (or (when (contains? container-type-greenlist
                                  (obu/noget+ node :?type))
                   node)
                 (top-level-node node)))]
     (when ((if for-any-node? identity subject-node) node)
       (or (-> node
               obpr/node->ancestors
               (->> (take-while (fn [ancestor]
                                  (let [root-node? (= (obu/noget+ ancestor :?id)
                                                      (obu/noget+ ancestor :?tree.?rootNode.?id))
                                        ancestor-type (obu/noget+ ancestor :?type)]
                                    (or (contains? container-type-redlist ancestor-type)
                                        (and (not (contains? container-type-greenlist ancestor-type))
                                             (not (subject-node ancestor))
                                             (not (top-level-node* ancestor))
                                             (not root-node?)))))))
               (#(or (last %) node))
               (obu/noget+ :?parent)
               top-level-node*)
           default)))))

(defn ts-comment-block-start-node [node]
  (when (= ts-comment-block-start (obu/noget+ node :?type))
    node))

(defn jsx-opening-element-node [node]
  (when (= jsx-opening-element (obu/noget+ node :?type))
    node))

(defn jsx-fragmant-start-tag-node [node]
  (when (= jsx-fragment-start-tag (obu/noget+ node :?type))
    node))

(defn start-nodes
  "Given a `node`, 
   returns the start-indicating nodes in that node 
   e.g. syntax nodes and opening-element nodes"
  [node]
  (when-let [first-child (obu/noget+ node :?firstChild)]
    (when-let [last-child-id (obu/noget+ node :?lastChild.?id)]
      (if-let [start-node (or (ts-comment-block-start-node first-child)
                              (jsx-opening-element-node first-child)
                              (jsx-fragmant-start-tag-node first-child))]
        [start-node]
        (some-> first-child
                ts-syntax-node
                (->> (obpr/node->forward-sibling-nodes)
                     (take-while #(and (ts-syntax-node %)
                                       (not (= last-child-id (obu/noget+ % :?id))))))
                (conj first-child)
                reverse
                vec
                (as-> $
                      (if-let [container-node (subject-container-node node {:default nil})]
                        (into $ (start-nodes container-node))
                        $)))))))

(defn end-nodes
  "Given a `node`, 
   returns the end-indicating nodes in that node 
   e.g. syntax nodes and closing-element nodes"
  [node]
  (when-let [last-child (obu/noget+ node :?lastChild)]
    (when-let [first-child-id (obu/noget+ node :?firstChild.?id)]
      (if-let [end-node (or (ts-comment-block-end-node last-child)
                            (jsx-closing-element-node last-child)
                            (jsx-fragment-end-tag-node last-child))]
        [end-node]
        (some-> last-child
                ts-syntax-node
                (->> (obpr/node->backward-sibling-nodes)
                     (take-while #(and (ts-syntax-node %)
                                       (not (= first-child-id (obu/noget+ % :?id))))))
                (conj last-child)
                reverse
                vec
                (as-> $
                      (if-let [container-node (subject-container-node node {:default nil})]
                        (into $ (end-nodes container-node))
                        $)))))))

(defn ts-object-ends-with-pair
  "Given a `node`, 
   returns the `node` 
   if it is a TS object that ends with a pair"
  [node]
  (when (and (ts-object-node node)
             (-> (end-nodes node)
                 first
                 (obu/noget+ :?previousSibling.?type)
                 (= ts-pair)))
    node))

(defn complete-ts-object-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS object type that ends with a pair"
  [node]
  (when (and (ts-object-type-node node)
             (-> (end-nodes node)
                 first
                 (obu/noget+ :?previousSibling.?type)
                 (= ts-property-signature)))
    node))

(defn incomplete-ts-object-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS object that ends with an incomplete pair"
  [node]
  (when (and (= ts-object (obu/noget+ node :?type))
             (-> (end-nodes node)
                 first
                 (obu/noget+ :?previousSibling.?type)
                 (= ts-incomplete-pair)))
    node))

(defn incomplete-ts-object-type-node
  "Given a `node`, 
   returns the `node` 
   if it is an object-type that ends with an 
   incomplete property signature"
  [node]
  (when (and (= ts-object-type (obu/noget+ node :?type))
             (-> (end-nodes node)
                 first
                 (obu/noget+ :?previousSibling.?type)
                 (= ts-incomplete-property-signature)))
    node))

(defn node->current-subject-nodes
  "Given a `node` and an `offset`, 
   returns a lazy seq of all the subject nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/node->descendants
          (->> (filter subject-node))
          (obpr/filter-current-nodes offset)))

(defn node->current-object-nodes
  "Given a `node` and an `offset`, 
   returns a lazy seq of all the object nodes containing that offset"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some-> node
          obpr/node->descendants
          (->> (filter object-node))
          (obpr/filter-current-nodes offset)))

(defn next-forward-object-node
  "Given a `node`, 
   returns the first sibling node in the forward direction 
   that is an object node"
  [node]
  (obpr/some-forward-sibling-node object-node node))

(defn node->current-forward-object-ctx
  "Given a `node` and character `offset`, 
   returns a map of the deepest node containing the `offset`, `:current-node`,
   and the next forward sibling object at the subject-container level, `:forward-object-node`, 
   of the current node or nil if there is no current node or forward-object node"
  [node offset]
  {:pre [(<= 0 offset)]}
  (some->> (node->current-subject-nodes node offset)
           (keep (fn [current-node]
                   (when-let [forward-object-node (next-forward-object-node (subject-container-node current-node))]
                     {:forward-object-node forward-object-node
                      :current-node current-node})))
           last))

(defn node->child-object-nodes [node]
  (when node
    (keep object-node (oget node :?children))))

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

(defn node->template-string-nodes-in-substitutions
  "Given a `node`, 
   returns all the child nodes (recursive) that are 
   template strings being evaluated in substitutions
   
   e.g.
   ```typescript
   // There is a template string being evaluated in a template substitution
   `${``}`

   // This, however, does not count
   `${() => ``}`
   ```"
  [node]
  (obpr/filter-descendants (comp #{ts-template-substitution}
                                 #(obu/noget+ % :?parent.?type)
                                 ts-template-string-node)
                           node))

(defn content-range
  "Given a `node`, 
   returns the start and end indices
   of the content area within a node"
  [node]
  (let [node-start (obu/noget+ node :?startIndex)
        node-end (obu/noget+ node :?endIndex)
        start-node (last (start-nodes node))
        end-node (last (end-nodes node))]
    (if (and start-node end-node)
      [(obu/noget+ start-node :?endIndex)
       (obu/noget+ end-node :?startIndex)]
      [node-start node-end])))

(defn insignificant-str?
  "Given a string, `s`, 
   returns true if the string consists entirely 
   of insignificant characters like ws and syntax 😉"
  [s]
  (or (str/blank? s) (syntax-str? s)))