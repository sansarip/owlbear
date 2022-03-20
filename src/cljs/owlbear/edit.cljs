(ns owlbear.edit
  "Public-facing JS API for Owlbear's paredit-like operations"
  (:require [cljs-bean.core :refer [->js]]
            [owlbear.html.edit.barf :as obp-html-barf]
            [owlbear.html.edit.raise :as ob-html-raise]
            [owlbear.html.edit.slurp :as obp-html-slurp]))

(def html-forward-barf (comp ->js obp-html-barf/forward-barf))
(def html-forward-slurp (comp ->js obp-html-slurp/forward-slurp))
(def html-raise (comp ->js ob-html-raise/raise))