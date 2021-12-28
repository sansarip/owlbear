(ns owlbear.grammar
  "Owlbear's domain specific grammar functions"
  (:require
   [antlr4 :as a4]
   [owlbear-grammar :as obg]
   [owlbear.utilities :as obu]
   [oops.core :refer [oget oget+ ocall]]
   [clojure.string :as string]))

(def rule-index-map
  {:html-document 0
   :html-elements 2
   :html-element  3
   :char-data 6
   :html-misc 7
   :html-comment 8})

(def token-type-map
  {:tag-open 10
   :tag-close 12
   :tag-name 16
   :tag-slash 14})

(defn src->html-document-ctx
  "Given a src string, returns an HTML document parser tree"
  [src]
  (let [chars (a4/InputStream. (str src))
        lexer (obg/HTMLLexer. chars)
        token-stream (a4/CommonTokenStream. lexer)
        parser (obg/HTMLParser. token-stream)
        _ (set! (.-buildParseTrees parser) true)]
    (.htmlDocument parser)))

(defn rule?
  "Given a context and a `rule-index-map` keyword, 
   returns true if the context is a rule of the keyword's (rule index) value"
  [ctx kw]
  (= (get rule-index-map kw) (oget ctx :?ruleIndex)))

(defn token-type?
  "Given a context and a `token-type-map` keyword, 
   returns true if the context is a token type of the keyword's (token type index) value"
  [ctx kw]
  (= (get token-type-map kw) (oget ctx :?symbol.?type)))

(defn html-char-data-ctx?
  "Given a context, 
   returns true if the context is an HTML character data ctx"
  [ctx]
  (and (some? ctx) (rule? ctx :char-data)))

(defn html-char-data-ctx
  "Given a context, 
   returns the context if `html-char-data-ctx?` returns true"
  [ctx]
  (when (html-char-data-ctx? ctx)
    ctx))

(defn html-element-ctx?
  "Given a context, 
   returns true if the context is an HTML element context"
  [ctx]
  (and (some? ctx) (rule? ctx :html-element)))

(defn html-elements-ctx?
  "Given a context, 
   returns true if the context is an HTML elements context"
  [ctx]
  (and (some? ctx) (rule? ctx :html-elements)))

(defn html-misc-ctx?
  "Given a context, 
   returns true if the context is an HTML miscellaneous context"
  [ctx]
  (and (some? ctx) (rule? ctx :html-misc)))

(defn html-comment-ctx?
  "Given a context, 
   returns true if the context is an HTML comment context"
  [ctx]
  (and (some? ctx) (rule? ctx :html-comment)))

(defn html-comment-ctx
  "Given a context, 
   returns the context if `html-comment-ctx?` returns true or
   returns the first HTML comment context if the given context is an HTML misc context"
  [ctx]
  (cond
    (html-comment-ctx? ctx) ctx
    (html-misc-ctx? ctx) (html-comment-ctx (oget ctx :?children.?0))
    :else nil))

(defn tag-name-ctx?
  "Given a context, 
   returns true if the context is an HTML tag name"
  [ctx]
  (and (some? ctx) (token-type? ctx :tag-name)))

(defn tag-name-ctx
  "Given a context, 
   returns the context if `tag-name-ctx?` returns true"
  [ctx]
  (when (tag-name-ctx? ctx)
    ctx))

(defn tag-open-bracket-ctx?
  "Given a context, 
   returns true if the context is an HTML tag open angular bracket e.g. <"
  [ctx]
  (and (some? ctx) (token-type? ctx :tag-open)))

(defn tag-open-bracket-ctx
  "Given a context, 
   returns the context if tag-open-bracket-ctx? returns true"
  [ctx]
  (when (tag-open-bracket-ctx? ctx)
    ctx))

(defn tag-close-bracket-ctx?
  "Given a context, 
   returns true if the context is an HTML tag close angular bracket e.g. >"
  [ctx]
  (and (some? ctx) (token-type? ctx :tag-close)))

(defn tag-close-bracket-ctx
  "Given a context, 
   returns the context if tag-close-bracket-ctx? returns true"
  [ctx]
  (when (tag-close-bracket-ctx? ctx)
    ctx))

(defn html-element-ctx
  "Given a context, 
   returns the context if `html-element-ctx?` returns true or
   returns the first HTML element context if the given context is an HTML elements context"
  [ctx]
  (cond
    (html-element-ctx? ctx) ctx
    (html-elements-ctx? ctx) (html-element-ctx (oget ctx :?children.?0))
    :else nil))

(defn filter-html-elements-ctxs
  "Given a list of contexts, 
   returns a filtered sequence of only the HTML element contexts"
  [ctxs]
  (filter
   (fn [ctx]
     (when (html-element-ctx? ctx)
       ctx))
   ctxs))

