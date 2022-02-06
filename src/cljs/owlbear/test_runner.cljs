(ns owlbear.test-runner
  (:require [cljs.test :refer [run-tests]]
            [owlbear.parse.html :as obp-html]))

(defn run-tests* []
  ;; Add/remove test namespaces here 👇
  (run-tests 'owlbear.html.edit.barf-test
             'owlbear.html.edit.rules-test
             'owlbear.html.edit.slurp-test
             'owlbear.html.parse.rules-test
             'owlbear.parse.rules-test))

(defn ^:export init []
  (.then (obp-html/init-tree-sitter!) run-tests*))