(ns owlbear.ts.edit.delete-test
  (:require [cljs.test :refer [is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.ts :as obgt-ts]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.ts.edit.delete :as ts-delete]
            [owlbear.utilities :as obu :refer-macros [&testing]]
            [owlbear.parse :as obp]))

(defspec backward-delete-spec 5
  (prop/for-all [{:keys [src
                         current-node-start-index
                         current-node-end-index]} (gen/let [src (gen/no-shrink obgt-ts/empty-subject)]
                                                    (let [tree (obp/src->tree! src obp/tsx-lang-id)
                                                          root-node (obu/noget+ tree :?rootNode)
                                                          [backward-delete-target] (obpr/filter-descendants
                                                                                    #(and (ts-rules/subject-node %)
                                                                                          (ts-rules/empty-node? %))
                                                                                    root-node)]
                                                      {:src src
                                                       :current-node-text (obu/noget+ backward-delete-target :?text)
                                                       :current-node-start-index (obu/noget+ backward-delete-target :?startIndex)
                                                       :current-node-end-index (obu/noget+ backward-delete-target :?endIndex)}))]
    (let [{result-src :src
           result-offset :offset
           :keys [delete-start-offset delete-end-offset]
           :as delete-result} (ts-delete/backward-delete src (inc current-node-start-index) nil :tsx)]
      (&testing "when cursor is at node start"
        (is (< 1 (- delete-end-offset delete-start-offset))
            "more than one char is deleted")
        (is (> (count src) (count result-src))
            "source length is reduced")
        (is (contains? #{0 current-node-start-index} result-offset)
            "offset is moved to node start or src start")))
    (let [{result-src :src
           result-offset :offset
           :keys [delete-start-offset delete-end-offset]
           :as delete-result} (ts-delete/backward-delete src current-node-end-index nil :tsx)]
      (&testing "when cursor offset is at node end"
        (is (< 1 (- delete-end-offset delete-start-offset))
            "more than one char is deleted")
        (is (> (count src) (count result-src))
            "source length is reduced")
        (is (contains? #{0 current-node-start-index} result-offset)
            "offset is moved to node start or src start")))))
