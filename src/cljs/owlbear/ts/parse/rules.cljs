(ns owlbear.ts.parse.rules
  (:require [clojure.string :as str]
            [owlbear.parse.rules :as obpr]))

(def jsx-attribute "jsx_attribute")
(def jsx-closing-element "jsx_closing_element")
(def jsx-element "jsx_element")
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
(def ts-export-statement "export_statement")
(def ts-expression-statement "expression_statement")
(def ts-for-statement "for_statement")
(def ts-for-in-statement "for_in_statement")
(def ts-formal-parameters "formal_parameters")
(def ts-function-declaration "function_declaration")
(def ts-function-type "function_type")
(def ts-generator-function-declaration "generator_function_declaration")
(def ts-identifier "identifier")
(def ts-if-statement "if_statement")
(def ts-import-statement "import_statement")
(def ts-incomplete-pair "incomplete_pair")
(def ts-incomplete-property-signature "incomplete_property_signature")
(def ts-interface-declaration "interface_declaration")
(def ts-intersection-type "intersection_type")
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
(def ts-switch-case "switch_case")
(def ts-switch-statement "switch_statement")
(def ts-syntax "syntax")
(def ts-template-fragment "template_fragment")
(def ts-template-string "template_string")
(def ts-template-substitution "template_substitution")
(def ts-type-alias-declaration "type_alias_declaration")
(def ts-type-annotation "type_annotation")
(def ts-type-identifier "type_identifier")
(def ts-update-expression "update_expression")
(def ts-union-type "union_type")
(def ts-variable-declarator "variable_declarator")
(def ts-variable-declaration "variable_declaration")
(def ts-while-statement "while_statement")
(def ts-yield-expression "yield_expression")

(defn ts-arguments-node
  "Given a `node`, 
   returns the `node` 
   if it is a arguments node"
  [^js node]
  (when (and node (= ts-arguments (.-type node)))
    node))

(defn ts-array-node
  "Given a `node`, 
   returns the `node` 
   if it is an array"
  [^js node]
  (when (and node (= ts-array (.-type node)))
    node))

(defn ts-await-expression-node
  "Given a `node`, 
   returns the `node` 
   if it is an await expression"
  [^js node]
  (when (and node (= ts-await-expression (.-type node)))
    node))

(defn ts-escape-sequence-node
  "Given a `node`, 
   returns the `node` 
   if it is an escape sequence"
  [^js node]
  (when (and node (= ts-escape-sequence (.-type node)))
    node))

(defn ts-member-expression-node
  "Given a `node`, 
   returns the `node` 
   if it is a member-expression node"
  [^js node]
  (when (and node (= ts-member-expression (.-type node)))
    node))

(defn ts-spread-element-node
  "Given a `node`, 
   returns the `node` 
   if it is a spread-element node"
  [^js node]
  (when (and node (= ts-spread-element (.-type node)))
    node))

(defn ts-object-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS object"
  [^js node]
  (when (and node (= ts-object (.-type node)))
    node))

(defn ts-object-type-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS object type"
  [^js node]
  (when (and node (= ts-object-type (.-type node)))
    node))

(defn ts-type-annotation-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS type annotation"
  [^js node]
  (when (and node (= ts-type-annotation (.-type node)))
    node))

(defn ts-computed-property-name-node
  "Given a `node`, 
   returns the `node` 
   if it is a computed property name"
  [^js node]
  (when (and node (= ts-computed-property-name (.-type node)))
    node))

(defn ts-literal-type-node
  "Given a `node`, 
   returns the `node` 
   if it is a literal type"
  [^js node]
  (when (and node (= ts-literal-type (.-type node)))
    node))

