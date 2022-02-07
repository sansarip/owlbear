(ns owlbear.html.utilities
  "Utility tooling around Tree-sitter HTML trees"
  (:require [oops.core :refer [oget]]))

(defn all-white-space-chars*
  "Given a `node`, 
   returns the `node` if the node contains only whitespace chars"
  [node]
  (when (some-> node
                (oget :?text)
                (->> (re-matches #"\s+")))
    node))