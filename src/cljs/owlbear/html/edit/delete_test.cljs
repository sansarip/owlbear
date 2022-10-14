(ns owlbear.html.edit.delete-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.generators.utilities :as obgu]
            [owlbear.parse.rules :as obpr]
            [owlbear.html.parse.rules :as html-rules]
            [owlbear.html.edit.delete :as html-delete]
            [owlbear.utilities :refer-macros [&testing] :refer [noget+]]))

(defspec backward-delete-spec 10
  (prop/for-all [{:keys [src
                         out-of-bounds-offset
                         current-node-text
                         current-node-start-index
                         current-node-end-index]} (gen/let [tree (obgt-html/tree {:hiccup-gen-opts {:vector-gen-args [0]
                                                                                                    :tag-name-gen (gen/let [s1 obgu/string-alphanumeric-starts-with-alpha
                                                                                                                            s2 gen/string-alphanumeric]
                                                                                                                    (str s1 s2))
                                                                                                    :props-gen (gen/return {})}})]
                                                    (let [root-node (noget+ tree :?rootNode)
                                                          backward-delete-targets (obpr/filter-descendants html-rules/tag-node root-node)]
                                                      (gen/let [current-node (gen/elements backward-delete-targets)]
                                                        (let [current-tag-name (obpr/some-child-node html-rules/tag-name-node current-node)]
                                                          {:src (noget+ root-node :?text)
                                                           :current-tag-name-start (noget+ current-tag-name :?startIndex)
                                                           :current-tag-name-end (noget+ current-tag-name :?endIndex)
                                                           :current-node-text (noget+ current-node :?text)
                                                           :current-node-start-index (noget+ current-node :?startIndex)
                                                           :current-node-end-index (noget+ current-node :?endIndex)}))))]
    (let [{result-src :src
           result-offset :offset
           :as delete-result} (html-delete/backward-delete src (inc current-node-start-index))]
      (&testing "when cursor is at node start"
        (is (empty? result-src)
            "node is deleted")
        (is (= 0 result-offset)
            "offset is moved to node start")))
    (let [{result-src :src
           result-offset :offset
           :as delete-result} (html-delete/backward-delete src current-node-end-index)]
      (&testing "when cursor offset is at node end"
        (is (empty? result-src)
            "node is deleted")
        (is (= 0 result-offset)
            "offset is moved to node start")))
    (let [{result-src :src
           result-offset :offset
           :as delete-result} (html-delete/backward-delete src current-node-end-index)]
      (&testing "when cursor offset is at node end"
        (is (empty? result-src)
            "node is deleted")
        (is (= 0 result-offset)
            "offset is moved to node start")))))