(defn ctx->children-seq
  "Given a context, 
   returns a flattened, depth-first traversed, lazy sequence 
   of all of the context's children"
  [ctx]
  (tree-seq #(oget % :?children) #(oget % :children) ctx))

(defn ctx->parent-seq
  "Given a context, 
   returns a flattened, depth-first traversed vector of all the context's parents"
  [ctx]
  (loop [parent-ctx (oget ctx :?parentCtx)
         parent-ctx-coll []]
    (if parent-ctx
      (recur (oget parent-ctx :?parentCtx) (conj parent-ctx-coll parent-ctx))
      parent-ctx-coll)))

(defn ctx->html-elements-ctxs
  "Given a context, 
   returns a flattened sequence of only the HTML elements in the context"
  [ctx]
  (filter-html-elements-ctxs (ctx->children-seq ctx)))

(defn range-in-ctx?
  "Given an HTML element context, a start offset, and a stop offset,
   returns true if the HTML element context is within the given range (inclusive)"
  ([ctx start]
   (range-in-ctx? ctx start start))
  ([ctx start stop]
   (and start
        stop
        (let [ctx-start (oget ctx :?start.?start)
              ctx-stop (oget ctx :?stop.?stop)]
          (and
           ctx-start
           ctx-stop
           (apply <=
                  (concat [ctx-start]
                          (range start (inc stop))
                          [ctx-stop])))))))

(defn sibling-ctxs
  "Given a context, 
   returns the sibling contexts for that context"
  [ctx]
  (rest (concat (oget ctx :?parentCtx.?children)
                (oget ctx :?parentCtx.?parentCtx.?children))))

(defn forward-ctx?
  "Given a context and a second context, 
   returns true if the second context is positionall ahead of the first context"
  [ctx forward-ctx]
  (< (oget ctx :?stop.?stop) (oget forward-ctx :?start.?start)))

(defn forward-sibling-html-comment-ctx?
  "Given a context and a second context, 
   returns true if the second context is an HTML comment context
   that is positionally ahead of the first context"
  [ctx forward-ctx]
  (and (or (html-comment-ctx? forward-ctx) (html-misc-ctx? forward-ctx))
       (forward-ctx? ctx forward-ctx)))

(defn forward-sibling-html-comment-ctx
  "Calls `forward-sibling-html-comment-ctx?` and returns the second context, 
   coerced to an HTML comment context, 
   if `forward-sibling-html-comment-ctx?` returns true"
  [ctx forward-ctx]
  (when (forward-sibling-html-comment-ctx? ctx forward-ctx)
    (html-comment-ctx forward-ctx)))

(defn forward-sibling-html-element-ctx?
  "Given a context and a context, 
   returns true if the second context is an HTML element(s) context 
   that is positionally ahead of the first context"
  [ctx forward-ctx]
  (and (or (html-element-ctx? forward-ctx) (html-elements-ctx? forward-ctx))
       (forward-ctx? ctx forward-ctx)))

(defn forward-sibling-html-element-ctx
  "Calls `forward-sibling-html-element-ctx?` and returns the second context, 
   coerced to an HTML element context, 
   if `forward-sibling-html-element-ctx?` returns true"
  [ctx forward-ctx]
  (when (forward-sibling-html-element-ctx? ctx forward-ctx)
    (html-element-ctx forward-ctx)))

(defn forward-sibling-html-char-data-ctx?
  "Given a context and a second context, 
   returns true if the second context is an HTML character data context
   that is positionally ahead of the first context"
  [ctx forward-ctx]
  (and (html-char-data-ctx? forward-ctx)
       (forward-ctx? ctx forward-ctx)))

(defn forward-sibling-html-char-data-ctx
  "Calls `forward-sibling-html-char-data-ctx?` and returns the second context 
   if `forward-sibling-html-char-data-ctx?` returns true"
  [ctx forward-ctx]
  (when (forward-sibling-html-char-data-ctx? ctx forward-ctx)
    forward-ctx))

(defn some-sibling-ctx
  "Given a context (and optionally a parent context), 
   returns the first sibling context that fulfills the predicate function"
  [pred ctx]
  (some pred (sibling-ctxs ctx)))

(defn next-sibling-html-element-ctx
  "Given a context (and optionally a parent context), 
   returns the next sibling context that is an HTML element ctx"
  [ctx]
  (some-sibling-ctx (partial forward-sibling-html-element-ctx ctx) ctx))

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
                                       (when (or (tag-open-bracket-ctx? ctx)
                                                 (tag-close-bracket-ctx? ctx))
                                         index))
                                     ctx-children)]
      (when (and (number? tag-open-bracket-index) (number? tag-close-bracket-index))
        (let [[tag-open-bracket-ctx*
               :as tag-ctxs] (subvec (vec ctx-children)
                                     tag-open-bracket-index
                                     (inc tag-close-bracket-index))
              tag-close-bracket-ctx* (last tag-ctxs)]
          {:contexts tag-ctxs
           :tag-name (some-> (some tag-name-ctx tag-ctxs) (ocall :?getText))
           :start-index tag-open-bracket-index
           :stop-index tag-close-bracket-index
           :start-offset (oget tag-open-bracket-ctx* :?symbol.?start)
           :stop-offset (oget tag-close-bracket-ctx* :?symbol.?stop)})))))

