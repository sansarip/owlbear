(ns owlbear.parser.html.edit
  (:require [owlbear.parser.html.edit.barf :as obp-html-barf]
            [owlbear.parser.html.edit.slurp :as obp-html-slurp]))

(def forward-barf obp-html-barf/forward-barf)
(def forward-slurp obp-html-slurp/forward-slurp)