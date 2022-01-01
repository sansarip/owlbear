(ns owlbear.parser.html.char-data
  (:require [owlbear.parser.utilities :as putils]
            [owlbear.parser.html.rules :as obp-html-rules]))

(defn html-char-data-ctx?
  "Given a context, 
   returns true if the context is an HTML character data context"
  [ctx]
  (and (some? ctx) (obp-html-rules/rule? ctx :char-data)))

(defn html-char-data-ctx
  "Given a context, 
   returns the context if the context is an HTML character data context"
  [ctx]
  (when (html-char-data-ctx? ctx)
    ctx))

(defn forward-sibling-html-char-data-ctx?
  "Given a context and a second context, 
   returns true if the second context is an HTML character data context
   that is positionally ahead of the first context"
  [ctx forward-ctx]
  (and (html-char-data-ctx? forward-ctx)
       (putils/forward-ctx? ctx forward-ctx)))

(defn forward-sibling-html-char-data-ctx
  "Calls `forward-sibling-html-char-data-ctx?` and returns the second context 
   if `forward-sibling-html-char-data-ctx?` returns true"
  [ctx forward-ctx]
  (when (forward-sibling-html-char-data-ctx? ctx forward-ctx)
    forward-ctx))