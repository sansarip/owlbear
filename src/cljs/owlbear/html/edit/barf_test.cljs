(ns owlbear.html.edit.barf-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.parse :as obp]
            [owlbear.html.parse.rules :as ob-html-rules]
            [owlbear.html.edit.barf :as obp-barf]
            [owlbear.utilities :refer [noget+]]))

(defspec forward-barf-spec 10
  (prop/for-all [{:keys [src
                         out-of-bounds-offset
                         current-node-text
                         current-node-start-index
                         current-node-end-index]} (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                                  {:vector-gen-args [1 6]
                                                                                   :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                                    (let [root-node (noget+ tree :?rootNode)
                                                          forward-barf-subjects (obp-barf/node->forward-barf-subjects root-node)]
                                                      (gen/let [current-node (gen/elements forward-barf-subjects)]
                                                        {:src (noget+ root-node :?text)
                                                         :out-of-bounds-offset (inc (noget+ root-node :?endIndex))
                                                         :current-node-text (noget+ current-node :?text)
                                                         :current-node-start-index (noget+ current-node :?startIndex)
                                                         :current-node-end-index (noget+ current-node :?endIndex)})))]

    (testing "when cursor out of bounds"
      (is (nil? (obp-barf/forward-barf src out-of-bounds-offset))
          "no result"))
    (let [common-assertions (fn [result-src result-offset]
                              (is (= (count src) (count result-src))
                                  "src text length does not change")
                              (is (not= src result-src)
                                  "barfed src is different")
                              (is (> (count current-node-text)
                                     (-> result-src
                                         (obp/src->tree :html)
                                         (noget+ :?rootNode)
                                         (ob-html-rules/node->current-subject-nodes result-offset)
                                         last
                                         (noget+ :?text)
                                         count))
                                  "current node is smaller after barf"))]
      (testing "when cursor at node start"
        (let [{result-src :src
               result-offset :offset
               :as barf-result} (obp-barf/forward-barf src current-node-start-index)]
          (is (= current-node-start-index result-offset)
              "cursor offset does not change")
          (common-assertions result-src result-offset)))
      (testing "when cursor offset is at node end"
        (let [cursor-offset (dec current-node-end-index)
              {result-src :src
               result-offset :offset
               :as barf-result} (obp-barf/forward-barf src cursor-offset)]
          (is (> cursor-offset result-offset)
              "cursor offset is moved backward")
          (common-assertions result-src result-offset))))))

(deftest forward-barf-test
  (testing "when src is empty"
    (is (nil? (obp-barf/forward-barf "" 0))
        "no result")))