(ns owlbear.parser.html.edit.slurp-test
  (:require  [cljs.test :refer [deftest is testing]]
             [clojure.string :as str]
             [clojure.test.check.clojure-test :refer [defspec]]
             [clojure.test.check.generators :as gen]
             [clojure.test.check.properties :as prop]
             [owlbear.generators.tree.html :as obgt-html]
             [owlbear.parser.html :as obp-html]
             [owlbear.parser.html.edit.slurp :as obp-html-slurp]
             [owlbear.parser.html.edit.rules :as obp-html-edit-rules]
             [owlbear.parser.utilities :as obpu]
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
                                                          forward-slurp-subjects (obp-html-slurp/node->forward-slurp-subjects root-node)]
                                                      (gen/let [current-node (gen/elements forward-slurp-subjects)]
                                                        {:src (noget+ root-node :?text)
                                                         :out-of-bounds-offset (inc (noget+ root-node :?endIndex))
                                                         :current-node-next-sibling-text (noget+ current-node :?nextSibling.?text)
                                                         :current-node-text (noget+ current-node :?text)
                                                         :current-node-start-index (noget+ current-node :?startIndex)
                                                         :current-node-end-index (noget+ current-node :?endIndex)})))]
    (testing "when cursor out of bounds"
      (is (nil? (obp-html-slurp/forward-slurp src out-of-bounds-offset))
          "no result"))
    (testing "when cursor at node start"
      (let [slurp-result (obp-html-slurp/forward-slurp src current-node-start-index)
            slurp-result-offset (noget+ slurp-result :?offset)
            slurp-result-src (noget+ slurp-result :?src)]
        (is (= (count src) (count slurp-result-src))
            "text length does not change")
        (is (= current-node-start-index slurp-result-offset)
            "cursor offset does not change")
        (let [slurp-result-current-node-text (-> slurp-result-src
                                                 obp-html/src->tree
                                                 (noget+ :?rootNode)
                                                 (obp-html-edit-rules/node->current-subject-nodes slurp-result-offset)
                                                 last
                                                 (noget+ :?text))]
          (is (str/includes? slurp-result-current-node-text current-node-next-sibling-text)
              "slurped former forward sibling"))))
    (testing "when cursor offset is at node end"
      (let [cursor-offset (dec current-node-end-index)
            slurp-result (obp-html-slurp/forward-slurp src cursor-offset)
            slurp-result-offset (noget+ slurp-result :?offset)
            slurp-result-src (noget+ slurp-result :?src)]
        (is (= (count src) (count slurp-result-src))
            "text length does not change")
        (is (< cursor-offset slurp-result-offset)
            "cursor offset is moved forward")
        (let [slurp-result-current-node-text (-> slurp-result-src
                                                 obp-html/src->tree
                                                 (noget+ :?rootNode)
                                                 (obp-html-edit-rules/node->current-subject-nodes slurp-result-offset)
                                                 last
                                                 (noget+ :?text))]
          (is (str/includes? slurp-result-current-node-text current-node-next-sibling-text)
              "slurped former forward sibling"))))))

(deftest forward-slurp-test
  (testing "when src is empty"
    (is (nil? (obp-html-slurp/forward-slurp "" 0))
        "no result")))