(defn html-comment-ctx-start-tag-map
  "Given an HTML comment context, 
   returns a map representing the HTML comment's start tag, 
   containing a string tag name (`:tag-name`), 
   a tag start offset (`:start-offset`), 
   and a tag stop offset (`:stop-offset`)"
  [ctx]
  (when (html-comment-ctx? ctx)
    (when-let [html-comment-txt (ocall ctx :?getText)]
      (when (string/starts-with? html-comment-txt "<!--")
        (let [html-comment-start-offset (oget ctx :?start.?start)]
          {:tag-name "<!--"
           :start-offset html-comment-start-offset
           :stop-offset (+ html-comment-start-offset 3)})))))

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
                                                      (or (tag-open-bracket-ctx? ctx)
                                                          (tag-close-bracket-ctx? ctx)))
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
               :tag-name (some-> (some tag-name-ctx tag-ctxs) (ocall :?getText))
               :start-index tag-open-bracket-index
               :stop-index tag-close-bracket-index
               :start-offset (oget tag-open-bracket-ctx* :?symbol.?start)
               :stop-offset (oget tag-close-bracket-ctx* :?symbol.?stop)})))))))

(defn html-comment-ctx-end-tag-map
  "Given an HTML comment context, 
   returns a map representing the HTML comment's end tag, 
   containing a string tag name (`:tag-name`), 
   a tag start offset (`:start-offset`), 
   and a tag stop offset (`:stop-offset`)"
  [ctx]
  (when (html-comment-ctx? ctx)
    (when-let [html-comment-txt (ocall ctx :?getText)]
      (when (string/ends-with? html-comment-txt "-->")
        (let [html-comment-stop-offset (oget ctx :?stop.?stop)]
          {:tag-name "-->"
           :start-offset (- html-comment-stop-offset 2)
           :stop-offset html-comment-stop-offset})))))

(defn filter-current-ctxs
  "Given a list of contexts and a character offset, 
   a lazy sequence of only the contexts containing the given offset"
  [offset ctxs]
  (filter #(range-in-ctx? % offset) ctxs))

(defn ctx->current-ctxs
  "Given a context and a character offset, 
   return a lazy sequence of the contexts containing the given offset"
  [ctx offset]
  (filter-current-ctxs offset (ctx->children-seq ctx)))

(defn ctx->current-html-element-ctxs
  "Given a context and a character offset, 
   returns a lazy sequence of the HTML element contexts containing the given offset"
  [ctx offset]
  (filter-current-ctxs offset (ctx->html-elements-ctxs ctx)))

(defn src-with-cursor-symbol->current-ctx-map
  "Given a src string (and optionally a string), 
   return the HTML element context at the cursor symbol"
  ([src]
   (src-with-cursor-symbol->current-ctx-map src nil))
  ([src {:keys [ctx-type cursor-symbol]
         :or {cursor-symbol "📍"}}]
   (let [cursor-symbol-start-offset (string/index-of src cursor-symbol)
         cursor-symbol-length (count cursor-symbol)
         cursor-symbol-stop-offset (+ cursor-symbol-start-offset cursor-symbol-length)
         actual-cursor-symbol-offset (dec cursor-symbol-start-offset)
         src-without-cursor-symbol (obu/str-remove src cursor-symbol-start-offset cursor-symbol-stop-offset)
         html-doc-ctx (src->html-document-ctx src-without-cursor-symbol)
         ctx->current-ctxs-fn (if (= ctx-type :html-element)
                                ctx->current-html-element-ctxs
                                ctx->current-ctxs)
         current-ctx (last (ctx->current-ctxs-fn html-doc-ctx actual-cursor-symbol-offset))
         root-ctx (last (ctx->parent-seq current-ctx))]
     {:current-ctx current-ctx
      :root-ctx root-ctx
      :src-without-cursor-symbol src-without-cursor-symbol
      :cursor-offset actual-cursor-symbol-offset})))