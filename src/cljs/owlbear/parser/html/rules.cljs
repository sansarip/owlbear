(ns owlbear.parser.html.rules
  (:require [oops.core :refer [oget]]))

(def rule-index-map
  {:html 0
   :html-document 1
   :html-elements 3
   :html-element  4
   :html-content 5
   :char-data 7
   :html-misc 8
   :html-comment 9})

(defn rule?
  "Given a context and a `rule-index-map` keyword, 
   returns true if the context is a rule of the keyword's (rule index) value"
  [ctx kw]
  (= (get rule-index-map kw) (oget ctx :?ruleIndex)))
