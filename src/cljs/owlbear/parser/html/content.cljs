(ns owlbear.parser.html.content
  (:require [owlbear.parser.html.rules :as obp-html-rules]))

(defn html-content-ctx?
  "Given a context, 
   returns true if the context is an HTML content context"
  [ctx]
  (and (some? ctx) (obp-html-rules/rule? ctx :html-content)))

(defn html-content-ctx
  "Given a context, 
   returns the context if the context is an HTML content context"
  [ctx]
  (when (html-content-ctx? ctx)
    ctx))