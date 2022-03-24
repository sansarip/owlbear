(ns owlbear.test-runner
  "Responsible for running tests"
  (:require [cljs.test :refer [run-tests]]
            [owlbear.parse :as obp]))

(defn run-tests* []
  ;; Add/remove test namespaces here 👇
  (run-tests 'owlbear.html.edit.barf-test
             'owlbear.html.edit.kill-test
             'owlbear.html.edit.raise-test
             'owlbear.html.edit.slurp-test
             'owlbear.html.parse.rules-test
             'owlbear.parse.rules-test))

(defn ^:export init
  "Initializes Tree-sitter and then runs tests"
  []
  (let [wasms [[:html "resources/tree-sitter-html.wasm"]
               [:typescript "resources/tree-sitter-typescript.wasm"]
               [:tsx "resources/tree-sitter-tsx.wasm"]]]
    (.then (js/Promise.all (map #(apply obp/load-language-wasm! %) wasms))
           run-tests*)))