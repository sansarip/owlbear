(ns owlbear.html.edit.rules-test
  (:require [cljs.pprint :refer [pprint]]
            [cljs.test :as t :refer [deftest is testing]]
            [cljs-bean.core :refer [bean]]
            [clojure.string :as str]
            [clojure.test.check.clojure-test :as tc :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.generators.utilities :as obgu]
            [owlbear.html.edit.rules :as obp-html-edit-rules]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :refer [noget+]]))

(defspec subject-node-spec 10
  (prop/for-all [node obgt-html/subject-node]
    (testing "when subject node"
      (is (= node (obp-html-edit-rules/subject-node node))
          "input node should equal returned node"))))

(defspec self-closing-element-not-subjectifiable-spec 10
  (prop/for-all [self-closing-node (obgt-html/element-node*
                                    {:hiccup-gen-opts
                                     {:vector-gen-args [0]
                                      :tag-name-gen (gen/fmap str/upper-case obgu/string-alphanumeric-starts-with-alpha)}})]
    (testing "when self-closing node"
      (is (nil? (obp-html-edit-rules/subject-node self-closing-node))
          "subject node not found"))))

(deftest subject-node-test
  (testing "when invalid type"
    (is (nil? (obp-html-edit-rules/subject-node #js {:type (str (random-uuid))}))
        "subject node not found")))

(defspec node->current-subject-nodes-spec 10
  (prop/for-all [[root-node :as children] (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                          {:vector-gen-args [2 6]
                                                                           :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                            (obpr/flatten-children (noget+ tree :?rootNode)))]
    (let [current-subject-nodes (some #(obp-html-edit-rules/node->current-subject-nodes % (noget+ % :?startIndex))
                                      children)]
      (testing "when forward object node is found"
        (is (not-empty current-subject-nodes)
            "found current subject nodes")
        (is (every? obp-html-edit-rules/subject-node current-subject-nodes)
            "every node is a subject node")
        (is (reduce < (map #(noget+ % :?startIndex) current-subject-nodes))
            "nodes sorted from least to most specific")))))

(deftest node->current-subject-nodes-test
  (testing "when node has no subject-node children"
    (is (empty? (obp-html-edit-rules/node->current-subject-nodes #js {:children #js [#js {:type (str (random-uuid))}]} 0))
        "invalid child node type")
    (is (empty? (obp-html-edit-rules/node->current-subject-nodes #js {} 0))
        "no children")))

(defspec object-node-spec 10
  (prop/for-all [node obgt-html/object-node]
    (testing "when object node"
      (is (= node (obp-html-edit-rules/object-node node))
          "input node should equal returned node"))))

(defspec whitespace-text-not-objectifiable-spec 10
  (prop/for-all [text-node (obgt-html/text-node* [" " "\n"])]
    (testing "when text node contains only whitespace"
      (is (nil? (obp-html-edit-rules/object-node text-node))
          "object node is not found"))))

(deftest object-node-test
  (testing "when invalid node type"
    (is (nil? (obp-html-edit-rules/object-node #js {:type (str (random-uuid))}))
        "object node is not found")))

(defspec next-object-node-spec 10
  (prop/for-all [children (gen/let [tree (obgt-html/tree {:hiccup-gen-opts {:vector-gen-args [2 6]}})]
                            (obpr/flatten-children (noget+ tree :?rootNode)))]
    (let [[slurp-anchor-node
           forward-object-node] (some #(when-let [fsn (obp-html-edit-rules/next-forward-object-node %)]
                                         [% fsn])
                                      children)]
      (testing "when forward-object node is found"
        (is (some? forward-object-node)
            "forward-object node is not nil")
        (is (obp-html-edit-rules/object-node forward-object-node)
            "forward-object node is actually object")
        (is (< (noget+ slurp-anchor-node :?startIndex) (noget+ forward-object-node :?startIndex))
            "forward-object node is positionally ahead")))))

(deftest next-forward-object-node-test
  (testing "when next node is not objectifiable"
    (is (nil? (obp-html-edit-rules/next-forward-object-node #js {:nextSibling #js {:type (str (random-uuid))}}))
        "invalid sibling node type")
    (is (nil? (obp-html-edit-rules/next-forward-object-node #js {}))
        "no siblings")))

(defspec node->current-forward-object-ctx-spec 10
  (prop/for-all [[root-node :as children] (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                          {:vector-gen-args [2 6]
                                                                           :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                            (obpr/flatten-children (noget+ tree :?rootNode)))]
    (let [{:keys [forward-object-node
                  current-node]} (some #(obp-html-edit-rules/node->current-forward-object-ctx % (noget+ % :?startIndex))
                                       children)]
      (testing "when forward-object node is found"
        (is (some? forward-object-node)
            "forward-object node is not nil")
        (is (obp-html-edit-rules/object-node forward-object-node)
            "forward-object node is actually objectifiable")
        (is (obp-html-edit-rules/subject-node current-node)
            "current node is subjectifiable")
        (is (< (noget+ current-node :?startIndex) (noget+ forward-object-node :?startIndex))
            "forward-object node is positionally ahead")
        (is (obpr/some-forward-sibling-node #(= forward-object-node %) current-node)
            "forward-object node is a forward sibling of the current node")))))

(defspec node->current-last-child-object-ctx-spec 10
  (prop/for-all [parent-node (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                             {:vector-gen-args [2 6]
                                                              :tag-name-gen obgt-html/html-element-container-tag-name}})]
                               (noget+ tree :?rootNode))]
    (let [{:keys [last-child-object-node
                  current-node]} (obp-html-edit-rules/node->current-last-child-object-ctx
                                  parent-node
                                  (noget+ parent-node :?startIndex))]
      (testing "when last-child-object node is found"
        (is (some? last-child-object-node)
            "last-child-object node is not nil")
        (is (obp-html-edit-rules/object-node last-child-object-node)
            "last-child-object node is actually objectifiable")
        (is (obp-html-edit-rules/subject-node current-node)
            "current node is subjectifiable")
        (is (obpr/range-in-node? current-node
                                 (noget+ last-child-object-node :?startIndex)
                                 (dec (noget+ last-child-object-node :?endIndex)))
            "last-child-object node is positionally within the current-node")
        (is (obpr/some-child-node #(= last-child-object-node %) current-node)
            "last-child-object node is a child of the current node")))))