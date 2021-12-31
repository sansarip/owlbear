(ns owlbear.tagedit
  "Owlbear's domain specific paredit-like tagedit functions"
  (:require [owlbear.grammar :as obg]
            [owlbear.utilities :as obu]
            [oops.core :refer [oget oget+ ocall]]))

(defn all-white-space-chars? [ctx]
  (boolean (some-> ctx
                   (ocall :?getText)
                   (->> (re-matches #"\s+")))))

(defn not-all-white-space-chars [ctx]
  (when-not (all-white-space-chars? ctx)
    ctx))

(defn forward-slurpable-ctx [ctx forward-ctx]
  (or (obg/forward-sibling-html-element-ctx ctx forward-ctx)
      (not-all-white-space-chars (obg/forward-sibling-html-char-data-ctx ctx forward-ctx))
      (obg/forward-sibling-html-comment-ctx ctx forward-ctx)))

(defn next-forward-slurpable-ctx [ctx]
  (obg/some-sibling-ctx (partial forward-slurpable-ctx ctx) ctx))

(defn editable-ctx? [ctx]
  (or (obg/html-element-ctx? ctx) (obg/html-comment-ctx? ctx)))

(defn ctx->current-editable-ctxs [ctx offset]
  (filter editable-ctx? (obg/ctx->current-ctxs ctx offset)))

(defn forward-slurp-ctx-map
  "Given a context and character offset, 
   returns a map of the deepest context (containing the offset)
   with a forward sibling slurpable context"
  [ctx offset]
  (->> (ctx->current-editable-ctxs ctx offset)
       (keep (fn [current-ctx]
               (when-let [fwd-slurpable-ctx (next-forward-slurpable-ctx current-ctx)]
                 {:fwd-slurpable-ctx fwd-slurpable-ctx
                  :current-ctx current-ctx})))
       last))

(defn editable-ctx-end-tag-map [ctx]
  (or (obg/html-element-ctx-end-tag-map ctx)
      (obg/html-comment-ctx-end-tag-map ctx)))

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
                                    (obg/src->html-document-ctx src)
                                    offset)]
    ;; Current context must have an end tag
    (when-let [{current-ctx-end-tag-start-offset :start-offset
                current-ctx-end-tag-stop-offset :stop-offset} (editable-ctx-end-tag-map current-ctx)]
      (let [current-ctx-end-tag-text (subs src
                                           current-ctx-end-tag-start-offset
                                           (inc current-ctx-end-tag-stop-offset))
            fwd-slurpable-end-offset (oget fwd-slurpable-ctx :?stop.?stop)]
        (-> src
            (obu/str-remove current-ctx-end-tag-start-offset (inc current-ctx-end-tag-stop-offset))
            (obu/str-insert current-ctx-end-tag-text (- (inc fwd-slurpable-end-offset)
                                                        (count current-ctx-end-tag-text))))))))

(defn barfable-ctx [ctx]
  (or (obg/html-element-ctx ctx)
      (not-all-white-space-chars (obg/html-char-data-ctx ctx))
      (obg/html-comment-ctx ctx)))

(defn child-ctxs [ctx]
  (when-let [children (oget ctx :?children)]
    (or (some-> (some obg/html-content-ctx children)
                (oget :?children))
        (some-> (obg/html-comment-ctx ctx)
                obg/html-comment-ctx-content
                ;; Content has to be between tags to get parsed 📌
                (as-> $ (obg/src->html-document-ctx (str "<📌>" $ "</📌>")))
                (oget :?children.?0.?children.?0.?children)
                (->> (some obg/html-content-ctx))
                (oget :?children)))))

(defn next-forward-barfable-ctx [ctx]
  (last (filter barfable-ctx (child-ctxs ctx))))

(defn forward-barf-ctx-map
  "Given a context and character offset, 
   returns a map of the deepest context (containing the offset)
   with a barfable child context"
  [ctx offset]
  (->> (ctx->current-editable-ctxs ctx offset)
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
                                    (obg/src->html-document-ctx src)
                                    offset)]
    ;; Current context must have an end tag
    (when-let [{current-ctx-end-tag-start-offset :start-offset
                current-ctx-end-tag-stop-offset :stop-offset} (editable-ctx-end-tag-map current-ctx)]
      (let [current-ctx-end-tag-text (subs src
                                           current-ctx-end-tag-start-offset
                                           (inc current-ctx-end-tag-stop-offset))
            fwd-barfable-start-offset (oget fwd-barfable-ctx :?start.?start)]
        (-> src
            (obu/str-remove current-ctx-end-tag-start-offset (inc current-ctx-end-tag-stop-offset))
            (obu/str-insert current-ctx-end-tag-text fwd-barfable-start-offset))))))