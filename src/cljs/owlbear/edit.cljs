(ns owlbear.edit
  "Public-facing JS API for Owlbear's paredit-like operations"
  (:require [cljs-bean.core :refer [->js]]
            [owlbear.html.edit.barf :as html-barf]
            [owlbear.html.edit.delete :as html-delete]
            [owlbear.html.edit.kill :as html-kill]
            [owlbear.html.edit.move :as html-move]
            [owlbear.html.edit.raise :as html-raise]
            [owlbear.html.edit.slurp :as html-slurp]
            [owlbear.html.edit.splice :as html-splice]
            [owlbear.ts.edit.barf :as ts-barf]
            [owlbear.ts.edit.delete :as ts-delete]
            [owlbear.ts.edit.kill :as ts-kill]
            [owlbear.ts.edit.move :as ts-move]
            [owlbear.ts.edit.raise :as ts-raise]
            [owlbear.ts.edit.slurp :as ts-slurp]
            [owlbear.ts.edit.splice :as ts-splice]
            [owlbear.utilities :as obu]))

(defn ->js* [m]
  (->js (update-keys m (comp obu/kabob->camel name))))

(def html-backward-delete (comp ->js* html-delete/backward-delete))
(def html-backward-move (comp ->js* html-move/backward-move))
(def html-downward-move (comp ->js* html-move/downward-move))
(def html-forward-barf (comp ->js* html-barf/forward-barf))
(def html-forward-delete (comp ->js* html-delete/forward-delete))
(def html-forward-move (comp ->js* html-move/forward-move))
(def html-forward-slurp (comp ->js* html-slurp/forward-slurp))
(def html-kill (comp ->js* html-kill/kill))
(def html-raise (comp ->js* html-raise/raise))
(def html-splice (comp ->js* html-splice/splice))
(def html-upward-move (comp ->js* html-move/upward-move))
(def ts-backward-delete (comp ->js* ts-delete/backward-delete))
(def tsx-backward-delete (comp ->js* #(ts-delete/backward-delete % %2 %3 :tsx)))
(def ts-backward-move (comp ->js* ts-move/backward-move))
(def tsx-backward-move (comp ->js* #(ts-move/backward-move % %2 %3 :tsx)))
(def ts-downward-move (comp ->js* ts-move/downward-move))
(def tsx-downward-move (comp ->js* #(ts-move/downward-move % %2 %3 :tsx)))
(def ts-forward-barf (comp ->js* ts-barf/forward-barf))
(def tsx-forward-barf (comp ->js* #(ts-barf/forward-barf % %2 %3 :tsx)))
(def ts-forward-delete (comp ->js* ts-delete/forward-delete))
(def tsx-forward-delete (comp ->js* #(ts-delete/forward-delete % %2 %3 :tsx)))
(def ts-forward-move (comp ->js* ts-move/forward-move))
(def tsx-forward-move (comp ->js* #(ts-move/forward-move % %2 %3 :tsx)))
(def ts-forward-slurp (comp ->js* ts-slurp/forward-slurp))
(def tsx-forward-slurp (comp ->js* #(ts-slurp/forward-slurp % %2 %3 :tsx)))
(def ts-kill (comp ->js* ts-kill/kill))
(def tsx-kill (comp ->js* #(ts-kill/kill % %2 %3 :tsx)))
(def ts-raise (comp ->js* ts-raise/raise))
(def tsx-raise (comp ->js* #(ts-raise/raise % %2 %3 :tsx)))
(def ts-splice (comp ->js* ts-splice/splice))
(def tsx-splice (comp ->js* #(ts-splice/splice % %2 %3 :tsx)))
(def ts-upward-move (comp ->js* ts-move/upward-move))
(def tsx-upward-move (comp ->js* #(ts-move/upward-move % %2 %3 :tsx)))
