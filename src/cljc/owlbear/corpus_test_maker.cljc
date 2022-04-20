(ns owlbear.corpus-test-maker
  "The place where the CLJS tests for the project's corpus files are manufactured 🏭")

(def corpus-tests-by-language
  {"TypeScript" [{:file-path "src/cljs/owlbear/ts/edit/slurp_test.md"
                  :test-function-map {"Forward Slurp" 'owlbear.ts.edit.slurp/forward-slurp}}]})

{"TypeScript" [{:test-name "Forward Slurp"
                :sections [{:section-name "Simple HTML"
                            :tests [{:input "<div></div><h1></h1>"
                                     :expected "<div><h1></h1></div>"}]}]
                :test-function 'owlbear.ts.edit.slurp/forward-slurp}]}

#?(:clj (->> "resources/libjava-tree-sitter.dylib"
             (str (System/getProperty "user.dir") "/")
             System/load))
#?(:clj (println "LOADED BB!"))

(defmacro def-tests
  []
  `(resolve '~(get-in corpus-tests-by-language ["TypeScript" 0 :test-function-map "Forward Slurp"]))
  #_`(loop [rs# ~records]
       (when-let [~r (first (seq rs#))]
         (do (eval (list 'deftest ~'(gensym "test") ~@body))
             (recur (rest rs#))))))