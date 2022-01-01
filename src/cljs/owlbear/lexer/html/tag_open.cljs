(ns owlbear.lexer.html.tag-open
  (:require [owlbear.lexer.html.token-types :as html-tknt]))

(defn tag-open-bracket-ctx?
  "Given a context, 
   returns true if the context is an HTML tag open angular bracket e.g. <"
  [ctx]
  (and (some? ctx) (html-tknt/token-type? ctx :tag-open)))

(defn tag-open-bracket-ctx
  "Given a context, 
   returns the context if the tag is an HTML opening tag"
  [ctx]
  (when (tag-open-bracket-ctx? ctx)
    ctx))
