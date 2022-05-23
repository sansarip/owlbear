(ns owlbear.edit
  "Public-facing JS API for Owlbear's paredit-like operations"
  (:require [cljs-bean.core :refer [->js]]
            [owlbear.html.edit.barf :as obp-barf]
            [owlbear.html.edit.kill :as obp-kill]
            [owlbear.html.edit.raise :as ob-html-raise]
            [owlbear.html.edit.slurp :as obp-slurp]
            [owlbear.ts.edit.barf :as ob-ts-barf]
            [owlbear.ts.edit.slurp :as ob-ts-slurp]))

(def html-forward-barf (comp ->js obp-barf/forward-barf))
(def html-forward-slurp (comp ->js obp-slurp/forward-slurp))
(def html-kill (comp ->js obp-kill/kill))
(def html-raise (comp ->js ob-html-raise/raise))
(def ts-forward-barf (comp ->js ob-ts-barf/forward-barf))
(def tsx-forward-barf (comp ->js #(ob-ts-barf/forward-barf % %2 :tsx)))
(def ts-forward-slurp (comp ->js ob-ts-slurp/forward-slurp))
(def tsx-forward-slurp (comp ->js #(ob-ts-slurp/forward-slurp % %2 :tsx)))