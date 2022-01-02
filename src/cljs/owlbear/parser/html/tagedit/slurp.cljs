(ns owlbear.parser.html.tagedit.slurp
  (:require  [oops.core :refer [oget]]
             [owlbear.parser.html.char-data :as obp-html-char]
             [owlbear.parser.html :as obp-html]
             [owlbear.parser.html.elements :as obp-html-ele]
             [owlbear.parser.html.misc :as obp-html-misc]
             [owlbear.parser.html.tagedit.utilities :as obp-html-tagedit-util]
             [owlbear.parser.html.utilities :as obp-html-util]
             [owlbear.parser.utilities :as obpu]
             [owlbear.utilities :as obu]))

(defn forward-slurpable-ctx [ctx forward-ctx]
  (or (obp-html-ele/forward-sibling-html-element-ctx ctx forward-ctx)
      (obp-html-util/not-all-white-space-chars (obp-html-char/forward-sibling-html-char-data-ctx ctx forward-ctx))
      (obp-html-misc/forward-sibling-html-comment-ctx ctx forward-ctx)))

(defn next-forward-slurpable-ctx [ctx]
  (obpu/some-sibling-ctx (partial forward-slurpable-ctx ctx) ctx))

(defn forward-slurp-ctx-map
  "Given a context and character offset, 
   returns a map of the deepest context (containing the offset)
   with a forward sibling slurpable context"
  [ctx offset]
  (->> (obp-html-tagedit-util/ctx->current-tag-editable-ctxs ctx offset)
       (keep (fn [current-ctx]
               (when-let [fwd-slurpable-ctx (next-forward-slurpable-ctx current-ctx)]
                 {:fwd-slurpable-ctx fwd-slurpable-ctx
                  :current-ctx current-ctx})))
       last))

(defn forward-slurp
  "Given a src string and character offset, 
   returns a new src string with the forward slurp operation applied at the offset

   e.g.
   ```html
     <div>📍</div><h1></h1> => <div>📍<h1></h1></div>
   ```"
  [src offset]
  (when-let [{:keys [fwd-slurpable-ctx
                     current-ctx]} (forward-slurp-ctx-map
                                    (obp-html/src->html src)
                                    offset)]
    ;; Current context must have an end tag
    (when-let [{current-ctx-end-tag-start-offset :start-offset
                current-ctx-end-tag-stop-offset :stop-offset} (obp-html-tagedit-util/tag-editable-ctx-end-tag-map current-ctx)]
      (let [current-ctx-end-tag-text (subs src
                                           current-ctx-end-tag-start-offset
                                           (inc current-ctx-end-tag-stop-offset))
            fwd-slurpable-end-offset (oget fwd-slurpable-ctx :?stop.?stop)]
        (-> src
            (obu/str-remove current-ctx-end-tag-start-offset (inc current-ctx-end-tag-stop-offset))
            (obu/str-insert current-ctx-end-tag-text (- (inc fwd-slurpable-end-offset)
                                                        (count current-ctx-end-tag-text))))))))
