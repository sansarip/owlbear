(ns owlbear.ts.edit.barf-test
  (:require [cljs.test :refer [deftest is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.ts :as obgt-ts]
            [owlbear.ts.edit.barf :as ob-ts-barf]
            [owlbear.utilities :as obu :refer-macros [&testing]]))

(defspec forward-barf-spec 10
  (prop/for-all [{:keys [src
                         out-of-bounds-offset
                         current-node-text
                         current-node-start-index
                         current-node-end-index]} (gen/let [tree obgt-ts/tree-with-t-subject]
                                                    (let [root-node (obu/noget+ tree :?rootNode)
                                                          forward-barf-subjects (ob-ts-barf/node->forward-barf-subjects root-node)]
                                                      (gen/let [current-node (gen/elements forward-barf-subjects)]
                                                        {:src (obu/noget+ root-node :?text)
                                                         :out-of-bounds-offset (inc (obu/noget+ root-node :?endIndex))
                                                         :current-node-text (obu/noget+ current-node :?text)
                                                         :current-node-start-index (obu/noget+ current-node :?startIndex)
                                                         :current-node-end-index (obu/noget+ current-node :?endIndex)})))]

    (&testing "when TSX forward slurp"
      (&testing "and cursor out of bounds"
        (is (nil? (ob-ts-barf/forward-barf src out-of-bounds-offset))
            "no result"))
      (letfn [(common-assertions [result result-src result-offset]
                (and (is (map? result)
                         "result-map returned")
                     (is (string? result-src)
                         "result src is a string")
                     (is (not-empty result-src)
                         "result src is not empty")
                     (is (number? result-offset)
                         "result offset is a number")
                     (is (not= src result-src)
                         "barfed src is different")))]
        (&testing "and cursor at node start"
          (let [{result-src :src
                 result-offset :offset
                 :as result} (ob-ts-barf/forward-barf src current-node-start-index)]
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
                 :as result} (ob-ts-barf/forward-barf src cursor-offset)]
            (common-assertions result result-src result-offset)))))))

(deftest forward-barf-test
  (&testing "when src is empty"
    (is (nil? (ob-ts-barf/forward-barf "" 0))
        "no result")))