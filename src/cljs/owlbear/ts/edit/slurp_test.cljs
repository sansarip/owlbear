(ns owlbear.ts.edit.slurp-test
  (:require  [clojure.test :refer [deftest is]]
             [clojure.test.check.clojure-test :refer [defspec]]
             [clojure.test.check.generators :as gen]
             [clojure.test.check.properties :as prop]
             [owlbear.generators.tree.ts :as obgt-ts]
             [owlbear.ts.edit.slurp :as ob-ts-slurp]
             [owlbear.utilities :as obu :refer-macros [&testing]]))

(defspec forward-slurp-spec 5
  (prop/for-all [{:keys [src
                         out-of-bounds-offset
                         current-node-start-index
                         current-node-end-index]} (gen/let [tree obgt-ts/tree-with-t-subject]
                                                    (let [root-node (obu/noget+ tree :?rootNode)
                                                          forward-slurp-subjects (ob-ts-slurp/node->forward-slurp-subjects root-node)]
                                                      (gen/let [current-node (gen/elements forward-slurp-subjects)]
                                                        {:src (obu/noget+ root-node :?text)
                                                         :out-of-bounds-offset (inc (obu/noget+ root-node :?endIndex))
                                                         :current-node-text (obu/noget+ current-node :?text)
                                                         :current-node-start-index (obu/noget+ current-node :?startIndex)
                                                         :current-node-end-index (obu/noget+ current-node :?endIndex)})))]
    (letfn [(common-assertions [result result-src result-offset]
              (&testing ""
                (is (map? result)
                    "result-map returned")
                (is (string? result-src)
                    "result src is a string")
                (is (not-empty result-src)
                    "result src is not empty")
                (is (number? result-offset)
                    "result offset is a number")))]
      (&testing "when TSX forward slurp"
        (&testing "and cursor out of bounds"
          (is (nil? (ob-ts-slurp/forward-slurp src out-of-bounds-offset :tsx))
              "no result"))
        (&testing "and cursor at node start"
          (let [{result-src :src
                 result-offset :offset
                 :as result} (ob-ts-slurp/forward-slurp src current-node-start-index :tsx)]
            (common-assertions result result-src result-offset)
            (is (= current-node-start-index result-offset)
                "cursor offset does not change")
            (is (= (subs src 0 current-node-start-index)
                   (subs result-src 0 result-offset))
                "text up to cursor offset matches in original/result source")))
        (&testing "and cursor offset is at node end"
          (let [cursor-offset (dec current-node-end-index)
                {result-src :src
                 result-offset :offset
                 :as result} (ob-ts-slurp/forward-slurp src cursor-offset :tsx)]
            (common-assertions result result-src result-offset)))))))

(deftest forward-slurp-test
  (&testing "when src is empty"
    (is (nil? (ob-ts-slurp/forward-slurp "" 0))
        "no result")
    (is (nil? (ob-ts-slurp/forward-slurp "" 0 :tsx))
        "no result")))