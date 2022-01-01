(ns owlbear.lexer.html.tag-close
  (:require [owlbear.lexer.html.token-types :as html-tknt]))

(defn tag-close-bracket-ctx?
  "Given a context, 
   returns true if the context is an HTML tag close angular bracket e.g. >"
  [ctx]
  (and (some? ctx) (html-tknt/token-type? ctx :tag-close)))

(defn tag-close-bracket-ctx
  "Given a context, 
   returns the context if the context is an HTML closing tag"
  [ctx]
  (when (tag-close-bracket-ctx? ctx)
    ctx))