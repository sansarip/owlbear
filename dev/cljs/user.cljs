(ns cljs.user
  (:require [owlbear.parse :as obp]))

(defn load-wasms!
  "Returns a promise encapsulating the loading of 
   all necessary WASM resources
   
   Optionally accepts a collection of `wasms` e.g. 
   
   ```clojure
   [[:html \"path/to/html.wasm\"]]
   ```"
  ([] (load-wasms! [[:html "resources/tree-sitter-html.wasm"]
                    [:markdown "resources/tree-sitter-markdown.wasm"]
                    [:typescript "resources/tree-sitter-typescript.wasm"]
                    [:tsx "resources/tree-sitter-tsx.wasm"]]))
  ([wasms]
   (js/Promise.all (map #(apply obp/load-language-wasm! %) wasms))))

(println "Loading language WASMs...")
(.then (load-wasms!) #(println "Finished loading WASMs!"))
