(ns owlbear.html.edit.kill-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.html.edit.kill :as ob-html-kill]
            [owlbear.html.parse.rules :as ob-html-rules]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :refer [noget+] :as obu]))

(defspec kill-spec 5
  (prop/for-all [{:keys [src
                         current-node-text
                         in-bounds-offset
                         out-of-bounds-offset]} (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                                {:vector-gen-args [1 6]}})]
                                                  (let [root-node (noget+ tree :?rootNode)
                                                        root-node-text (noget+ root-node :?text)
                                                        object-nodes (->> root-node
                                                                          obpr/flatten-children
                                                                          (filter #(and (ob-html-rules/object-node %)
                                                                                        (not= (noget+ % :?text) root-node-text))))]
                                                    (gen/let [current-node (gen/elements object-nodes)
                                                              in-bounds-offset (gen/elements
                                                                                [(noget+ current-node :?startIndex)
                                                                                 (dec (noget+ current-node :?endIndex))])]
                                                      {:src root-node-text
                                                       :current-node current-node
                                                       :current-node-text (noget+ current-node :?text)
                                                       :in-bounds-offset in-bounds-offset
                                                       :out-of-bounds-offset (inc (noget+ root-node :?endIndex))})))]
    (testing "when cursor out of bounds"
      (is (nil? (ob-html-kill/kill src out-of-bounds-offset))
          "no result"))
    (testing "when cursor in bounds"
      (let [{result-offset :offset
             result-src :src
             :as result} (ob-html-kill/kill src in-bounds-offset)]
        (is (some? result)
            "kill performed")
        (is (string? result-src)
            "resulting text is a string")
        (is (number? result-offset)
            "resulting offset is a number")
        (is (>= result-offset 0)
            "resulting offset is non-negative")
        (is (< (count result-src) (count src))
            "resulting text length is shorter")
        (is (<= result-offset in-bounds-offset)
            "resulting offset is not greater")
        (is (not= src result-src)
            "result src does not equal original")))))

(deftest kill-test
  (testing "when src is empty"
    (is (nil? (ob-html-kill/kill "" 0))
        "no result"))
  (testing "when root node"
    (is (= {:src "" :offset 0} (ob-html-kill/kill "<div></div>" 0)))))
