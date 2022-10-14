(ns owlbear.generators.tree.html
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require  [clojure.test.check.generators :as gen]
             [hiccups.runtime :as hiccupsrt]
             [oops.core :refer [oget]]
             [owlbear.generators.utilities :as obgu]
             [owlbear.parse :as obp]
             [miner.strgen :as sg]))

;;================================================================================
;; HTML raw
;;================================================================================

;; FIXME: This disj is because the Tree-sitter grammar has issues 
;; when children have the same tag name as their parent with certain tag names
(def ^:dynamic *container-tag-names*
  "Collection of tag names that must have an end tag"
  (disj hiccupsrt/container-tags "p" "dd" "li"))

(def ^:dynamic *html-element-container-tag-names*
  "Collection of tag names that can have HTML element children"
  (disj *container-tag-names* "script" "style"))

(def container-tag-name (gen/elements *container-tag-names*))

(def html-element-container-tag-name (gen/elements *html-element-container-tag-names*))

(def tag-name
  "Generates an HTML element tag name, can be made up"
  (gen/one-of [(gen/elements *html-element-container-tag-names*)
               obgu/string-alphanumeric-starts-with-alpha]))

(def html-text
  (obgu/string-from-generators
   [(gen/not-empty gen/string-alphanumeric)
    (sg/string-generator (re-pattern "\\w*[\n ]*\\w+"))
    (obgu/string-from-elements ["[" "]" "(" ")" "." "_" "-" "!"])]))

;;================================================================================
;; Hiccup
;;================================================================================

(defn hiccup-base
  "Generates a single, basic hiccup vector 
   with children generated from the given generator
   
   Accepts a map of options including 
   arguments for the child-vector generator, `vector-gen-args`, 
   and a generator for the tag names, `tag-name-gen`"
  ([] (hiccup-base html-text))
  ([child-g] (hiccup-base child-g {}))
  ([child-g {:keys [vector-gen-args tag-name-gen props-gen]
             :or {tag-name-gen tag-name
                  props-gen (gen/map obgu/string-alphanumeric-starts-with-alpha gen/string-alphanumeric)}}]
   {:pre [(or (gen/generator? child-g) (fn? child-g)) (gen/generator? tag-name-gen) (seqable? vector-gen-args)]}
   (gen/let [tag-name-and-props (gen/tuple tag-name-gen props-gen)
             children (apply gen/vector child-g vector-gen-args)]
     (into tag-name-and-props children))))

(defn hiccup
  "Generates hiccup vectors recursively
   
   Accepts a map of options including a generator for the hiccup children, `hiccup-child-gen` 
   
   The map of options, `opts`, will also be passed down to the underlying `hiccup-base` generator fn"
  ([] (hiccup {}))
  ([{:keys [hiccup-child-gen] :as opts}]
   (let [hiccup-child-gen* (or hiccup-child-gen (hiccup-base (hiccup-base html-text opts) opts))]
     (gen/let [result (gen/recursive-gen #(hiccup-base % opts) hiccup-child-gen*)]
       (cond-> result
         (string? result) (-> gen/return (hiccup-base (merge opts {:vector-gen-args [1]}))))))))


;;================================================================================
;; Tree-sitter
;;================================================================================

(defn hiccup->tree
  "Converts a hiccup vector to an HTML tree"
  [h]
  (when (not-empty h)
    (obp/src->tree! (html h) obp/html-lang-id)))

(defn tree
  "Generates an HTML tree 
   
   Optionally takes options for the hiccup generator responsible for determining the 
   HTML structure as `:hiccup-gen-opts`"
  ([] (tree {}))
  ([{:keys [hiccup-gen-opts]}]
   (gen/no-shrink (gen/fmap hiccup->tree (hiccup hiccup-gen-opts)))))

(defn element-node*
  ([] (element-node* {}))
  ([{:keys [hiccup-gen-opts]}]
   (gen/fmap (comp #(oget % :?rootNode.?children.?0)
                   #(obp/src->tree! % obp/html-lang-id)
                   #(html %))
             (hiccup-base html-text hiccup-gen-opts))))

(def element-node (element-node*))

(def comment-node
  (gen/fmap (comp #(oget % :?rootNode.?children.?0)
                  #(obp/src->tree! % obp/html-lang-id))
            ;; Advanced compilation seems to have a problem with regex literals
            (sg/string-generator (re-pattern "<!--([\\w\\d]+\\s*)+-->"))))

(defn text-node*
  ([] (text-node* nil))
  ([text-elements]
   {:pre [(seqable? text-elements)]}
   (gen/fmap (comp #(oget % :?rootNode.?children.?0)
                   #(obp/src->tree! % obp/html-lang-id))
             (if text-elements
               (obgu/string-from-elements text-elements)
               html-text))))

(def text-node (text-node*))

(def object-node
  (gen/one-of [element-node comment-node text-node]))

(def subject-node
  (gen/one-of
   [(element-node* {:hiccup-gen-opts {:tag-name-gen html-element-container-tag-name}})
    comment-node]))

(comment
  (gen/sample (hiccup-base (hiccup-base gen/string) {:vector-gen-args [2 4]})))