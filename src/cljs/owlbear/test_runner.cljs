(ns owlbear.test-runner
  "Responsible for running tests"
  (:require [cljs.test :refer [run-tests]]
            [cljs.user :refer [load-wasms!]]))

(defn run-tests* []
  ;; Add/remove test namespaces here 👇
  (run-tests 'owlbear.html.edit.barf-test
             'owlbear.html.edit.kill-test
             'owlbear.html.edit.raise-test
             'owlbear.html.edit.slurp-test
             'owlbear.html.parse.rules-test
             'owlbear.parse.rules-test
             'owlbear.ts.parse.rules-test))

(defn ^:export init
  "Initializes Tree-sitter and then runs tests"
  []
  (.then (load-wasms!) run-tests*))