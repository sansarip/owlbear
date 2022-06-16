(ns owlbear.edit
  "Public-facing JS API for Owlbear's paredit-like operations"
  (:require [cljs-bean.core :refer [->js]]
            [owlbear.html.edit.barf :as html-barf]
            [owlbear.html.edit.kill :as html-kill]
            [owlbear.html.edit.raise :as html-raise]
            [owlbear.html.edit.slurp :as html-slurp]
            [owlbear.ts.edit.barf :as ts-barf]
            [owlbear.ts.edit.kill :as ts-kill]
            [owlbear.ts.edit.raise :as ts-raise]
            [owlbear.ts.edit.slurp :as ts-slurp]))

(def html-forward-barf (comp ->js html-barf/forward-barf))
(def html-forward-slurp (comp ->js html-slurp/forward-slurp))
(def html-kill (comp ->js html-kill/kill))
(def html-raise (comp ->js html-raise/raise))
(def ts-forward-barf (comp ->js ts-barf/forward-barf))
(def tsx-forward-barf (comp ->js #(ts-barf/forward-barf % %2 :tsx)))
(def ts-forward-slurp (comp ->js ts-slurp/forward-slurp))
(def tsx-forward-slurp (comp ->js #(ts-slurp/forward-slurp % %2 :tsx)))
(def ts-kill (comp ->js ts-kill/kill))
(def tsx-kill (comp ->js #(ts-kill/kill % %2 :tsx)))
(def ts-raise (comp ->js ts-raise/raise))
(def tsx-raise (comp ->js #(ts-raise/raise % %2 :tsx)))
