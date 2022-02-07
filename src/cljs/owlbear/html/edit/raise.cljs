(ns owlbear.html.edit.raise 
  (:require [owlbear.parse.rules :as obpr]
            [owlbear.html.parse :as ob-html]
            [oops.core :refer [oget]]
            [owlbear.utilities :as obu]))
(defn raise
  [src offset]
  (let [root-node (oget (ob-html/src->tree src) :rootNode)
        current-node (last (obpr/node->current-nodes root-node offset))
        current-parent (oget current-node :parent)
        current-parent-start (oget current-parent :startIndex)
        current-parent-end (oget current-parent :endIndex)]
    #js{:src (obu/str-insert 
              (obu/str-remove src current-parent-start current-parent-end) 
              (oget current-node :text) 
              current-parent-start)}))