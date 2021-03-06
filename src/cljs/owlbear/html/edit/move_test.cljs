(ns owlbear.html.edit.move-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.html.edit.move :as html-move]
            [owlbear.html.parse.rules :as html-rules]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :refer-macros [&testing] :refer [noget+] :as obu]))

(defspec move-forward-spec 5
  (prop/for-all [{:keys [src
                         current-node-text
                         current-node-start
                         out-of-bounds-offset]} (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                                {:vector-gen-args [2 6]}})]
                                                  (let [root-node (noget+ tree :?rootNode)
                                                        {:keys [current-node
                                                                forward-object-node]} (->> root-node
                                                                                           obpr/node->descendants
                                                                                           rest
                                                                                           (filter html-rules/object-node)
                                                                                           shuffle
                                                                                           (some #(when-let [fon (html-rules/next-forward-object-node %)]
                                                                                                    {:current-node %
                                                                                                     :forward-object-node fon})))]
                                                    {:src (noget+ root-node :?text)
                                                     :current-node-start (noget+ current-node :?startIndex)
                                                     :current-node-text (noget+ current-node :?text)
                                                     :forward-object-node-start (noget+ forward-object-node :?startIndex)
                                                     :forward-object-node-text (noget+ forward-object-node :?text)
                                                     :out-of-bounds-offset (inc (noget+ root-node :?endIndex))}))]
    (&testing ""
      (&testing "when cursor out of bounds"
        (is (nil? (html-move/forward-move src out-of-bounds-offset))
            "no result"))
      (let [{result-offset :offset
             :as result} (html-move/forward-move src current-node-start)]
        (&testing "when cursor in bounds"
          (is (some? result)
              "move performed")
          (is (number? result-offset)
              "resulting offset is a number")
          (is (>= result-offset 0)
              "resulting offset is non-negative")
          (is (> result-offset current-node-start)
              "resulting offset is greater than original"))))))

(deftest forward-move-test
  (testing "when src is empty"
    (is (nil? (html-move/forward-move "" 0))
        "no result"))
  (testing "when root node"
    (is (nil? (:offset (html-move/forward-move "<div></div>" 0))))))