(defn ts-collection-node
  "Given a `node`, 
   returns the `node` 
   if it is a collection 
   i.e. array or TS object"
  [^js node]
  (when (and node (#{ts-array ts-object} (.-type node)))
    node))

(defn ts-statement-block-node
  "Given a `node`, 
   returns the `node` 
   if it is a statement block"
  [^js node]
  (when (and node (= ts-statement-block (.-type node)))
    node))

(defn syntax-str?
  "Given a string, `s`,  
   returns true if the string 
   consists entirely of TS syntax chars"
  [s]
  (re-matches #"[:;,/`'&\|\"\(\)\{\}\[\]\<\>\=]" (str s)))

(defn ts-syntax-node
  "Given a `node`, 
   returns the `node` 
   if it is a syntax node"
  [^js node]
  (when (and node (syntax-str? (.-type node)))
    node))

(defn jsx-element-node
  "given a `node`, 
   returns the `node` 
   if it is a jsx element node"
  [^js node]
  (when (and node (= jsx-element (.-type node)))
    node))

(defn jsx-attribute-node
  "given a `node`, 
   returns the `node` 
   if it is a jsx attribute node"
  [^js node]
  (when (and node (= jsx-attribute (.-type node)))
    node))

(defn jsx-closing-element-node
  "given a `node`, 
   returns the `node` 
   if it is an end tag node"
  [^js node]
  (when (and node (= jsx-closing-element (.-type node)))
    node))

(defn jsx-self-closing-element-node
  "given a `node`, 
   returns the `node` 
   if it is a self-closing tag node"
  [^js node]
  (when (and node (= jsx-self-closing-element (.-type node)))
    node))

(defn jsx-fragment-end-tag-node
  "given a `node`, 
   returns the `node` 
   if it is a fragment end tag node"
  [^js node]
  (when (and node (= jsx-fragment-end-tag (.-type node)))
    node))

(defn ts-comment-node
  "given a `node`, 
   returns the `node` 
   if it is a comment node"
  [^js node]
  (when (and node (= ts-comment (.-type node)))
    node))

(defn ts-comment-block-node
  "given a `node`, 
   returns the `node` 
   if it is a comment-block node"
  [^js node]
  (when (and node (= ts-comment-block (.-type node)))
    node))

(defn ts-comment-block-end-node
  "given a `node`, 
   returns the `node` 
   if it is a comment-block end node"
  [^js node]
  (when (and node (= ts-comment-block-end (.-type node)))
    node))

(defn ts-expression-statement-node
  "given a `node`, 
   returns the `node` 
   if it is an expression statement node"
  [^js node]
  (when (and node (= ts-expression-statement (.-type node)))
    node))

(defn ts-string-node
  "given a `node`, 
   returns the `node` 
   if it is a string node"
  [^js node]
  (when (and node (= ts-string (.-type node)))
    node))

(defn ts-template-string-node
  "given a `node`, 
   returns the `node` 
   if it is a template string node"
  [^js node]
  (when (and node (= ts-template-string (.-type node)))
    node))

(defn ts-identifier-node
  "given a `node`, 
   returns the `node` 
   if it is an identifier node"
  [^js node]
  (when (and node (= ts-identifier (.-type node)))
    node))

(defn ts-program-node
  "given a `node`, 
   returns the `node` 
   if it is a program node"
  [^js node]
  (when (and node (= ts-program (.-type node)))
    node))

(defn ts-property-signature-node
  "given a `node`, 
   returns the `node` 
   if it is a property signature node"
  [^js node]
  (when (and node (= ts-property-signature (.-type node)))
    node))

(defn ts-pair-node
  "given a `node`, 
   returns the `node` 
   if it is a pair node"
  [^js node]
  (when (and node (= ts-pair (.-type node)))
    node))

(defn ts-call-expression-node
  "given a `node`, 
   returns the `node` 
   if it is a call expression node"
  [^js node]
  (when (and node (= ts-call-expression (.-type node)))
    node))

(defn ts-required-parameter-node
  "given a `node`, 
   returns the `node` 
   if it is a required parameter node"
  [^js node]
  (when (and node (= ts-required-parameter (.-type node)))
    node))

(defn ts-formal-parameters-node
  "given a `node`, 
   returns the `node` 
   if it is a formal parameters node"
  [^js node]
  (when (and node (= ts-formal-parameters (.-type node)))
    node))

(let [green-list #{jsx-element
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
                   ts-template-substitution}]
  (defn subject-node
    "Returns the given `node` 
     if edit operations can be run from within the node 
     i.e. the node doing the slurping or barfing"
    [^js node]
    (when node
      (let [node-type (.-type node)
            parent (.-parent node)
            parent-type (when parent (.-type parent))]
        (cond
          ;; FIXME: predefined TS types have the same grammar type as other nodes 👎
          (= ts-predefined-type parent-type) nil
          (contains? green-list node-type) node
          :else nil)))))

;; *1
(let [green-list #{jsx-self-closing-element
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
                   ts-function-type
                   ts-generator-function-declaration
                   ts-if-statement
                   ts-import-statement
                   ts-incomplete-pair
                   ts-incomplete-property-signature
                   ts-interface-declaration
                   ts-intersection-type
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
                   ts-switch-case
                   ts-template-fragment
                   ts-template-string
                   ts-type-alias-declaration
                   ts-type-annotation
                   ts-type-identifier
                   ts-update-expression
                   ts-union-type
                   ts-variable-declaration
                   ts-while-statement
                   ts-yield-expression}
      redlist #{ts-literal-type ts-predefined-type}]
  (defn object-node
    "Returns the given `node` 
     if edit operations can be run against the node 
     i.e. the node being slurped or barfed"
    [^js node]
    (when node
      (or (let [node-type (.-type node)
                parent (.-parent node)
                parent-type (when parent (.-type parent))]
            (cond
              (contains? redlist parent-type)
              nil
              (contains? green-list node-type) node
              (= node-type ts-identifier) (when-not (contains? #{jsx-closing-element
                                                                 jsx-opening-element}
                                                               parent-type)
                                            node)
              (= node-type jsx-text) (when-not (obpr/all-white-space-chars node)
                                       node)
              :else nil))
          (subject-node node)))))

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
  [^js node]
  (when (some-> node
                .-type
                (str/ends-with? "expression"))
    node))

(defn statement-node
  "Returns the given `node` 
   if it is a statement node"
  [node]
  (when (some-> node
                .-type
                (str/ends-with? "statement"))
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
  [node-types ^js node]

  (when (and node
             (= ts-expression-statement (.-type node))
             (contains? node-types (some-> node .-children (aget 0) .-type)))
    node))

;; *1
(let [greenlist #{ts-class-declaration
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
                  ts-while-statement}]
  (defn top-level-node
    "Given a `node`, 
   returns the `node` 
   if it is a top-level node i.e.
   statement or declaration"
    [^js node]
    (when node
      (let [node-type (.-type node)]
        (when (or (contains? greenlist node-type)
                  (and (contains? #{ts-arrow-function
                                    ts-await-expression
                                    ts-call-expression
                                    ts-function-declaration}
                                  node-type)
                       (not= ts-expression-statement (some-> node .-parent .-type))
                       (not= ts-variable-declarator (some-> node .-parent .-type))))
          node)))))

