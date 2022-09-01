(ns owlbear.html.parse.rules-test
  (:require [cljs.test :refer [deftest is testing]]
            [clojure.string :as str]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.html :as obgt-html]
            [owlbear.generators.utilities :as obgu]
            [owlbear.html.parse.rules :as html-rules]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :refer [noget+]]))

(defspec node->end-tag-node-test 10
  (prop/for-all [node (gen/one-of
                       [obgt-html/comment-node
                        (obgt-html/element-node*
                         {:hiccup-gen-opts
                          {:tag-name-gen obgt-html/container-tag-name}})])]
    (let [end-tag-node (html-rules/node->end-tag-node node)]
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
    (let [start-tag-node (html-rules/node->start-tag-node node)]
      (testing "when start-tag is found"
        (is (some? start-tag-node)
            "start-tag is not nil")
        (is (= (noget+ node :?startIndex)
               (noget+ start-tag-node :?startIndex))
            "start-tag starts at node's start")
        (is (str/starts-with? (noget+ node :?text)
                              (noget+ start-tag-node :?text))
            "node text starts with start-tag text")))))

(defspec subject-node-spec 10
  (prop/for-all [node obgt-html/subject-node]
    (testing "when subject node"
      (is (= node (html-rules/subject-node node))
          "input node should equal returned node"))))

(defspec self-closing-element-not-subjectifiable-spec 10
  (prop/for-all [self-closing-node (obgt-html/element-node*
                                    {:hiccup-gen-opts
                                     {:vector-gen-args [0]
                                      :tag-name-gen (gen/fmap str/upper-case obgu/string-alphanumeric-starts-with-alpha)}})]
    (testing "when self-closing node"
      (is (nil? (html-rules/subject-node self-closing-node))
          "subject node not found"))))

(deftest subject-node-test
  (testing "when invalid type"
    (is (nil? (html-rules/subject-node #js {:type (str (random-uuid))}))
        "subject node not found")))

(defspec node->current-subject-nodes-spec 10
  (prop/for-all [[root-node :as children] (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                          {:vector-gen-args [2 6]
                                                                           :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                            (obpr/node->descendants (noget+ tree :?rootNode)))]
    (let [current-subject-nodes (some #(html-rules/node->current-subject-nodes % (noget+ % :?startIndex))
                                      children)]
      (testing "when forward object node is found"
        (is (not-empty current-subject-nodes)
            "found current subject nodes")
        (is (every? html-rules/subject-node current-subject-nodes)
            "every node is a subject node")
        (is (reduce < (map #(noget+ % :?startIndex) current-subject-nodes))
            "nodes sorted from least to most specific")))))

(deftest node->current-subject-nodes-test
  (testing "when node has no subject-node children"
    (is (empty? (html-rules/node->current-subject-nodes #js {:children #js [#js {:type (str (random-uuid))}]} 0))
        "invalid child node type")
    (is (empty? (html-rules/node->current-subject-nodes #js {} 0))
        "no children")))

(defspec node->current-object-nodes-spec 10
  (prop/for-all [[root-node :as children] (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                          {:vector-gen-args [2 6]
                                                                           :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                            (obpr/node->descendants (noget+ tree :?rootNode)))]
    (let [current-object-nodes (some #(html-rules/node->current-object-nodes % (noget+ % :?startIndex))
                                      children)]
      (testing "when forward object node is found"
        (is (not-empty current-object-nodes)
            "found current object nodes")
        (is (every? html-rules/object-node current-object-nodes)
            "every node is an object node")
        (is (reduce < (map #(noget+ % :?startIndex) current-object-nodes))
            "nodes sorted from least to most specific")))))

(deftest node->current-object-nodes-test
  (testing "when node has no object-node children"
    (is (empty? (html-rules/node->current-object-nodes #js {:children #js [#js {:type (str (random-uuid))}]} 0))
        "invalid child node type")
    (is (empty? (html-rules/node->current-object-nodes #js {} 0))
        "no children")))

(defspec object-node-spec 10
  (prop/for-all [node obgt-html/object-node]
    (testing "when object node"
      (is (= node (html-rules/object-node node))
          "input node should equal returned node"))))

(defspec whitespace-text-not-objectifiable-spec 10
  (prop/for-all [text-node (obgt-html/text-node* [" " "\n"])]
    (testing "when text node contains only whitespace"
      (is (nil? (html-rules/object-node text-node))
          "object node is not found"))))

(deftest object-node-test
  (testing "when invalid node type"
    (is (nil? (html-rules/object-node #js {:type (str (random-uuid))}))
        "object node is not found")))

(defspec next-object-node-spec 10
  (prop/for-all [children (gen/let [tree (obgt-html/tree {:hiccup-gen-opts {:vector-gen-args [2 6]}})]
                            (obpr/node->descendants (noget+ tree :?rootNode)))]
    (let [[slurp-anchor-node
           forward-object-node] (some #(when-let [fsn (html-rules/node->forward-object-node %)]
                                         [% fsn])
                                      children)]
      (testing "when forward-object node is found"
        (is (some? forward-object-node)
            "forward-object node is not nil")
        (is (html-rules/object-node forward-object-node)
            "forward-object node is actually object")
        (is (< (noget+ slurp-anchor-node :?startIndex) (noget+ forward-object-node :?startIndex))
            "forward-object node is positionally ahead")))))

(deftest next-forward-object-node-test
  (testing "when next node is not objectifiable"
    (is (nil? (html-rules/node->forward-object-node #js {:nextSibling #js {:type (str (random-uuid))}}))
        "invalid sibling node type")
    (is (nil? (html-rules/node->forward-object-node #js {}))
        "no siblings")))

(defspec node->current-forward-object-ctx-spec 10
  (prop/for-all [[root-node :as children] (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                                          {:vector-gen-args [2 6]
                                                                           :tag-name-gen obgt-html/html-element-container-tag-name}})]
                                            (obpr/node->descendants (noget+ tree :?rootNode)))]
    (let [{:keys [forward-object-node
                  current-node]} (some #(html-rules/node->current-forward-object-ctx % (noget+ % :?startIndex))
                                       children)]
      (testing "when forward-object node is found"
        (is (some? forward-object-node)
            "forward-object node is not nil")
        (is (html-rules/object-node forward-object-node)
            "forward-object node is actually objectifiable")
        (is (html-rules/subject-node current-node)
            "current node is subjectifiable")
        (is (< (noget+ current-node :?startIndex) (noget+ forward-object-node :?startIndex))
            "forward-object node is positionally ahead")
        (is (obpr/some-forward-sibling-node (comp #{(noget+ forward-object-node :id)} #(noget+ % :id)) current-node)
            "forward-object node is a forward sibling of the current node")))))

(defspec node->current-last-child-object-ctx-spec 10
  (prop/for-all [parent-node (gen/let [tree (obgt-html/tree {:hiccup-gen-opts
                                                             {:vector-gen-args [2 6]
                                                              :tag-name-gen obgt-html/html-element-container-tag-name}})]
                               (noget+ tree :?rootNode))]
    (let [{:keys [last-child-object-node
                  current-node]} (html-rules/node->current-last-child-object-ctx
                                  parent-node
                                  (noget+ parent-node :?startIndex))]
      (testing "when last-child-object node is found"
        (is (some? last-child-object-node)
            "last-child-object node is not nil")
        (is (html-rules/object-node last-child-object-node)
            "last-child-object node is actually objectifiable")
        (is (html-rules/subject-node current-node)
            "current node is subjectifiable")
        (is (obpr/range-in-node? current-node
                                 (noget+ last-child-object-node :?startIndex)
                                 (dec (noget+ last-child-object-node :?endIndex)))
            "last-child-object node is positionally within the current-node")
        (is (obpr/some-descendant-node #(= last-child-object-node %) current-node)
            "last-child-object node is a child of the current node")))))