(ns owlbear.parser.html.misc
  (:require [oops.core :refer [ocall oget]]
            [owlbear.parser.utilities :as putils]
            [owlbear.parser.html.rules :as obp-html-rules]))

(defn html-misc-ctx?
  "Given a context, 
   returns true if the context is an HTML miscellaneous context"
  [ctx]
  (and (some? ctx) (obp-html-rules/rule? ctx :html-misc)))

(defn html-comment-ctx?
  "Given a context, 
   returns true if the context is an HTML comment context"
  [ctx]
  (and (some? ctx) (obp-html-rules/rule? ctx :html-comment)))

(defn html-comment-ctx
  "Given a context, 
   returns the context if the context is an HTML comment context or 
   returns the first HTML comment context if the given context is an HTML miscellaneous context"
  [ctx]
  (cond
    (html-comment-ctx? ctx) ctx
    (html-misc-ctx? ctx) (html-comment-ctx (oget ctx :?children.?0))
    :else nil))

(defn forward-sibling-html-comment-ctx?
  "Given a context and a second context, 
   returns true if the second context is an HTML comment context
   that is positionally ahead of the first context"
  [ctx forward-ctx]
  (and (or (html-comment-ctx? forward-ctx) (html-misc-ctx? forward-ctx))
       (putils/forward-ctx? ctx forward-ctx)))

(defn forward-sibling-html-comment-ctx
  "Given a context and a second context, 
   returns the second context as an HTML comment context if
   the second context is an HTML misc/comment context 
   and the second context is positional ahead of the first context"
  [ctx forward-ctx]
  (when (forward-sibling-html-comment-ctx? ctx forward-ctx)
    (html-comment-ctx forward-ctx)))

(defn html-comment-ctx-start-tag-map
  "Given an HTML comment context, 
   returns a map representing the HTML comment's start tag, 
   containing a string tag name (`:tag-name`), 
   a tag start offset (`:start-offset`), 
   and a tag stop offset (`:stop-offset`)"
  [ctx]
  (some-> (html-comment-ctx ctx)
          (ocall :?getText)
          (->> (re-find #"^<!--"))
          (as-> $
                (let [html-comment-start-offset (oget ctx :?start.?start)]
                  {:tag-name $
                   :start-offset html-comment-start-offset
                   :stop-offset (-> (count $) dec (+ html-comment-start-offset))}))))


(defn html-comment-ctx-end-tag-map
  "Given an HTML comment context, 
   returns a map representing the HTML comment's end tag, 
   containing a string tag name (`:tag-name`), 
   a tag start offset (`:start-offset`), 
   and a tag stop offset (`:stop-offset`)"
  [ctx]
  (some-> (html-comment-ctx ctx)
          (ocall :?getText)
          (->> (re-find #"-->$"))
          (as-> $
                (let [html-comment-stop-offset (oget ctx :?stop.?stop)]
                  {:tag-name $
                   :start-offset (->> (count $) dec (- html-comment-stop-offset))
                   :stop-offset html-comment-stop-offset}))))

(defn html-comment-ctx-content
  "Given an HTML comment context, 
   returns a string representing the HTML comment's content 
   (between its start and end tags)"
  [ctx]
  (let [{start-tag-stop-offset :stop-offset} (html-comment-ctx-start-tag-map ctx)
        {end-tag-start-offset :start-offset} (html-comment-ctx-end-tag-map ctx)]
    (some-> (ocall ctx :?getText)
            (subs (inc start-tag-stop-offset) end-tag-start-offset))))