(ns owlbear.parser.html.utilities
  (:require [oops.core :refer [ocall]]))

(defn all-white-space-chars? [ctx]
  (boolean (some-> ctx
                   (ocall :?getText)
                   (->> (re-matches #"\s+")))))

(defn not-all-white-space-chars [ctx]
  (when-not (all-white-space-chars? ctx)
    ctx))