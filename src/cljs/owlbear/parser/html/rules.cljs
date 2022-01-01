(ns owlbear.parser.html.rules
  (:require [oops.core :refer [oget]]))

(def rule-index-map
  {:html-document 0
   :html-elements 2
   :html-element  3
   :html-content 4
   :char-data 6
   :html-misc 7
   :html-comment 8})

(defn rule?
  "Given a context and a `rule-index-map` keyword, 
   returns true if the context is a rule of the keyword's (rule index) value"
  [ctx kw]
  (= (get rule-index-map kw) (oget ctx :?ruleIndex)))
