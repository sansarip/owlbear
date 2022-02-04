(ns owlbear.parser.utilities-test
  (:require [cljs.test :refer [deftest is testing]]
            [cljs-bean.core :refer [->clj]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [oops.core :refer [oget]]
            [owlbear.generators.tree :as obgt]
            [owlbear.generators.utilities :as obgu]
            [owlbear.parser.utilities :as obpu]))

(defspec valid-range-in-node-test 10
  (prop/for-all [{:keys [node
                         range-start
                         range-stop]} (gen/let [[node-start
                                                 range-start
                                                 range-stop
                                                 node-stop] (obgu/vector-distinct-sorted obgu/large-pos-integer {:num-elements 4})]
                                        {:node #js {:startIndex node-start
                                                    :endIndex node-stop}
                                         :range-start range-start
                                         :range-stop range-stop})]
    (testing "when given start offset only"
      (is (obpu/range-in-node? node range-start)
          "start offset in node's bounds"))
    (testing "when given start and stop offsets"
      (is (obpu/range-in-node? node range-start range-stop)
          "range is in node's bounds"))))

(defspec invalid-range-not-in-node-test 10
  (prop/for-all [{:keys [node
                         range-start
                         range-stop]} (gen/let [[node-start
                                                 range-start
                                                 range-stop] (obgu/vector-distinct-sorted obgu/large-pos-integer {:num-elements 4})]
                                        {:node #js {:startIndex node-start
                                                    ;; End index is not in range
                                                    :endIndex range-start}
                                         :range-start range-start
                                         :range-stop range-stop})]
    (testing "when given start offset onlu"
      (is (not (obpu/range-in-node? node range-start))
          "start offset is out of node's bounds"))
    (testing "when given start and stop offsets"
      (is (not (obpu/range-in-node? node range-start range-stop))
          "range is not in node's bounds"))))

(defspec node->current-nodes 10
  (prop/for-all [{:keys [node
                         out-of-bounds-offset
                         in-bounds-offset]} (gen/let [node obgt/root-node]
                                              (let [{:keys [startIndex endIndex]} (->clj node)]
                                                (gen/let [cursor-offset (gen/choose startIndex (dec endIndex))
                                                          addend obgu/large-pos-integer]
                                                  {:node node
                                                   :out-of-bounds-offset (+ endIndex (inc addend))
                                                   :in-bounds-offset cursor-offset})))]
    (testing "when offset in node"
      (let [current-nodes (obpu/node->current-nodes node in-bounds-offset)]
        (is (not-empty current-nodes)
            "found current nodes")
        (is (apply <= (map #(oget % :startIndex) current-nodes))
            "current nodes are sorted from least to most specific")))
    (testing "when offset not in nodes"
      (is (empty? (obpu/node->current-nodes node out-of-bounds-offset))
          "no current nodes"))))
