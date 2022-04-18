(ns owlbear.generators.tree.ts
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require [cljstache.core :as fmt]
            [clojure.string :as str]
            [clojure.test.check.generators :as gen]
            [oops.core :as oops]
            [owlbear.generators.tree :as obgt]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.generators.utilities :as obgu]
            [owlbear.parse :as obp]
            [owlbear.utilities :as obu]))

(def vector-gen-upper-limit 6)
(def ^:dynamic *expression* 
  "Returns a generates that generates TS expressions; 
   initialized later"
  (fn [] obgu/noop))
(def ^:dynamic *statement* 
  "Returns a generator that generates TS statements; 
   initialized later"
  (fn [] obgu/noop))
(def ^:dynamic *generator-context*
  "Context that many of the [often nested] generators in this namespace can draw from
   
   Options include the following: 
   * `:array/vector-gen-args`: vector-generator args for the statements in the pair-block generator
   * `:fn/return-statement-gen`: generator with which to generate the return statement of a function body 
   * `:hiccup/options`: options map passed to the hiccup generator function
   * `:pair-block/vector-gen-args`: vector-generator args for the statements in the pair-block generator 
   * `:statements/greenlist`: set of generators to greenlist from the statements generator
   * `:statements/redlist`: set of generators to redlist from the statements generator 
   * `:statement-block/statements-gen`: generator with which to generate the statements in the statement block
   * `:tree/transform-src`: function that, given the source generator in the tree generator, should return a generator that returns a new source string
   * `:type-alias/only-pair-blocks?`: determines whether type alias values should only be pair blocks
   * `:typescript/vector-gen-args`: vector-generator args for the statements in the typescript generator"
  {:array/vector-gen-args []
   :fn/return-statement-gen (obgu/with-function-gen (fn [] (*expression*)))
   :hiccup/options {}
   :pair-block/vector-gen-args [1 vector-gen-upper-limit]
   :statements/greenlist #{}
   :statements/redlist #{}
   :statement-block/statements-gen (gen/vector (obgu/with-function-gen (fn [] (*statement*))))
   :tree/transform-src identity
   :type-alias/only-pair-blocks? false
   :typescript/vector-gen-args [1 vector-gen-upper-limit]})

(defn with-children 
  "Given a function, `f`,  
   calls the function with the `*generator-context*` bindings set in a way 
   where all eligible parent nodes will have at least one child"
  [f]
  {:pre [(fn? f)]}
  (binding [*generator-context* (assoc *generator-context*
                                       :statement-block/statements-gen (gen/vector (*statement*) 1 vector-gen-upper-limit)
                                       :array/vector-gen-args [1 vector-gen-upper-limit]
                                       :hiccup/options {:vector-gen-args [1 vector-gen-upper-limit]})]
    (f)))

;;================================================================================
;; Expressions
;;================================================================================

(def primative
  "Generates representations of JS primatives"
  (gen/one-of [(gen/fmap str gen/small-integer)
               obgu/hex-string
               obgu/escaped-string
               (obgu/escape-string obgu/string-alphanumeric-starts-with-alpha)
               (gen/return "null")]))

(def identifier
  "Generates a JS identifier strings
   e.g. in `const a = 1`, `a` is the identifier"
  obgu/string-alphanumeric-starts-with-alpha)

(def operator 
  "Generates arithmetic operator strings/chars"
  (gen/elements [\+ \- \* \/ \< \> "==" "===" "<=" ">="]))

(def binary-expression
  "Generates a binary expression string
   e.g. `1 + 2 === 3`"
  (gen/fmap
   (fn s-exp->math [[operator* & operands]]
     (->> (interpose operator* operands)
          (map #(cond-> % (seq? %) s-exp->math))
          (str/join " ")))
   (obgu/ast operator primative)))

(def template-substitution
  "Generates template-string substitution expression strings 
   e.g. in ```1 + 1 = ${1 + 1}` ``, `${1 + 1}` is the 
   template substitution"
  (gen/let [default-value identifier
            expression (obgu/with-function-gen (fn [] (*expression*)) default-value)]
    (str "${" expression "}")))

(def template-string
  "Generates a template string"
  (gen/fmap #(str "`" (str/join " " %) "`")
            (gen/vector (gen/one-of [obgu/string-alphanumeric-starts-with-alpha
                                     template-substitution]))))

(def function-arguments
  "Generates parenthisized function arguments as a string 
   e.g. `(a, b, 1 + 1, [1, 2])`"
  (gen/fmap #(str "("
                  (str/join ", " %)
                  ")")
            (gen/vector (obgu/with-function-gen (fn [] (*expression*)) "\"\""))))

