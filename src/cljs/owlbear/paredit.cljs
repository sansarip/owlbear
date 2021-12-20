(ns owlbear.paredit
  (:require [owlbear.grammar :as obg]
            [owlbear.utilities :as obu]
            [oops.core :refer [oget oget+ ocall]]))


(defn forward-slurp-ctx-map
  "Given a context and character offset, 
   returns a map of the deepest context (containing the offset)
   with a forward sibling slurpable context"
  [ctx offset]
  (->> (obg/current-html-element-ctxs ctx offset)
       (keep (fn [current-ctx]
               ;; TODO: Make this work for other applicable slurpable context types as well, like HTML content contexts
               (when-let [fwd-slurpable-ctx (obg/next-sibling-html-element-ctx current-ctx)]
                 {:fwd-slurpable-ctx fwd-slurpable-ctx
                  :current-ctx current-ctx})))
       last))


(defn forward-slurp
  "Given a src string and character offset, 
   returns a new src string with the forward slurp operation applied at the offset"
  [src offset]
  (when-let [{:keys [fwd-slurpable-ctx
                     current-ctx]} (forward-slurp-ctx-map
                                    (obg/src->html-document-ctx src)
                                    offset)]
    (let [{current-ctx-end-tag-start-offset :start-offset
           current-ctx-end-tag-stop-offset :stop-offset} (obg/end-tag current-ctx)
          current-ctx-end-tag-text (subs src
                                         current-ctx-end-tag-start-offset
                                         (inc current-ctx-end-tag-stop-offset))
          fwd-slurpable-end-offset (oget fwd-slurpable-ctx :?stop.?stop)]
      (-> src
          (obu/str-remove current-ctx-end-tag-start-offset (inc current-ctx-end-tag-stop-offset))
          (obu/str-insert current-ctx-end-tag-text (- (inc fwd-slurpable-end-offset)
                                                      (count current-ctx-end-tag-text)))))))

