(ns owlbear.generators.tree.ts
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require [cljstache.core :as fmt]
            [clojure.string :as str]
            [clojure.test.check.generators :as gen]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.generators.utilities :as obgu]
            [owlbear.parse :as obp]))

(def ^:dynamic statement obgu/noop)

(defn concat-semicolon [g]
  (gen/fmap #(str % \;) g))

(def identifier obgu/string-alphanumeric-starts-with-alpha)

(def operator (gen/elements [\+ \- \* \/ \< \> "==" "===" "<=" ">="]))

(def binary-expression
  (gen/fmap
   (fn sexp->math [[operator* & operands]]
     (->> (interpose operator* operands)
          (map #(cond-> % (seq? %) sexp->math))
          (str/join " ")))
   (obgu/ast operator obgu/primative)))

(def expression
  (gen/one-of [binary-expression]))

(def statement-block
  (gen/fmap #(str "{"
                  (str/join " " %)
                  "}")
            (obgu/with-function-gen #(gen/vector statement))))

(def function-params
  (gen/fmap #(str "("
                  (str/join ", " %)
                  ")")
            (gen/vector obgu/string-alphanumeric-starts-with-alpha)))

(def arrow-function
  (gen/let [fn-params function-params
            fn-body statement-block]
    (str fn-params " => " fn-body)))

(def declaration
  (gen/let [scope (gen/elements ["const" "let" "var"])
            vname identifier
            value (gen/one-of [expression identifier arrow-function])]
    (fmt/render "{{&scope}} {{&name}} = {{&value}}"
                {:scope scope
                 :name vname
                 :value value})))

(def expression-statement
  (concat-semicolon expression))

(def arrow-function-statement
  (concat-semicolon arrow-function))

(def named-function-statement
  (gen/let [fn-name obgu/string-alphanumeric-starts-with-alpha
            fn-params function-params
            fn-body statement-block]
    (fmt/render "function {{&name}} {{&params}} {{&body}};"
                {:name fn-name
                 :params fn-params
                 :body fn-body})))

(def for-loop
  (gen/let [vname identifier
            iterable-name identifier
            loop-body statement-block]
    (fmt/render "for ({{&vname}} of {{&iname}}) {{&body}}"
                {:vname vname
                 :iname iterable-name
                 :body loop-body})))


(def while-loop
  (gen/let [condition (gen/one-of [gen/boolean binary-expression])
            loop-body statement-block]
    (fmt/render "while ({{&condition}}) {{&body}}"
                {:condition condition
                 :body loop-body})))

(def declaration-statement
  (concat-semicolon declaration))

(def import-statement
  (gen/let [vname obgu/string-alphanumeric-starts-with-alpha
            module-dir (gen/vector obgu/string-alphanumeric-starts-with-alpha)]
    (fmt/render "import {{&name}} from \"{{&module}}\";"
                {:name vname
                 :module (str/join "/" (conj module-dir vname))})))

(def export-statement
  (gen/fmap #(str "export " %) declaration-statement))

(set! statement
      (gen/one-of [arrow-function-statement
                   declaration-statement
                   import-statement
                   export-statement
                   expression-statement
                   for-loop
                   named-function-statement
                   while-loop]))
                  
(def jsx-expression
  (gen/fmap #(str "{"
                  (str/join " " %)
                  "}")
            (gen/vector (gen/one-of [declaration-statement expression-statement]))))
(def jsx
  (gen/fmap #(html %)
            (obgt-html/hiccup
             {:hiccup-child-gen (obgt-html/hiccup-base
                                 (obgt-html/hiccup-base
                                  (gen/one-of [obgt-html/html-text jsx-expression])))})))

(def typescript
  (gen/fmap (partial str/join "\n") (gen/vector statement 1 6)))

(def tree
  (gen/fmap #(obp/src->tree % :tsx) typescript))