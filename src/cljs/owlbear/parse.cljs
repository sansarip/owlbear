(ns owlbear.parse
  "Tree-sitter parsing"
  (:require [oops.core :refer [ocall]]
            [web-tree-sitter :as Parser]))

(defonce languages (atom {}))

(def init-tree-sitter!
  (memoize #(ocall Parser :init)))

(defn load-language-wasm!
  "Given a language name (`language-name`), 
   and the path to a Tree-sitter-language wasm (`wasm-path`), 
   loads and registers the language object under
   the given language name"
  [language-name wasm-path]
  {:pre [(some? language-name) (string? wasm-path)]}
  (-> (init-tree-sitter!)
      (.then #(ocall Parser :Language.load wasm-path))
      (.then #(swap! languages assoc language-name %))))

(defn src->tree
  "Given a source string (`src`), 
   and a loaded language name (`language-name`), 
   parses `src` and returns a Tree-sitter tree"
  [src language-name]
  {:pre [(string? src) (some? language-name)]}
  (let [parser (Parser.)]
    (when-let [language (get @languages language-name)]
      (ocall parser :setLanguage language)
      (ocall parser :parse src))))

(comment
  ;; Examples
  (src->tree "<div></div>" :html)
  (src->tree "const a = 1;" :typescript)
  (src->tree "<></>" :tsx))