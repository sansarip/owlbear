(ns owlbear.parser.html.tagedit
  (:require [owlbear.parser.html.tagedit.barf :as obp-html-barf]
            [owlbear.parser.html.tagedit.slurp :as obp-html-slurp]))

(def forward-barf obp-html-barf/forward-barf)
(def forward-slurp obp-html-slurp/forward-slurp)