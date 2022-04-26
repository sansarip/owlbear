(ns owlbear.config
  (:require #?@(:clj [[clojure.edn :as edn]]
                :cljs [[fs]
                       [cljs.reader :as edn]])))

(def shadow-cljs-config
  (let [raw #?(:clj (slurp "shadow-cljs.edn")
               :cljs (fs/readFileSync "shadow-cljs.edn" "utf8"))]
    (edn/read-string raw)))

(def corpus-parser-script-path (-> shadow-cljs-config :builds :corpus :output-to))

(def source-paths (:source-paths shadow-cljs-config))