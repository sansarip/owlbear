(ns owlbear.ts.parse.rules-test
  (:require [cljs.test :refer [deftest is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [owlbear.generators.tree.ts :as obgt-ts]
            [owlbear.ts.parse.rules :as ob-ts-rules]
            [owlbear.parse.rules :as obpr]
            [owlbear.utilities :refer [noget+] :refer-macros [&testing]]))

(defspec subject-node-spec 10
  (prop/for-all [{:keys [node]} (gen/let [node (obgt-ts/with-children obgt-ts/subject-node*)]
                                  {:node node
                                   :node-text (noget+ node :?text)})]
    (&testing "when subject node"
      (is (= node (ob-ts-rules/subject-node node))
          "input node should equal returned node"))))

(deftest subject-node-test
  (&testing "when invalid type"
    (is (nil? (ob-ts-rules/subject-node #js {:type (str (random-uuid))}))
        "subject node not found")))

(defspec node->current-subject-nodes-spec 10
  (prop/for-all [{:keys [nodes]} (gen/let [tree obgt-ts/tree-with-t-subject]
                                   (let [[root-node :as nodes] (obpr/node->descendants (noget+ tree :?rootNode))]
                                     {:nodes nodes
                                      :src (noget+ root-node :?text)}))]
    (let [current-subject-nodes (some (comp not-empty #(ob-ts-rules/node->current-subject-nodes % (noget+ % :?startIndex)))
                                      nodes)]
      (&testing "when current subject nodes exist"
        (is (not-empty current-subject-nodes)
            "returned current subject nodes not empty")
        (is (every? ob-ts-rules/subject-node current-subject-nodes)
            "every node is a subject node")
        (is (< (count current-subject-nodes) (count nodes))
            "there are less subject nodes than all nodes")
        (is (apply <= (map #(noget+ % :?startIndex) current-subject-nodes))
            "nodes are sorted from least to most specific")))))

(deftest node->current-subject-nodes-test
  (&testing "when node has no subject-node children"
    (is (empty? (ob-ts-rules/node->current-subject-nodes #js {:children #js [#js {:type (str (random-uuid))}]} 0))
        "invalid child node type")
    (is (empty? (ob-ts-rules/node->current-subject-nodes #js {} 0))
        "no children")))

(defspec object-node-spec 10
  (prop/for-all [{:keys [node]} (gen/let [node obgt-ts/object-node]
                                  {:node node
                                   :node-text (noget+ node :?text)})]
    (&testing "when object node"
      (is (= node (ob-ts-rules/object-node node))
          "input node should equal returned node"))))

(deftest object-node-test
  (&testing "when invalid node type"
    (is (nil? (ob-ts-rules/object-node #js {:type (str (random-uuid))}))
        "object node is not found")))

(defspec node->current-object-nodes-spec 10
  (prop/for-all [{:keys [nodes]} (gen/let [tree obgt-ts/tree-with-t-subject]
                                   (let [[root-node :as nodes] (obpr/node->descendants (noget+ tree :?rootNode))]
                                     {:nodes nodes
                                      :src (noget+ root-node :?text)}))]
    (let [current-object-nodes (some (comp not-empty #(ob-ts-rules/node->current-object-nodes % (noget+ % :?startIndex)))
                                     nodes)]
      (&testing "when current object nodes exist"
        (is (not-empty current-object-nodes)
            "returned current object nodes are not empty")
        (is (every? ob-ts-rules/object-node current-object-nodes)
            "every node is an object node")
        (is (apply <= (map #(noget+ % :?startIndex) current-object-nodes))
            "nodes sorted from least to most specific")))))

(deftest node->current-object-nodes-test
  (&testing "when node has no object-node children"
    (is (empty? (ob-ts-rules/node->current-object-nodes #js {:children #js [#js {:type (str (random-uuid))}]} 0))
        "invalid child node type")
    (is (empty? (ob-ts-rules/node->current-object-nodes #js {} 0))
        "no children")))

(defspec next-object-node-spec 10
  (prop/for-all [{:keys [nodes]} (gen/let [tree obgt-ts/tree-with-t-subject]
                                   (let [root-node (noget+ tree :?rootNode)]
                                     {:nodes (obpr/node->descendants root-node)
                                      :src (noget+ root-node :?text)}))]
    (let [[slurp-anchor-node
           forward-object-node] (some #(when-let [fsn (ob-ts-rules/next-forward-object-node %)]
                                         [% fsn])
                                      nodes)]
      (&testing "when forward-object node is found"
        (is (some? forward-object-node)
            "forward-object node is not nil")
        (is (ob-ts-rules/object-node forward-object-node)
            "forward-object node is actually object")
        (is (< (noget+ slurp-anchor-node :?startIndex) (noget+ forward-object-node :?startIndex))
            "forward-object node is positionally ahead")))))

(deftest next-forward-object-node-test
  (&testing "when next node is not objectifiable"
    (is (nil? (ob-ts-rules/next-forward-object-node #js {:nextSibling #js {:type (str (random-uuid))}}))
        "invalid sibling node type")
    (is (nil? (ob-ts-rules/next-forward-object-node #js {}))
        "no siblings")))

(defspec node->current-forward-object-ctx-spec 10
  (prop/for-all [{:keys [nodes]} (gen/let [tree obgt-ts/tree-with-t-subject]
                                   (let [[root-node :as nodes] (obpr/node->descendants (noget+ tree :?rootNode))]
                                     {:nodes nodes
                                      :src (noget+ root-node :?text)}))]
    (let [{:keys [forward-object-node
                  current-node]} (some #(ob-ts-rules/node->current-forward-object-ctx % (noget+ % :?startIndex))
                                       nodes)]
      (&testing "when forward-object node is found"
        (is (some? forward-object-node)
            "forward-object node is not nil")
        (is (ob-ts-rules/object-node forward-object-node)
            "forward-object node is actually objectifiable")
        (is (ob-ts-rules/subject-node current-node)
            "current node is subjectifiable")
        (is (< (noget+ current-node :?startIndex) (noget+ forward-object-node :?startIndex))
            "forward-object node is positionally ahead")
        (is (some (fn [ancestor]
                    (obpr/some-forward-sibling-node
                     (comp #{(noget+ forward-object-node :id)}
                           #(noget+ % :id))
                     ancestor))
                  (conj (obpr/node->ancestors current-node) current-node))
            "forward-object node is a forward sibling of the current node or one of its ancestors")))))

(defspec node->current-last-child-object-ctx-spec 10
  (prop/for-all [{:keys [node]} (gen/let [node (obgt-ts/with-children obgt-ts/subject-node*)]
                                  {:node node
                                   :node-text (noget+ node :?text)})]
    (let [{:keys [last-child-object-node
                  current-node]} (ob-ts-rules/node->current-last-child-object-ctx
                                  node
                                  (noget+ node :?startIndex))]
      (&testing "when last-child-object node is found"
        (is (some? last-child-object-node)
            "last-child-object node is not nil")
        (is (ob-ts-rules/object-node last-child-object-node)
            "last-child-object node is actually objectifiable")
        (is (ob-ts-rules/subject-node current-node)
            "current node is subjectifiable")
        (is (obpr/range-in-node? current-node
                                 (noget+ last-child-object-node :?startIndex)
                                 (noget+ last-child-object-node :?endIndex))
            "last-child-object node is positionally within the current-node")
        (is (obpr/some-descendant-node (comp #(= (noget+ last-child-object-node :?id)
                                            (noget+ % :?id))
                                        ob-ts-rules/object-node) current-node)
            "last-child-object node is a child of the current node")))))