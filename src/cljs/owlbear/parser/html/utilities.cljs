(ns owlbear.parser.html.utilities
  "Utility tooling around Tree-sitter HTML trees"
  (:require [owlbear.parser.html.rules :as obp-html-rules]
            [oops.core :refer [ocall oget]]))

(defn all-white-space-chars? [ctx]
  (boolean (some-> ctx
                   (ocall :?getText)
                   (->> (re-matches #"\s+")))))

(defn not-all-white-space-chars [ctx]
  (when-not (all-white-space-chars? ctx)
    ctx))

(defn all-white-space-chars*
  "Given a `node`, 
   returns the `node` if the node contains only whitespace chars"
  [node]
  (when (some-> node
                (oget :?text)
                (->> (re-matches #"\s+")))
    node))