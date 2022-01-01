(ns owlbear.parser.html.tagedit.barf
  (:require  [oops.core :refer [oget]]
             [owlbear.parser.html.char-data :as obp-html-char]
             [owlbear.parser.html.content :as obp-html-cont]
             [owlbear.parser.html.document :as obp-html-doc]
             [owlbear.parser.html.elements :as obp-html-ele]
             [owlbear.parser.html.misc :as obp-html-misc]
             [owlbear.parser.html.tagedit.utilities :as obp-html-tagedit-util]
             [owlbear.parser.html.utilities :as obp-html-util]
             [owlbear.utilities :as obu]))

(defn barfable-ctx [ctx]
  (or (obp-html-ele/html-element-ctx ctx)
      (obp-html-util/not-all-white-space-chars (obp-html-char/html-char-data-ctx ctx))
      (obp-html-misc/html-comment-ctx ctx)))

(defn child-ctxs [ctx]
  (when-let [children (oget ctx :?children)]
    (or (some-> (some obp-html-cont/html-content-ctx children)
                (oget :?children))
        (some-> (obp-html-misc/html-comment-ctx ctx)
                obp-html-misc/html-comment-ctx-content
                ;; Content has to be between tags to get parsed 📌
                (as-> $ (obp-html-doc/src->html-document-ctx (str "<📌>" $ "</📌>")))
                (oget :?children.?0.?children.?0.?children)
                (->> (some obp-html-cont/html-content-ctx))
                (oget :?children)))))

(defn next-forward-barfable-ctx [ctx]
  (last (filter barfable-ctx (child-ctxs ctx))))

(defn forward-barf-ctx-map
  "Given a context and character offset, 
   returns a map of the deepest context (containing the offset)
   with a barfable child context"
  [ctx offset]
  (->> (obp-html-tagedit-util/ctx->current-tag-editable-ctxs ctx offset)
       (keep (fn [current-ctx]
               (when-let [fwd-barfable-ctx (next-forward-barfable-ctx current-ctx)]
                 {:fwd-barfable-ctx fwd-barfable-ctx
                  :current-ctx current-ctx})))
       last))

(defn forward-barf
  "Given a src string and character offset, 
   returns a new src string with the forward barf operation applied at the offset
  
   e.g.
   ```html
     <div>📍<h1></h1></div> => <div>📍</div><h1></h1>
   ```"
  [src offset]
  (when-let [{:keys [fwd-barfable-ctx
                     current-ctx]} (forward-barf-ctx-map
                                    (obp-html-doc/src->html-document-ctx src)
                                    offset)]
    ;; Current context must have an end tag
    (when-let [{current-ctx-end-tag-start-offset :start-offset
                current-ctx-end-tag-stop-offset :stop-offset} (obp-html-tagedit-util/tag-editable-ctx-end-tag-map current-ctx)]
      (let [current-ctx-end-tag-text (subs src
                                           current-ctx-end-tag-start-offset
                                           (inc current-ctx-end-tag-stop-offset))
            fwd-barfable-start-offset (oget fwd-barfable-ctx :?start.?start)]
        (-> src
            (obu/str-remove current-ctx-end-tag-start-offset (inc current-ctx-end-tag-stop-offset))
            (obu/str-insert current-ctx-end-tag-text fwd-barfable-start-offset))))))