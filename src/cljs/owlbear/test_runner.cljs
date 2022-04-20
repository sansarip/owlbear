(ns owlbear.test-runner
  "Responsible for running tests"
  (:require [cljs.user :refer [load-wasms!]]
            [clojure.test :as test]
            [colors]
            [oops.core :refer [ocall oget]]
            [owlbear.ts.edit.slurp :as ob-ts-slurp]
            [owlbear.corpus-test-maker :refer-macros [def-tests]]))

(defn log-diff
  "Given two strings, `str-1` and `str-2`, 
   logs the commonalities/differences of `str-2` from `str-1` 
   
   White text indicates commonalities, 
   green text indicates missing but expected text, 
   and red text indicates added but unexpected text"
  [str-1 str-2]
  (let [diff #_{:clj-kondo/ignore [:unresolved-symbol]}
        (ocall Diff :diffChars str-1 str-2)]
    (if (> (oget diff :length) 1)
      ;; Log differences and return false
      (do (some-> diff
                  (ocall :map
                         (fn [part]
                           (let [color-code (cond (oget part :?added) "\u001b[31m"
                                                  (oget part :?removed) "\u001b[32m"
                                                  :else "\u001b[37m")
                                 reset-color-code "\u001b[0m"]
                             (-> part
                                 (oget :?value)
                                 (as-> $ (str color-code $ reset-color-code))))))
                  (ocall :join "")
                  js/console.log)
          false)
      true)))

(defn run-tests* []
  (def-tests)
  ;; Add/remove test namespaces here 👇
  #_(run-tests 'owlbear.html.edit.barf-test
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