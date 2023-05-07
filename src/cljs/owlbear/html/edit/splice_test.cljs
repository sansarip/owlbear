(ns owlbear.html.edit.splice-test
  (:require [cljs.test :refer [is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.html.parse.rules :as html-rules]
            [owlbear.parse.rules :as obpr]
            [owlbear.html.edit.splice :as html-splice]
            [owlbear.utilities :refer-macros [&testing] :refer [noget+]]))

(defspec splice-spec 5
  (prop/for-all [{:keys [src
                         in-bounds-offset
                         out-of-bounds-offset
                         parent-node-start-offset
                         current-node-text
                         current-node-start-index
                         current-node-end-index]} (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                                  {:vector-gen-args [1 6]
                                                                                   :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                                    (let [root-node (noget+ tree :?rootNode)]
                                                      (gen/let [current-node (gen/elements
                                                                              (filter #(and (html-rules/subject-node %)
                                                                                            ((complement html-rules/empty-node?) %))
                                                                                      (obpr/node->descendants root-node)))
                                                                in-bounds-offset (gen/elements (range (noget+ current-node :?startIndex) (noget+ current-node :?endIndex)))]
                                                        {:src (noget+ root-node :?text)
                                                         :parent-node-text (noget+ current-node :?parent.?text)
                                                         :parent-node-start-offset (noget+ current-node :?parent.?startIndex)
                                                         :in-bounds-offset in-bounds-offset
                                                         :out-of-bounds-offset (inc (noget+ root-node :?endIndex))
                                                         :current-node-text (noget+ current-node :?text)
                                                         :current-node-start-index (noget+ current-node :?startIndex)
                                                         :current-node-end-index (noget+ current-node :?endIndex)})))]
    (&testing ""
      (&testing "when cursor out of bounds"
        (is (nil? (html-splice/splice src out-of-bounds-offset))
            "no result"))
      (let [{result-src :src
             result-offset :offset
             :as result} (html-splice/splice src in-bounds-offset)]
        (&testing "when cursor in bounds"
          (is (map? result)
              "splice performed")
          (is (string? result-src)
              "resulting text is a string")
          (is (number? result-offset)
              "resulting offset is a number")
          (is (< (count result-src) (count src))
              "resulting text length is shorter")
          (is (<= current-node-start-index result-offset current-node-end-index)
              "resulting offset is smaller")
          (is (>= result-offset 0)
              "resulting offset is non-negative"))))))