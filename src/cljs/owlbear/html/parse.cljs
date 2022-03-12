(ns owlbear.html.parse
  "HTML parsing"
  (:require [oops.core :refer [ocall]]
            [web-tree-sitter :as Parser]))

(def ^:dynamic *html*)

(defn init-tree-sitter! [wasm-path]
  (-> (ocall Parser :init)
      (.then #(ocall Parser :Language.load wasm-path))
      (.then #(set! *html* %))))

(defn src->tree
  "Given a source string, returns an HTML parser tree"
  [src]
  (let [parser (Parser.)]
    (ocall parser :setLanguage *html*)
    (ocall parser :parse src)))

;; (defn src-with-cursor-symbol->current-ctx-map
;;   "Given a source string (and optionally a string), 
;;    return the HTML element context at the cursor symbol"
;;   ([src]
;;    (src-with-cursor-symbol->current-ctx-map src nil))
;;   ([src {:keys [ctx-type cursor-symbol]
;;          :or {cursor-symbol "📍"}}]
;;    (let [cursor-symbol-start-offset (string/index-of src cursor-symbol)
;;          cursor-symbol-length (count cursor-symbol)
;;          cursor-symbol-stop-offset (+ cursor-symbol-start-offset cursor-symbol-length)
;;          actual-cursor-symbol-offset (dec cursor-symbol-start-offset)
;;          src-without-cursor-symbol (obu/str-remove src cursor-symbol-start-offset cursor-symbol-stop-offset)
;;          html-ctx (src->html src-without-cursor-symbol)
;;          ctx->current-ctxs-fn (if (= ctx-type :html-element)
;;                                 obp-html-ele/ctx->html-elements-ctxs
;;                                 obpr/ctx->current-ctxs)
;;          current-ctx (last (ctx->current-ctxs-fn html-ctx actual-cursor-symbol-offset))
;;          root-ctx (last (obpr/ctx->parent-seq current-ctx))]
;;      {:current-ctx current-ctx
;;       :root-ctx root-ctx
;;       :src-without-cursor-symbol src-without-cursor-symbol
;;       :cursor-offset actual-cursor-symbol-offset})))
