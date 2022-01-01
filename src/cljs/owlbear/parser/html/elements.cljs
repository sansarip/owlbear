(ns owlbear.parser.html.elements
  (:require [oops.core :refer [oget ocall]]
            [owlbear.lexer.html.tag-open :as obl-html-tago]
            [owlbear.lexer.html.tag-close :as obl-html-tagc]
            [owlbear.lexer.html.tag-name :as obl-html-tagn]
            [owlbear.parser.html.rules :as obp-html-rules]
            [owlbear.parser.utilities :as obpu]))

(defn html-element-ctx?
  "Given a context, 
   returns true if the context is an HTML element context"
  [ctx]
  (and (some? ctx) (obp-html-rules/rule? ctx :html-element)))

(defn html-elements-ctx?
  "Given a context, 
   returns true if the context is an HTML elements context"
  [ctx]
  (and (some? ctx) (obp-html-rules/rule? ctx :html-elements)))

(defn html-element-ctx
  "Given a context, 
   returns the context if the context is an HTML element context or 
   returns the first HTML element context if the given context is an HTML elements context"
  [ctx]
  (cond
    (html-element-ctx? ctx) ctx
    (html-elements-ctx? ctx) (html-element-ctx (oget ctx :?children.?0))
    :else nil))

(defn forward-sibling-html-element-ctx?
  "Given a context and a second context, 
   returns true if the second context is an HTML element(s) context 
   that is positionally ahead of the first context"
  [ctx forward-ctx]
  (and (or (html-element-ctx? forward-ctx) (html-elements-ctx? forward-ctx))
       (obpu/forward-ctx? ctx forward-ctx)))

(defn forward-sibling-html-element-ctx
  "Given a context and a second context, 
   returns the second context as an HTML element  
   if the second context is an HTML element(s) context 
   and is positionally ahead of the first context"
  [ctx forward-ctx]
  (when (forward-sibling-html-element-ctx? ctx forward-ctx)
    (html-element-ctx forward-ctx)))

(defn next-sibling-html-element-ctx
  "Given a context (and optionally a parent context), 
   returns the next sibling context that is an HTML element context"
  [ctx]
  (obpu/some-sibling-ctx (partial forward-sibling-html-element-ctx ctx) ctx))

(defn html-element-ctx-start-tag-map
  "Given an HTML element context, 
   returns a map representing the HTML element's start tag, 
   containing a vector of tag contexts (`:contexts`), 
   a string tag name (`:tag-name`), 
   a start index position of the contexts (`:start-index`), 
   a stop index position of the contexts (`:stop-index`), 
   a tag start offset (`:start-offset`), 
   and a tag stop offset (`:stop-offset`)"
  [ctx]
  (when-let [ctx-children (oget ctx :?children)]
    (let [[tag-open-bracket-index
           tag-close-bracket-index] (keep-indexed
                                     (fn [index ctx]
                                       (when (or (obl-html-tago/tag-open-bracket-ctx? ctx)
                                                 (obl-html-tagc/tag-close-bracket-ctx? ctx))
                                         index))
                                     ctx-children)]
      (when (and (number? tag-open-bracket-index) (number? tag-close-bracket-index))
        (let [[tag-open-bracket-ctx*
               :as tag-ctxs] (subvec (vec ctx-children)
                                     tag-open-bracket-index
                                     (inc tag-close-bracket-index))
              tag-close-bracket-ctx* (last tag-ctxs)]
          {:contexts tag-ctxs
           :tag-name (some-> (some obl-html-tagn/tag-name-ctx tag-ctxs) (ocall :?getText))
           :start-index tag-open-bracket-index
           :stop-index tag-close-bracket-index
           :start-offset (oget tag-open-bracket-ctx* :?symbol.?start)
           :stop-offset (oget tag-close-bracket-ctx* :?symbol.?stop)})))))

(defn html-element-ctx-end-tag-map
  "Given an HTML element context, 
   returns a map representing the HTML element's end tag, 
   containing a vector of tag contexts (`:contexts`), 
   a string expected tag name based on the start tag's name (`:expected-tag-name`), 
   a string actual tag name (`:tag-name`), 
   a start index position of the contexts (`:start-index`), 
   a stop index position of the contexts (`:stop-index`), 
   a tag start offset (`:start-offset`), 
   and a tag stop offset (`:stop-offset`)"
  [ctx]
  (when (html-element-ctx? ctx)
    (when-let [ctx-children (oget ctx :?children)]
      (when-let [{:keys [tag-name]
                  start-tag-stop-index :stop-index} (html-element-ctx-start-tag-map ctx)]
        (let [[tag-open-bracket-index
               tag-close-bracket-index] (keep-indexed
                                         (fn [index ctx]
                                           (when (and (> index start-tag-stop-index)
                                                      (or (obl-html-tago/tag-open-bracket-ctx? ctx)
                                                          (obl-html-tagc/tag-close-bracket-ctx? ctx)))
                                             index))
                                         ctx-children)]
          (when (and (number? tag-open-bracket-index) (number? tag-close-bracket-index))
            (let [[tag-open-bracket-ctx*
                   :as tag-ctxs] (subvec (vec ctx-children)
                                         tag-open-bracket-index
                                         (inc tag-close-bracket-index))
                  tag-close-bracket-ctx* (last tag-ctxs)]
              {:contexts tag-ctxs
               :expected-tag-name tag-name
               :tag-name (some-> (some obl-html-tagn/tag-name-ctx tag-ctxs) (ocall :?getText))
               :start-index tag-open-bracket-index
               :stop-index tag-close-bracket-index
               :start-offset (oget tag-open-bracket-ctx* :?symbol.?start)
               :stop-offset (oget tag-close-bracket-ctx* :?symbol.?stop)})))))))

(defn filter-html-elements-ctxs
  "Given a list of contexts, 
   returns a lazy sequence of only the HTML element contexts"
  [ctxs]
  (filter html-element-ctx? ctxs))

(defn ctx->html-elements-ctxs
  "Given a context, 
   returns a flattened sequence of only the HTML elements in the context"
  [ctx]
  (filter-html-elements-ctxs (obpu/ctx->children-seq ctx)))

(defn ctx->current-html-element-ctxs
  "Given a context and a character offset, 
   returns a lazy sequence of the HTML element contexts containing the given offset"
  [ctx offset]
  (obpu/filter-current-ctxs offset (ctx->html-elements-ctxs ctx)))
