(ns owlbear.html.edit.raise-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.html.edit.raise :as ob-html-raise]
            [owlbear.parse :as ob-html]
            [owlbear.html.parse.rules :as ob-html-rules]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :refer [noget+]]))

(defspec raise-spec 5
  (prop/for-all [{:keys [src
                         in-bounds-offset
                         out-of-bounds-offset
                         parent-node-start-offset
                         current-node-text
                         current-node-start-index
                         current-node-end-index]} (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                                  {:vector-gen-args [1 6]
                                                                                   :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                                    (let [root-node (noget+ tree :?rootNode)
                                                          raise-subjects (filter (comp #(noget+ % :?parent.?parent) ob-html-rules/object-node)
                                                                                 (obpr/flatten-children root-node))]
                                                      (gen/let [current-node (gen/elements raise-subjects)
                                                                in-bounds-offset (gen/elements [(noget+ current-node :?startIndex) (dec (noget+ current-node :?endIndex))])]
                                                        {:src (noget+ root-node :?text)
                                                         :parent-node-text (noget+ current-node :?parent.?text)
                                                         :parent-node-start-offset (noget+ current-node :?parent.?startIndex)
                                                         :in-bounds-offset in-bounds-offset
                                                         :out-of-bounds-offset (inc (noget+ root-node :?endIndex))
                                                         :current-node-text (noget+ current-node :?text)
                                                         :current-node-start-index (noget+ current-node :?startIndex)
                                                         :current-node-end-index (noget+ current-node :?endIndex)})))]
    (testing "when cursor out of bounds"
      (is (nil? (ob-html-raise/raise src out-of-bounds-offset))
          "no result"))
    (testing "when cursor in bounds"
      (let [{result-src :src
             result-offset :offset
             :as result} (ob-html-raise/raise src in-bounds-offset)]
        (is (some? result)
            "raise performed")
        (is (string? result-src)
            "resulting text is a string")
        (is (number? result-offset)
            "resulting offset is a number")
        (is (< (count result-src) (count src))
            "resulting text length is shorter")
        (is (< result-offset in-bounds-offset)
            "resulting offset is smaller")
        (is (>= result-offset 0)
            "resulting offset is non-negative")
        (is (= (-> result-src
                   (ob-html/src->tree :html)
                   (noget+ :?rootNode)
                   (ob-html-rules/node->current-object-nodes parent-node-start-offset)
                   last
                   (noget+ :?text))
               current-node-text)
            "parent start offset now points to current node")))))

(deftest raise-test
  (testing "when src is empty"
    (is (nil? (ob-html-raise/raise "" 0))
        "no result"))
  (testing "when root node"
    (is (nil? (ob-html-raise/raise "<div>hello</div>" 0))
        "no result")))