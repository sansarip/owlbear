(ns owlbear.edit
  "Public-facing JS API for Owlbear's paredit-like operations"
  (:require [cljs-bean.core :refer [->js]]
            [owlbear.html.edit.barf :as html-barf]
            [owlbear.html.edit.kill :as html-kill]
            [owlbear.html.edit.move :as html-move]
            [owlbear.html.edit.raise :as html-raise]
            [owlbear.html.edit.slurp :as html-slurp]
            [owlbear.ts.edit.barf :as ts-barf]
            [owlbear.ts.edit.kill :as ts-kill]
            [owlbear.ts.edit.move :as ts-move]
            [owlbear.ts.edit.raise :as ts-raise]
            [owlbear.ts.edit.slurp :as ts-slurp]
            [owlbear.utilities :as obu]))

(defn ->js* [m]
  (->js (update-keys m (comp obu/kabob->camel name))))

(def html-backward-move (comp ->js* html-move/backward-move))
(def html-forward-barf (comp ->js* html-barf/forward-barf))
(def html-forward-move (comp ->js* html-move/forward-move))
(def html-forward-slurp (comp ->js* html-slurp/forward-slurp))
(def html-kill (comp ->js* html-kill/kill))
(def html-raise (comp ->js* html-raise/raise))
(def ts-backward-move (comp ->js* ts-move/backward-move))
(def tsx-backward-move (comp ->js* #(ts-move/backward-move % %2 %3 :tsx)))
(def ts-forward-barf (comp ->js* ts-barf/forward-barf))
(def tsx-forward-barf (comp ->js* #(ts-barf/forward-barf % %2 %3 :tsx)))
(def ts-forward-move (comp ->js* ts-move/forward-move))
(def tsx-forward-move (comp ->js* #(ts-move/forward-move % %2 %3 :tsx)))
(def ts-forward-slurp (comp ->js* ts-slurp/forward-slurp))
(def tsx-forward-slurp (comp ->js* #(ts-slurp/forward-slurp % %2 %3 :tsx)))
(def ts-kill (comp ->js* ts-kill/kill))
(def tsx-kill (comp ->js* #(ts-kill/kill % %2 %3 :tsx)))
(def ts-raise (comp ->js* ts-raise/raise))
(def tsx-raise (comp ->js* #(ts-raise/raise % %2 %3 :tsx)))
