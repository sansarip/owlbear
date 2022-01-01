(ns owlbear.lexer.html.tag-name
  (:require [owlbear.lexer.html.token-types :as html-tknt]))

(defn tag-name-ctx?
  "Given a context, 
   returns true if the context is an HTML tag name"
  [ctx]
  (and (some? ctx) (html-tknt/token-type? ctx :tag-name)))

(defn tag-name-ctx
  "Given a context, 
   returns the context if `tag-name-ctx?` returns true"
  [ctx]
  (when (tag-name-ctx? ctx)
    ctx))