(ns owlbear.parser.html.tagedit.utilities
  (:require [owlbear.parser.html.elements :as obp-html-ele]
            [owlbear.parser.html.misc :as obp-html-misc]
            [owlbear.parser.utilities :as obpu]))

(defn tag-editable-ctx? [ctx]
  (or (obp-html-ele/html-element-ctx? ctx)
      (obp-html-misc/html-comment-ctx? ctx)))

(defn tag-editable-ctx-end-tag-map [ctx]
  (or (obp-html-ele/html-element-ctx-end-tag-map ctx)
      (obp-html-misc/html-comment-ctx-end-tag-map ctx)))

(defn ctx->current-tag-editable-ctxs [ctx offset]
  (filter tag-editable-ctx? (obpu/ctx->current-ctxs ctx offset)))