(defn subject-container-node
  "Given a [subject] `node`, 
   returns a container node for the given `node` if applicable 
   else returns the node if it's a subject node  
   e.g. for `const a = () => {return \" \";};` the statement block 
   is the subject node and the lexical declaration, while not 
   a subject node itself, is the subject-container node
   
   Accepts an optional map containing:
   - `default`: the default value returned if a container is not found -defaults to the given `node`
   - `container-type-greenlist`: a set of container types to treat as top-level node types
   - `container-type-redlist`: a set of container types to ignore as top-level node types
   - `for-any-node?`: if true, the container node is returned for any node, otherwise only for subject nodes"
  ([node] (subject-container-node node {}))
  ([node {:keys [container-type-greenlist
                 container-type-redlist
                 default
                 for-any-node?]
          :or {default node}}]
   (letfn [(top-level-node* [^js node]
             (when node
               (or (when (contains? container-type-greenlist
                                    (.-type node))
                     node)
                   (top-level-node node))))]
     (when ((if for-any-node? identity subject-node) node)
       (or (-> node
               obpr/node->ancestors
               (->> (take-while (fn [^js ancestor]
                                  (let [root-node? (= (.-id node)
                                                      (some-> ancestor .-tree .-rootNode .-id))
                                        ancestor-type (.-type ancestor)]
                                    (or (contains? container-type-redlist ancestor-type)
                                        (and (not (contains? container-type-greenlist ancestor-type))
                                             (not (subject-node ancestor))
                                             (not (top-level-node* ancestor))
                                             (not root-node?)))))))
               (#(or (last %) node))
               .-parent
               top-level-node*)
           default)))))

(defn ts-comment-block-start-node [^js node]
  (when (and node (= ts-comment-block-start (.-type node)))
    node))

(defn jsx-opening-element-node [^js node]
  (when (and node (= jsx-opening-element (.-type node)))
    node))

(defn jsx-fragmant-start-tag-node [^js node]
  (when (and node (= jsx-fragment-start-tag (.-type node)))
    node))

(defn start-node [node]
  "Given a `node`, 
   returns the node if it is a start-indicating node 
   e.g. syntax nodes and opening-element nodes"
  (or (ts-comment-block-start-node node)
      (jsx-opening-element-node node)
      (jsx-fragmant-start-tag-node node)))

(defn start-nodes
  "Given a `node`, 
   returns a vector of start-indicating nodes 
   identified by the the given `pred` function
   
   The `pred` function defaults to identifying 
   start nodes (e.g. opening elements) and syntax nodes"
  ([^js node pred]
   (when-let [first-child (.-firstChild node)]
     (when-let [last-child-id (some-> node .-lastChild .-id)]
       (some-> first-child
               pred
               (->> (obpr/node->forward-sibling-nodes)
                    (take-while #(and (pred %)
                                      (not (= last-child-id (.-id ^js %))))))
               (conj first-child)
               reverse
               vec
               (as-> $
                     (if-let [container-node (subject-container-node node {:default nil})]
                       (into $ (start-nodes container-node pred))
                       $))))))
  ([^js node]
   (start-nodes
    node
    #(or (ts-syntax-node %) (start-node %)))))

(defn end-node
  "Given a `node`, 
   returns the node if it is an end-indicating node 
   e.g. syntax nodes and closing-element nodes"
  [^js node]
  (or (ts-comment-block-end-node node)
      (jsx-closing-element-node node)
      (jsx-fragment-end-tag-node node)))

(defn end-nodes
  "Given a `node`, 
   returns the end-indicating nodes in that node 
   e.g. syntax nodes and closing-element nodes"
  ([^js node pred]
   (when-let [last-child (.-lastChild node)]
     (when-let [first-child-id (some-> node .-firstChild .-id)]
       (some-> last-child
               pred
               (->> (obpr/node->backward-sibling-nodes)
                    (take-while #(and (pred %)
                                      (not (= first-child-id (.-id ^js %))))))
               (conj last-child)
               reverse
               vec
               (as-> $
                     (if-let [container-node (subject-container-node node {:default nil})]
                       (into $ (end-nodes container-node))
                       $))))))
  ([^js node]
   (end-nodes
    node
    #(or (ts-syntax-node %) (end-node %)))))

(defn ts-object-ends-with-pair
  "Given a `node`, 
   returns the `node` 
   if it is a TS object that ends with a pair"
  [^js node]
  (when (and (ts-object-node node)
             (-> (end-nodes node)
                 first
                 (some-> .-previousSibling .-type)
                 (= ts-pair)))
    node))

(defn complete-ts-object-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS object type that ends with a pair"
  [^js node]
  (when (and (ts-object-type-node node)
             (-> (end-nodes node)
                 first
                 (some-> .-previousSibling .-type)
                 (= ts-property-signature)))
    node))

(defn incomplete-ts-object-node
  "Given a `node`, 
   returns the `node` 
   if it is a TS object that ends with an incomplete pair"
  [^js node]
  (when (and node
             (= ts-object (.-type node))
             (-> (end-nodes node)
                 first
                 (some-> .-previousSibling .-type)
                 (= ts-incomplete-pair)))
    node))

(defn incomplete-ts-object-type-node
  "Given a `node`, 
   returns the `node` 
   if it is an object-type that ends with an 
   incomplete property signature"
  [^js node]
  (when (and node
             (= ts-object-type (.-type node))
             (-> (end-nodes node)
                 first
                 (some-> .-previousSibling .-type)
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

(defn node->forward-object-node
  "Given a `node`, 
   returns the first sibling node in the forward direction 
   that is an object node"
  [node]
  (obpr/some-forward-sibling-node object-node node))

(defn node->backward-object-node
  "Given a `node`, 
   returns the first sibling node in the forward direction 
   that is an object node"
  [node]
  (obpr/some-backward-sibling-node object-node node))

(defn node->current-forward-object-ctx
  "Given a `node` and character `offset`, 
   returns a map of the deepest node containing the `offset`, `:current-node`,
   and the next forward sibling object [optionally] at the subject-container level, `:forward-object-node`, 
   of the current node or nil if there is no current node or forward-object node
   
   Accepts an options map which can specify whether the operation should 
   allow object nodes as well, `:object-nodes?` (default false), 
   and whether the forward node should be found from the subject container of the current node, `:from-subject-container?`"
  ([node offset]
   (node->current-forward-object-ctx node offset {}))
  ([node offset {:keys [object-nodes? from-subject-container?]
                 :or {from-subject-container? true}}]
   {:pre [(<= 0 offset)]}
   (let [current-nodes-fn (if object-nodes?
                            node->current-object-nodes
                            node->current-subject-nodes)
         subject-container-fn (if from-subject-container?
                                subject-container-node
                                identity)]
     (some->> (current-nodes-fn node offset)
              (keep (fn [current-node]
                      (when-let [forward-object-node (node->forward-object-node (subject-container-fn current-node))]
                        {:forward-object-node forward-object-node
                         :current-node current-node})))
              last))))

(defn node->current-backward-object-ctx
  "Given a `node` and character `offset`, 
   returns a map of the deepest node containing the `offset`, `:current-node`,
   and the previous backward sibling object [optionally] at the subject-container level, `:backward-object-node`, 
   of the current node or nil if there is no current node or backward-object node
   
   Accepts an options map which can specify whether the operation should 
   allow object nodes as well, `:object-nodes?` (default false), 
   and whether the forward node should be found from the subject container of the current node, `:from-subject-container?`"
  ([node offset]
   (node->current-backward-object-ctx node offset {}))
  ([node offset {:keys [object-nodes? from-subject-container?]
                 :or {from-subject-container? true}}]
   {:pre [(<= 0 offset)]}
   (let [current-nodes-fn (if object-nodes?
                            node->current-object-nodes
                            node->current-subject-nodes)
         subject-container-fn (if from-subject-container?
                                subject-container-node
                                identity)]
     (some->> (current-nodes-fn node offset)
              (keep (fn [current-node]
                      (when-let [backward-object-node (node->backward-object-node (subject-container-fn current-node))]
                        {:backward-object-node backward-object-node
                         :current-node current-node})))
              last))))

(defn node->child-object-nodes [^js node]
  (when node
    (keep object-node (.-children node))))

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
                                 #(some-> ^js % .-parent .-type)
                                 ts-template-string-node)
                           node))

(defn content-range
  "Given a `node`, 
   returns the start and end indices
   of the content area within a node"
  [^js node]
  (let [node-start (.-startIndex node)
        node-end (.-endIndex node)
        start-node (last (start-nodes node))
        end-node (last (end-nodes node))]
    (if (and start-node end-node)
      [(.-endIndex start-node)
       (.-startIndex end-node)]
      [node-start node-end])))

(defn insignificant-str?
  "Given a string, `s`, 
   returns true if the string consists entirely 
   of insignificant characters like ws and syntax 😉"
  [s]
  (or (str/blank? s) (syntax-str? s)))

(defn insignicantly-in-node?
  "Returns true of the `offset` is 
   an insignificant area of the given `node`"
  [^js node offset]
  (let [node-start (.-startIndex node)
        [content-start content-end] (content-range node)]
    (and (<= content-start offset content-end)
         (boolean (some-> node
                          .-text
                          (get (- offset node-start))
                          insignificant-str?)))))

(defn default-value
  "Given a `node`,
   returns the default value of the parameter 
   if the `node` is a required-parameter node"
  [^js node]
  (when (ts-required-parameter-node node)
    (.childForFieldName node "value")))

(defn node->boundary-offsets [^js node]
  (if (or (jsx-fragment-end-tag-node node)
          (jsx-closing-element-node node))
    [(.-startIndex node) (inc (.-startIndex node)) (.-endIndex node)]
    (obpr/node->boundary-offsets node)))

(defn empty-node? [^js node]
  (every? (comp not
                #(or (object-node %)
                     (obpr/all-white-space-chars node)))
          (.-children node)))

;; Foot notes:
;; *1 | The peculiar function-surrounding-let-binding code was added to improve performance -tested against playground/jquery.js: