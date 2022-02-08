(ns owlbear.edit
  (:require [owlbear.html.edit.barf :as obp-html-barf]
            [owlbear.html.edit.raise :as ob-html-raise]
            [owlbear.html.edit.slurp :as obp-html-slurp]))

(def html-forward-barf obp-html-barf/forward-barf)
(def html-forward-slurp obp-html-slurp/forward-slurp)
(def html-raise ob-html-raise/raise)