(def call-expression
  "Generates a call expression 
   e.g. `foo(a, b)`"
  (gen/let [fn-name obgu/string-alphanumeric-starts-with-alpha
            fn-args function-arguments]
    (str fn-name fn-args)))

(defn pair-block
  "Given a key generator, `key-gen`, and a value generator, `value-gen`, 
   returns a generator that generates a pair block string

   May contain incomplete pairs
   
   e.g. `{a: 1, b: 2, c:}`
   
   Optionally accepts an end-of-pair symbol, `end-of-pair-symbol`, to 
   use as a delimeter between pairs - defaults to a comma"
  ([key-gen value-gen]
   (pair-block key-gen value-gen \,))
  ([key-gen value-gen end-of-pair-symbol]
   (let [pairs-gen (apply gen/vector
                          (gen/tuple key-gen value-gen)
                          (:pair-block/vector-gen-args *generator-context*))]
     (gen/let [pairs pairs-gen
               solo-key (gen/fmap #(cond-> % (not-empty %) (str ":"))
                                  (gen/one-of [key-gen (gen/return "")]))]
       (let [pairs-str (-> pairs
                           (->> (map #(str/join ": " %))
                                (str/join (str end-of-pair-symbol " ")))
                           (as-> $ (cond-> $
                                     (not-empty $) (str end-of-pair-symbol))))]
         (str "{"
              pairs-str
              (cond->> solo-key
                (and (not-empty pairs-str) (not-empty solo-key)) (str " "))
              "}"))))))

(def basic-types
  "Generates a basic TS type string"
  (gen/elements ["any"
                 "bigint"
                 "boolean"
                 "number"
                 "null"
                 "object"
                 "string"
                 "undefined"
                 "unknown"
                 "void"]))

(defn array-literal*
  "Returns a generator that generates an array literal string 
   e.g. `[a, b, 1 + 1]`"
  []
  (let [expressions-gen (apply gen/vector
                               (obgu/with-function-gen (fn [] (*expression*)) "\"\"")
                               (:array/vector-gen-args *generator-context*))]
    (gen/let [expressions expressions-gen]
      ;; FIXME: This str/replace is necessary due to a bug in the TS parser where arrays can't have expressions that end with a negative number e.g. [1 < -2]
      (let [expression-str (str/join ", " (map #(str/replace % #"-(\d)$" "$1") expressions))]
        (str "[" expression-str "]")))))
(def array-literal (array-literal*))

(defn object-literal* 
  "Returns a generator that generates an object literal string 
   e.g. `{a: 1, b: 2}`"
  []
  (pair-block obgu/string-alphanumeric-starts-with-alpha
              (obgu/with-function-gen (fn [] (*expression*)) "\"\"")))
(def object-literal (object-literal*))

(set! *expression*
      (fn [] (gen/one-of [(array-literal*)
                          (object-literal*)
                          binary-expression
                          call-expression
                          identifier
                          primative
                          template-string])))

(defn jsx-expression* 
  "Returns a generator that generates a JSX expression 
   e.g. in `<div>1 + 1 = {1 + 1}</div>` the `{1 + 1}` is a JSX expression"
  []
  (gen/fmap #(str "{"
                  %
                  "}")
            (*expression*)))
(def jsx-expression (jsx-expression*))

;;================================================================================
;; Statements
;;================================================================================

(defn concat-semicolon
  "Given a generator, `g`, 
   returns a generator that concats a semicolon to the end 
   of the generated value from the given generator"
  [g]
  (gen/fmap #(str % \;) g))

(defn array-literal-statement*
  "Returns a generator that generates an array-literal statement string
   i.e an array-literal expression with a semicolon at the end" 
  []
  (concat-semicolon (array-literal*)))
(def array-literal-statement (array-literal-statement*))

(defn object-literal-statement*
  "Returns a generator that generates an object-literal statement string
   i.e an object-literal expression with a semicolon at the end"
  []
  (concat-semicolon (object-literal*)))
(def object-literal-statement (object-literal-statement*))

(def interface
  "Returns a generator that generates a TS interface declaration string 
   e.g. `interface Foo {a: string}`"
  (gen/let [i-name obgu/string-alphanumeric-starts-with-alpha
            pb (pair-block obgu/string-alphanumeric-starts-with-alpha
                           basic-types
                           \;)]
    (str "interface " i-name " " pb)))

(defn type-alias*
  "Returns a generator that generates a TS type-alias string 
   e.g. `type Foo = {a: string};`" 
  []
  (let [type-value-gen (gen/one-of
                        (cond-> [(pair-block obgu/string-alphanumeric-starts-with-alpha
                                             basic-types
                                             \;)]
                          (not (:type-alias/only-pair-blocks? *generator-context*))  (conj basic-types)))]
    (gen/let [type-name obgu/string-alphanumeric-starts-with-alpha
              type-value type-value-gen]
      (str "type " type-name " = " type-value ";"))))
(def type-alias (type-alias*))

(defn statement-block* 
  "Returns a generator that generates a statement-block string 
   e.g. `{const a = 1; 2 + 3 === 5;}`"
  []
  (gen/fmap #(str "{"
                  (str/join " " %)
                  "}")
            (:statement-block/statements-gen *generator-context*)))
(def statement-block (statement-block*))

(defn function-body* 
  "Returns a generator that generates a function-body string 
   e.g. `{const a = 1; return 1 + 1;}`"
  []
  (let [{:fn/keys [return-statement-gen]
         :statement-block/keys [statements-gen]
         :as ctx} *generator-context*
        body-contents-gen (gen/let [contents statements-gen
                                    return-value return-statement-gen]
                            (cond-> contents
                              (some? return-value) (conj (str "return " return-value ";"))))]
    (binding [*generator-context* (assoc ctx
                                         :statement-block/statements-gen body-contents-gen)]
      (statement-block*))))
(def function-body (function-body*))

(def function-params
  "Returns a generator that generates a function-params string 
   e.g. `(a, b, c)`"
  (gen/fmap #(str "("
                  (str/join ", " %)
                  ")")
            (gen/vector obgu/string-alphanumeric-starts-with-alpha)))

(defn arrow-function*
  "Returns a generator that generates an arrow-function string 
   e.g. `() => {return 1 + 1;}`"
  []
  (let [fn-body-gen (function-body*)]
    (gen/let [fn-params function-params
              fn-body fn-body-gen]
      (str fn-params " => " fn-body))))
(def arrow-function (arrow-function*))

(defn named-function-statement* 
  "Returns a generator that generates a named-function-statement string 
   e.g. `function foo () {return 1 + 1;}`"
  []
  ;; Generator defined outside of gen/let so bindings can take affect
  (let [fn-body-gen (function-body*)]
    (gen/let [fn-name obgu/string-alphanumeric-starts-with-alpha
              fn-params function-params
              fn-body fn-body-gen]
      (fmt/render "function {{&name}} {{&params}} {{&body}};"
                  {:name fn-name
                   :params fn-params
                   :body fn-body}))))
(def named-function-statement (named-function-statement*))

(defn jsx* 
  "Returns a generator that generates a JSX-expression-statement string 
   e.g. `<div>{1 + 1}</div>`"
  []
  (let [hiccup-opts (:hiccup/options *generator-context*)]
    (gen/fmap #(html %)
              (obgt-html/hiccup
               (merge {:hiccup-child-gen (obgt-html/hiccup-base
                                          (obgt-html/hiccup-base
                                           (gen/one-of [obgt-html/html-text
                                                        jsx-expression])
                                           hiccup-opts)
                                          hiccup-opts)}
                      hiccup-opts)))))
(def jsx (jsx*))

(defn jsx-component-function*
  "Returns a generator that generates a named-function-statement string 
   that contains a JSX-return statement"
  []
  (binding [*generator-context* (assoc *generator-context*
                                       :fn/return-statement-gen (jsx*))]
    (named-function-statement*)))
(def jsx-component-function (jsx-component-function*))

(defn declaration-statement* 
  "Returns a generator that generates a declaration-statement string 
   e.g. `const foo = 1;`"
  []
  (let [value-gen (gen/one-of [(*expression*)
                               (arrow-function*)
                               (jsx*)
                               identifier])]
    (gen/let [scope (gen/elements ["const" "let" "var"])
              vname identifier
              value value-gen]
      (fmt/render "{{&scope}} {{&name}} = {{&value}};"
                  {:scope scope
                   :name vname
                   :value value}))))
(def declaration-statement (declaration-statement*))

(defn expression-statement* 
  "Returns a generator that generates an expression-statement string 
   e.g. `1 + 1 === 2;`"
  []
  (concat-semicolon (*expression*)))
(def expression-statement (expression-statement*))

(defn arrow-function-statement* 
  "Returns a generator that generates an arrow-function-expression string 
   with a semicolon at the end of it"
  []
  (concat-semicolon (arrow-function*)))
(def arrow-function-statement (arrow-function-statement*))

(defn for-loop* 
  "Returns a generator that generates a for-loop string 
   e.g. `for (e of a) {1 + 1}`"
  []
  (let [loop-body-gen (statement-block*)]
    (gen/let [vname identifier
              iterable-name identifier
              loop-body loop-body-gen]
      (fmt/render "for ({{&vname}} of {{&iname}}) {{&body}}"
                  {:vname vname
                   :iname iterable-name
                   :body loop-body}))))
(def for-loop (for-loop*))

(defn while-loop* 
  "Returns a generator that generates a while-loop string 
   e.g. `while (true) {1 + 1}`"
  []
  (let [loop-body-gen (statement-block*)]
    (gen/let [condition (gen/one-of [gen/boolean binary-expression])
              loop-body loop-body-gen]
      (fmt/render "while ({{&condition}}) {{&body}}"
                  {:condition condition
                   :body loop-body}))))
(def while-loop (while-loop*))

(def import-statement
  "Generates an import statement 
   e.g. `import a from \"foo/a\"`"
  (gen/let [vname obgu/string-alphanumeric-starts-with-alpha
            module-dir (gen/vector obgu/string-alphanumeric-starts-with-alpha)]
    (fmt/render "import {{&name}} from \"{{&module}}\";"
                {:name vname
                 :module (str/join "/" (conj module-dir vname))})))

(defn export-statement* 
  "Returns a generator that generates an export statement 
   e.g. `export const a = 1 + 1;`"
  []
  (gen/fmap #(str "export " %) (declaration-statement*)))
(def export-statement (export-statement*))

(set! *statement*
      (fn []
        (let [{:statements/keys [greenlist redlist]} *generator-context*
              statement-generators (->> [arrow-function-statement*
                                         declaration-statement*
                                         import-statement
                                         interface
                                         export-statement*
                                         expression-statement*
                                         for-loop*
                                         jsx-component-function*
                                         named-function-statement*
                                         type-alias
                                         while-loop*]
                                        (filter (or (not-empty greenlist) identity))
                                        (remove redlist)
                                        (map obu/call-fn))]
          (gen/one-of statement-generators))))

;;================================================================================
;; Program
;;================================================================================

(defn typescript*
  "Returns a generator that generates a syntactically correct TSX program string"
  []
  (let [{:typescript/keys [vector-gen-args]} *generator-context*]
    (gen/fmap (partial str/join "\n") (apply gen/vector (*statement*) vector-gen-args))))
(def typescript (typescript*))

;;================================================================================
;; Tree-sitter
;;================================================================================

(defn jsx-element-node*
  "Returns a generator that generates a JSX element as a string"
  []
  (obgt/src-gen->node-gen (jsx*) {:tree->node #(obu/noget+ % :?rootNode.?children.?0.?children.?0)}))
(def jsx-element-node (jsx-element-node*))

(defn subject*
  "Returns a generator that generates a TSX subject as a string"
  []
  (gen/one-of [(array-literal-statement*)
               (arrow-function-statement*)
               (jsx-component-function*)
               (for-loop*)
               (named-function-statement*)
               (object-literal-statement*)
               (statement-block*)
               (while-loop*)
               interface
               type-alias]))
(def subject (subject*))

(defn object* 
  "Returns a generator of TSX objects, 
   generated as source code"
  []
  (*statement*))
(def object (object*))

(defn t-subject*
  "A T-subject is a a subject that has at least one child object 
   and sibling objects on both sides"
  []
  (let [subject-gen (with-children subject*)]
    (gen/fmap #(str/join "\n" %)
              (gen/tuple (object*) subject-gen (object*)))))
(def t-subject (t-subject*))

(defn with-t-subject 
  "Given a source generator, `src-gen`, 
   returns a generator that generates a source string with at least 
   one T-subject"
  [src-gen]
  {:pre [(gen/generator? src-gen)]}
  (let [t-subject-gen (t-subject*)]
    (gen/let [src src-gen
              t-subject-src t-subject-gen]
      (str src "\n" t-subject-src))))

(defn subject-node*
  "Returns a generator that generates a subject node"
  []
  (gen/one-of [(jsx-element-node*)
               (obgt/src-gen->node-gen (array-literal*) {:tree->node #(obu/noget+ % :?rootNode.?children.?0.?children.?0)})
               (obgt/src-gen->node-gen (object-literal*) {:tree->node #(obu/noget+ % :?rootNode.?children.?0.?children.?0)})
               (obgt/src-gen->node-gen (statement-block*))
               (obgt/src-gen->node-gen interface {:tree->node #(oops/ocall+ % :?rootNode.?children.?0.?childForFieldName "body")})
               (binding [*generator-context* (assoc *generator-context* :type-alias/only-pair-blocks? true)]
                 (obgt/src-gen->node-gen (type-alias*) {:tree->node #(oops/ocall+ % :?rootNode.?children.?0.?childForFieldName "value")}))]))
(def subject-node (subject-node*))

(defn object-node* 
  "Returns a generator that generates an object node"
  []
  (obgt/src-gen->node-gen (*expression*)))
(def object-node (object-node*))

(defn tree*
  "Returns a generator that generates a Tree-sitter, TSX tree"
  []
  (->> (typescript*)
       ((:tree/transform-src *generator-context*))
       (gen/fmap #(obp/src->tree % :tsx))))
(def tree (tree*))

(defn tree-with-t-subject*
  "Returns a generator that generates a Tree-sitter, TSX tree that has 
   at least one T-subject"
  []
  (binding [*generator-context* (assoc *generator-context*
                                       :tree/transform-src with-t-subject)]
    (tree*)))
(def tree-with-t-subject (tree-with-t-subject*))
;
;;================================================================================
;; Playground 
;;================================================================================

(comment
  ;; Using the *generator-context* to override statement generation in statement blocks
  (let [updated-context (assoc *generator-context*
                               :statement-block/statements-gen (gen/vector gen/boolean))
        statement-block (binding [*generator-context* updated-context]
                          (statement-block*))]
    (gen/sample statement-block))
  (let [updated-context (assoc *generator-context*
                               :statement-block/statements-gen (gen/vector gen/boolean)
                               :fn/return-statement-gen gen/boolean)
        named-function-statement (binding [*generator-context* updated-context]
                                   (named-function-statement*))]
    (gen/sample named-function-statement))
  (let [updated-context (assoc *generator-context*
                               :statement-block/statements-gen (gen/vector gen/boolean)
                               :fn/return-statement-gen gen/boolean)
        statement (binding [*generator-context* updated-context]
                    (*statement*))]
    (gen/sample statement))

  ;; Using greenlist/redlist to filter statement generators
  (let [updated-context (assoc *generator-context*
                               :statements/greenlist #{named-function-statement*})
        statement (binding [*generator-context* updated-context]
                    (*statement*))]
    (gen/sample statement))
  (let [updated-context (assoc *generator-context*
                               :statements/redlist #{jsx-component-function*
                                                     import-statement})
        statement (binding [*generator-context* updated-context]
                    (*statement*))]
    (gen/sample statement))

  ;; Generating JSX with at least 2 child nodes
  (let [updated-context (assoc *generator-context*
                               :hiccup/vector-gen-args [2])
        jsx (binding [*generator-context* updated-context]
              (jsx*))]
    (gen/sample jsx))

  ;; with t-subject
  (gen/sample (with-t-subject (typescript*)))

  ;; with > 0 children
  (gen/sample (with-children jsx*))
  (map #(obu/noget+ % :?text) (gen/sample (with-children subject-node*))))