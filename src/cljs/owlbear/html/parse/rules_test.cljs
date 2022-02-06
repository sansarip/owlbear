(ns owlbear.html.parse.rules-test
  (:require [cljs.test :refer [is testing]]
            [clojure.string :as str]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.html.parse.rules :as obp-html-rules]
            [owlbear.utilities :refer [noget+]]))

(defspec node->end-tag-node-test 10
  (prop/for-all [node (gen/one-of
                       [obgt-html/comment-node
                        (obgt-html/element-node*
                         {:hiccup-gen-opts
                          {:tag-name-gen obgt-html/container-tag-name}})])]
    (let [end-tag-node (obp-html-rules/node->end-tag-node node)]
      (testing "when end-tag is found"
        (is (some? end-tag-node)
            "end-tag is not nil")
        (is (= (noget+ node :?endIndex)
               (noget+ end-tag-node :?endIndex))
            "end-tag ends at node's end")
        (is (str/ends-with? (noget+ node :?text)
                            (noget+ end-tag-node :?text))
            "node text ends with end-tag text")))))

(defspec node->start-tag-node-test 10
  (prop/for-all [node (gen/one-of
                       [obgt-html/comment-node
                        (obgt-html/element-node*
                         {:hiccup-gen-opts
                          {:tag-name-gen obgt-html/container-tag-name}})])]
    (let [start-tag-node (obp-html-rules/node->start-tag-node node)]
      (testing "when start-tag is found"
        (is (some? start-tag-node)
            "start-tag is not nil")
        (is (= (noget+ node :?startIndex)
               (noget+ start-tag-node :?startIndex))
            "start-tag starts at node's start")
        (is (str/starts-with? (noget+ node :?text)
                              (noget+ start-tag-node :?text))
            "node text starts with start-tag text")))))