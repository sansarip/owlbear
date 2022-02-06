(ns owlbear.test-runner
  "Responsible for running tests"
  (:require [cljs.test :refer [run-tests]]
            [owlbear.html.parse :as obp-html]))

(defn run-tests* []
  ;; Add/remove test namespaces here 👇
  (run-tests 'owlbear.html.edit.barf-test
             'owlbear.html.edit.rules-test
             'owlbear.html.edit.slurp-test
             'owlbear.html.parse.rules-test
             'owlbear.parse.rules-test))

(defn ^:export init
  "Initializes Tree-sitter and then runs tests"
  []
  (.then (obp-html/init-tree-sitter! "resources/tree-sitter-html.wasm") run-tests*))