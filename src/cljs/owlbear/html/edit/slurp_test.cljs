(ns owlbear.html.edit.slurp-test
  (:require  [cljs.test :refer [deftest is testing]]
             [clojure.string :as str]
             [clojure.test.check.clojure-test :refer [defspec]]
             [clojure.test.check.generators :as gen]
             [clojure.test.check.properties :as prop]
             [owlbear.generators.tree.html :as obgt-html]
             [owlbear.parse :as obp]
             [owlbear.html.parse.rules :as ob-html-rules]
             [owlbear.html.edit.slurp :as obp-slurp]
             [owlbear.utilities :refer [noget+]]))

(defspec forward-slurp-spec 5
  (prop/for-all [{:keys [src
                         out-of-bounds-offset
                         current-node-next-sibling-text
                         current-node-start-index
                         current-node-end-index]} (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                                  {:vector-gen-args [2 6]
                                                                                   :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                                    (let [root-node (noget+ tree :?rootNode)
                                                          forward-slurp-subjects (obp-slurp/node->forward-slurp-subjects root-node)]
                                                      forward-slurp-subjects
                                                      (gen/let [current-node (gen/elements forward-slurp-subjects)]
                                                        {:src (noget+ root-node :?text)
                                                         :out-of-bounds-offset (inc (noget+ root-node :?endIndex))
                                                         :current-node-next-sibling-text (noget+ current-node :?nextSibling.?text)
                                                         :current-node-text (noget+ current-node :?text)
                                                         :current-node-start-index (noget+ current-node :?startIndex)
                                                         :current-node-end-index (noget+ current-node :?endIndex)})))]
    (testing "when cursor out of bounds"
      (is (nil? (obp-slurp/forward-slurp src out-of-bounds-offset))
          "no result"))
    (testing "when cursor at node start"
      (let [{result-src :src
             result-offset :offset
             :as slurp-result} (obp-slurp/forward-slurp src current-node-start-index)]
        (is (= (count src) (count result-src))
            "text length does not change")
        (is (= current-node-start-index result-offset)
            "cursor offset does not change")
        (let [slurp-result-current-node-text (-> result-src
                                                 (obp/src->tree :html)
                                                 (noget+ :?rootNode)
                                                 (ob-html-rules/node->current-subject-nodes result-offset)
                                                 last
                                                 (noget+ :?text))]
          (is (str/includes? slurp-result-current-node-text current-node-next-sibling-text)
              "slurped former forward sibling"))))
    (testing "when cursor offset is at node end"
      (let [cursor-offset (dec current-node-end-index)
            {result-src :src
             result-offset :offset
             :as slurp-result} (obp-slurp/forward-slurp src cursor-offset)]
        (is (= (count src) (count result-src))
            "text length does not change")
        (is (< cursor-offset result-offset)
            "cursor offset is moved forward")
        (let [slurp-result-current-node-text (-> result-src
                                                 (obp/src->tree :html)
                                                 (noget+ :?rootNode)
                                                 (ob-html-rules/node->current-subject-nodes result-offset)
                                                 last
                                                 (noget+ :?text))]
          (is (str/includes? slurp-result-current-node-text current-node-next-sibling-text)
              "slurped former forward sibling"))))))

(deftest forward-slurp-test
  (testing "when src is empty"
    (is (nil? (obp-slurp/forward-slurp "" 0))
        "no result")))