(ns owlbear.html.edit.kill-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.html.edit.kill :as html-kill]
            [owlbear.html.parse.rules :as html-rules]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :refer-macros [&testing] :refer [noget+] :as obu]))

(defspec kill-spec 5
  (prop/for-all [{:keys [src
                         current-node-text
                         in-bounds-offset
                         out-of-bounds-offset]} (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                                {:vector-gen-args [1 6]}})]
                                                  (let [root-node (noget+ tree :?rootNode)
                                                        current-node (->> root-node
                                                                          obpr/node->descendants
                                                                          rest
                                                                          shuffle
                                                                          (some html-rules/object-node))]
                                                    (gen/let [in-bounds-offset (gen/choose
                                                                                (noget+ current-node :?startIndex)
                                                                                (dec (noget+ current-node :?endIndex)))]
                                                      {:src (noget+ root-node :?text)
                                                       :current-node current-node
                                                       :current-node-text (noget+ current-node :?text)
                                                       :in-bounds-offset in-bounds-offset
                                                       :out-of-bounds-offset (inc (noget+ root-node :?endIndex))})))]
    (&testing "when cursor out of bounds"
      (is (nil? (html-kill/kill src out-of-bounds-offset))
          "no result"))
    (let [{result-offset :offset
           result-src :src
           :as result} (html-kill/kill src in-bounds-offset)]
      (&testing "when cursor in bounds"
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
    (is (nil? (html-kill/kill "" 0))
        "no result"))
  (testing "when root node"
    (let [src "<div></div>"]
      (is (= {:src ""
              :offset 0
              :removed-text src}
             (html-kill/kill src 0))))))
