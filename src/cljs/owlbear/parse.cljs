#_{:clj-kondo/ignore [:unused-namespace]}
(ns owlbear.parse
  "Tree-sitter parsing"
  (:require [cljs-bean.core :refer [->js]]
            [oops.core :refer [ocall]]
            [web-tree-sitter :as Parser]))

(defonce languages (atom {}))

(def html-lang-id :html)
(def md-lang-id :markdown)
(def ts-lang-id :tsx)
(def tsx-lang-id :tsx)

(defonce trees (atom {}))

(defn get-tree [tree-id]
  (get @trees tree-id))

(defn trees->js []
  (->js @trees))

(defn languages->js []
  (->js @languages))

(defn set-tree! [tree-id tree]
  (swap! trees assoc tree-id tree))

(defn delete-tree! [tree-id]
  (swap! trees dissoc tree-id))

(defn edit-tree!
  "Given a `src` string, 
   tree ID, `tree-id`, 
   and a delta object, `delta`, 
   edits the specified tree with the given change, 
   parsing only the affected region
   
   For an example of the change object, 
   see https://github.com/tree-sitter/node-tree-sitter"  
  [src tree-id delta]
  {:pre [(string? src) (object? delta)]}
  (when-let [tree (get-tree tree-id)]
    (let [parser (Parser.)
          language (ocall tree :getLanguage)]
      (ocall parser :setLanguage language)
      (ocall tree :edit delta)
      (let [updated-tree (ocall parser :parse src tree)]
        (set-tree! tree-id updated-tree)
        updated-tree))))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(def init-tree-sitter!
  (memoize #(ocall Parser :init)))

(defn load-language-wasm!
  "Given a language name (`language-name`), 
   and the path to a Tree-sitter-language wasm (`wasm-path`), 
   loads and registers the language object under
   the given language name"
  [language-name wasm-path]
  {:pre [(some? language-name) (string? wasm-path)]}
  #_{:clj-kondo/ignore [:unresolved-symbol]}
  (-> (init-tree-sitter!)
      (.then #(ocall Parser :Language.load wasm-path))
      (.then #(swap! languages assoc language-name %))))

(defn src->tree!
  "Given a source string (`src`), 
   and a loaded language name (`language-name`), 
   parses `src` and returns a Tree-sitter tree
   
   As a side-effect, the resulting tree will be 
   stored in the `trees` atom under the given `tree-id`"
  ([src language-name]
   (src->tree! src language-name nil))
  ([src language-name tree-id]
   {:pre [(string? src) (some? language-name)]}
   (let [parser (Parser.)]
     (when-let [language (get @languages language-name)]
       (ocall parser :setLanguage language)
       (let [tree (ocall parser :parse src)]
         (set-tree! (or tree-id (random-uuid)) tree)
         tree)))))

(comment
  ;; Examples
  (src->tree! "<div></div>" html-lang-id)
  (src->tree! "const a = 1;" tsx-lang-id)
  (src->tree! "<></>" tsx-lang-id)

  ;; Map of registed languages
  @languages

  ;; Editing a tree
  (let [tree-id (random-uuid)]
    (src->tree! "let x = 1; console.log(x);" ts-lang-id tree-id)
    (-> (edit-tree! "const x = 1; console.log(x);" tree-id
                    #js {:startPosition #js {:row 0, :column 0},
                         :oldEndPosition #js {:row 0, :column 3},
                         :newEndPosition #js {:row 0, :column 5}})
        (oops.core/oget :rootNode.?text)))) 