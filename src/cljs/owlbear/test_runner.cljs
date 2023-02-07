(ns owlbear.test-runner
  "Responsible for running tests"
  (:require [cljs.user :refer [load-wasms!]]
            [clojure.string :as str]
            [clojure.test :refer [run-tests]]))

(defn success? [test-result-str]
  (str/includes? test-result-str "0 failures, 0 errors"))

(defn run-tests* []
  (println "🧪 Running tests...")
  (let [test-result-str (with-out-str
                          ;; Add/remove test namespaces here 👇
                          (run-tests 
                           'owlbear.corpus-test
                           'owlbear.html.edit.barf-test
                           'owlbear.html.edit.delete-test
                           'owlbear.html.edit.kill-test
                           'owlbear.html.edit.move-test
                           'owlbear.html.edit.raise-test
                           'owlbear.html.edit.slurp-test
                           'owlbear.html.parse.rules-test
                           'owlbear.parse.rules-test
                           'owlbear.ts.edit.barf-test
                           'owlbear.ts.edit.delete-test
                           'owlbear.ts.edit.kill-test
                           'owlbear.ts.edit.move-test
                           'owlbear.ts.edit.raise-test
                           'owlbear.ts.edit.slurp-test
                           'owlbear.ts.parse.rules-test))]
    (println test-result-str)
    (when-not (success? test-result-str)
      (js/process.exit 1))))

(defn ^:export init
  "Initializes Tree-sitter and then runs tests"
  []
  (.then (load-wasms!) run-tests*))