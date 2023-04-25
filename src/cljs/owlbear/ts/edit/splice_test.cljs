(ns owlbear.ts.edit.splice-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.ts :as obgt-ts]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.edit.splice :as ts-splice]
            [owlbear.ts.parse.rules :as ts-rules]
            [owlbear.utilities :refer [noget+] :as obu :refer-macros [&testing]]))

(defspec splice-spec 5
  (prop/for-all [{:keys [src
                         in-bounds-offset
                         out-of-bounds-offset
                         current-node-text
                         current-node-start-index
                         current-node-end-index
                         container-node-end-index
                         container-node-start-index]} (gen/let [tree obgt-ts/tree-with-t-subject]
                                                        (let [root-node (noget+ tree :?rootNode)]
                                                          (gen/let [current-node (gen/elements
                                                                                  (->> root-node
                                                                                       obpr/node->descendants
                                                                                       (filter #(and (ts-rules/subject-node %)
                                                                                                     ((complement ts-rules/empty-node?) %)))))
                                                                    in-bounds-offset (gen/elements
                                                                                      [(noget+ current-node :?startIndex)
                                                                                       (dec (noget+ current-node :?endIndex))])]
                                                            (let [subject-container (ts-rules/subject-container-node current-node)]
                                                              {:src (noget+ root-node :?text)
                                                               :in-bounds-offset in-bounds-offset
                                                               :out-of-bounds-offset (inc (noget+ root-node :?endIndex))
                                                               :current-node-text (noget+ current-node :?text)
                                                               :current-node-start-index (noget+ current-node :?startIndex)
                                                               :current-node-end-index (noget+ current-node :?endIndex)
                                                               :container-node-text (noget+ subject-container :?text)
                                                               :container-node-start-index (noget+ subject-container :?startIndex)
                                                               :container-node-end-index (noget+ subject-container :?endIndex)}))))]
    (&testing ""
      (&testing "when cursor out of bounds"
        (is (nil? (ts-splice/splice src out-of-bounds-offset nil :tsx))
            "no result"))
      (let [{result-src :src
             result-offset :offset
             :as result} (ts-splice/splice src in-bounds-offset nil :tsx)]
        (&testing "when cursor in bounds"
          (is (map? result)
              "splice performed")
          (is (string? result-src)
              "resulting text is a string")
          (is (number? result-offset)
              "resulting offset is a number")
          (is (< (count result-src) (count src))
              "resulting text length is shorter")
          (is (<= container-node-start-index result-offset container-node-end-index)
              "resulting offset between start and end of subject container node")
          (is (>= result-offset 0)
              "resulting offset is non-negative"))))))

(deftest splice-test
  (testing "when src is empty"
    (is (nil? (ts-splice/splice "" 0))
        "no result"))
  (testing "when empty root node"
    (is (nil? (ts-splice/splice "<div></div>" 0 nil :tsx))
        "no result")))