(ns owlbear.ts.edit.raise-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.ts :as obgt-ts]
            [owlbear.parse.rules :as obpr]
            [owlbear.ts.edit.raise :as ts-raise]
            [owlbear.utilities :refer [noget+] :as obu :refer-macros [&testing]]))

(defspec raise-spec 5
  (prop/for-all [{:keys [src
                         in-bounds-offset
                         out-of-bounds-offset
                         ancestor-node-start-offset
                         current-node-text
                         current-node-start-index
                         current-node-end-index]} (gen/let [tree obgt-ts/tree-with-t-subject]
                                                    (let [root-node (noget+ tree :?rootNode)
                                                          {:keys [current-node
                                                                  current-ancestor-node]} (->> root-node
                                                                                               obpr/node->descendants
                                                                                               shuffle
                                                                                               (some #(ts-raise/raise-ctx % (inc (noget+ % :?startIndex)))))] 
                                                      (gen/let [in-bounds-offset (gen/elements
                                                                                  [(noget+ current-node :?startIndex)
                                                                                   (dec (noget+ current-node :?endIndex))])]
                                                        {:src (noget+ root-node :?text)
                                                         :ancestor-node-text (noget+ current-ancestor-node :?text)
                                                         :ancestor-node-start-offset (noget+ current-ancestor-node :?parent.?startIndex)
                                                         :in-bounds-offset in-bounds-offset
                                                         :out-of-bounds-offset (inc (noget+ root-node :?endIndex))
                                                         :current-node-text (noget+ current-node :?text)
                                                         :current-node-start-index (noget+ current-node :?startIndex)
                                                         :current-node-end-index (noget+ current-node :?endIndex)})))]
    (&testing ""
      (&testing "when cursor out of bounds"
        (is (nil? (ts-raise/raise src out-of-bounds-offset nil :tsx))
            "no result"))
      (let [{result-src :src
             result-offset :offset
             :as result} (ts-raise/raise src in-bounds-offset nil :tsx)]
        (&testing "when cursor in bounds"
          (is (map? result)
              "raise performed")
          (is (string? result-src)
              "resulting text is a string")
          (is (number? result-offset)
              "resulting offset is a number")
          (is (< (count result-src) (count src))
              "resulting text length is shorter")
          (is (<= result-offset in-bounds-offset)
              "resulting offset <=")
          (is (>= result-offset 0)
              "resulting offset is non-negative"))))))

(deftest raise-test
  (testing "when src is empty"
    (is (nil? (ts-raise/raise "" 0))
        "no result"))
  (testing "when root node"
    (is (nil? (ts-raise/raise "<div>hello</div>" 0 nil :tsx))
        "no result")))