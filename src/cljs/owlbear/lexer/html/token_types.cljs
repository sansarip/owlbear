(ns owlbear.lexer.html.token-types
  (:require [oops.core :refer [oget]]))

(def token-type-map
  {:tag-open 10
   :tag-close 12
   :tag-name 16
   :tag-slash 14})

(defn token-type?
  "Given a context and a `token-type-map` keyword, 
   returns true if the context is a token type of the keyword's (token type index) value"
  [ctx kw]
  (= (get token-type-map kw) (oget ctx :?symbol.?type)))
