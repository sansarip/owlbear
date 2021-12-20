(ns owlbear.grammar-test
  (:require
   [cljs.test :refer [deftest is testing]]
   [cljstache.core :as fmt]
   [clojure.string :as string]
   [owlbear.grammar :as obg]))

(def html-element-rule-index (:html-element obg/rule-index-map))
(def html-elements-rule-index (:html-elements obg/rule-index-map))

(deftest html-element-ctx-test
  (testing "Valid HTML element contexts"
    (let [html-element-ctx #js {:ruleIndex html-element-rule-index}
          html-elements-ctx #js {:ruleIndex html-elements-rule-index
                                 :children #js [html-element-ctx]}]
      (is (= html-element-ctx (obg/html-element-ctx html-element-ctx)))
      (is (= html-element-ctx (obg/html-element-ctx html-elements-ctx))
          "HTML elements context")))
  (testing "Invalid HTML element contexts"
    (is (nil? (obg/html-element-ctx #js {:ruleIndex (inc html-element-rule-index)}))
        "Wrong rule index")
    (is (nil? (obg/html-element-ctx #js {:ruleIndex nil}))
        "Nil rule index")
    (is (nil? (obg/html-element-ctx #js {}))
        "No rule index property")
    (is (nil? (obg/html-element-ctx nil))
        "Nil ctx")))

(deftest range-in-html-element-ctx-test
  (testing "Valid HTML element and range"
    (is (obg/range-in-html-element-ctx?
         #js {:ruleIndex html-element-rule-index
              :start #js {:start 0}
              :stop #js {:stop 19}}
         10)
        "Offset in HTML element ctx range")
    (is (obg/range-in-html-element-ctx?
         #js {:ruleIndex html-element-rule-index
              :start #js {:start 0}
              :stop #js {:stop 19}}
         0 19)
        "HTML element ctx in range"))
  (testing "Invalid HTML element or range"
    (is (not (obg/range-in-html-element-ctx?
              #js {:ruleIndex (inc html-element-rule-index)
                   :start #js {:start 0}
                   :stop #js {:stop 19}}
              10))
        "Wrong rule index")
    (is (not (obg/range-in-html-element-ctx?
              #js {:ruleIndex html-element-rule-index
                   :start #js {:start 0}
                   :stop #js {:stop 19}}
              20))
        "Offset out of HTML element ctx range")
    (is (not (obg/range-in-html-element-ctx?
              #js {:ruleIndex html-element-rule-index}
              0))
        "No start/stop properties")
    (is (not (obg/range-in-html-element-ctx?
              #js {:ruleIndex html-element-rule-index
                   :start #js {:start 5}
                   :stop #js {:stop 19}}
              nil))
        "Nil offset")))

(deftest next-sibling-html-element-ctx-test
  ;; Given
  (let [html-document-tree (obg/src->html-document-ctx
                            (str "<html id=\"foo\">\n"
                                 "  <div id=\"baz\">\n"
                                 "    <h1 id=\"quuz\">hello!</h1>\n"
                                 "    <h2 id=\"corge\">from tests!</h2>\n"
                                 "  </div>\n"
                                 "  <div id=\"qux\">weee!</div>\n"
                                 "  <div id=\"quux\">wooo!</div>\n"
                                 "</html>"
                                 "<div id=\"bar\"></div>"))
        [foo-ctx baz-ctx quuz corge qux quux bar-ctx] (obg/ctx->html-elements-ctxs html-document-tree)

        ;; When
        next-sibling-element-to-foo (obg/next-sibling-html-element-ctx foo-ctx)
        next-sibling-element-to-baz (obg/next-sibling-html-element-ctx baz-ctx)
        next-sibling-element-to-qux (obg/next-sibling-html-element-ctx qux)
        next-sibling-element-to-quuz (obg/next-sibling-html-element-ctx quuz)
        next-sibling-element-to-quux (obg/next-sibling-html-element-ctx quux)
        next-sibling-element-to-corge (obg/next-sibling-html-element-ctx corge)]

    ;; Then
    (testing "HTML elements with siblings"
      (is (= bar-ctx next-sibling-element-to-foo)
          "The next sibling to foo-ctx is bar-ctx")
      (is (= qux next-sibling-element-to-baz)
          "The next sibling to baz-ctx is qux")
      (is (= quux next-sibling-element-to-qux)
          "The next sibling to qux is quux")
      (is (= corge next-sibling-element-to-quuz)
          "The next sibling to quuz is corge"))
    (testing "HTML elements without siblings"
      (is (nil? next-sibling-element-to-quux)
          "No sibling element to quux")
      (is (nil? next-sibling-element-to-corge)
          "No sibling element to corge"))))

(deftest start-tag-ctx-test
  ;; Given
  (let [expected-foo-tag-name "h1"
        foo-start-tag-text (fmt/render "<{{tag-name}} id=\"foo\">" {:tag-name expected-foo-tag-name})
        expected-bar-tag-name "h2"
        bar-start-tag-text (fmt/render "<{{tag-name}} id=\"bar\">" {:tag-name expected-bar-tag-name})
        expected-baz-tag-name "h3"
        baz-start-tag-text (fmt/render "<{{tag-name}} id=\"baz\">" {:tag-name expected-baz-tag-name})
        html-document-str (fmt/render (str "{{&foo-start-tag}}\n"
                                           "  Hello!\n"
                                           "</{{foo-tag-name}}>\n"
                                           "{{&bar-start-tag}}\n"
                                           "  Bye!\n"
                                           "  {{&baz-start-tag}}\n"
                                           "  Bye again!\n"
                                           "</{{bar-tag-name}}>")
                                      {:foo-start-tag foo-start-tag-text
                                       :foo-tag-name expected-foo-tag-name
                                       :bar-start-tag bar-start-tag-text
                                       :bar-tag-name expected-bar-tag-name
                                       :baz-start-tag baz-start-tag-text})
        html-document-tree (obg/src->html-document-ctx html-document-str)
        [foo-ctx bar-ctx baz-ctx] (obg/ctx->html-elements-ctxs html-document-tree)
        expected-foo-start-tag-start-offset (string/index-of html-document-str foo-start-tag-text)
        expected-foo-start-tag-stop-offset (-> (count foo-start-tag-text)
                                               (+ expected-foo-start-tag-start-offset)
                                               dec)
        expected-bar-start-tag-start-offset (string/index-of html-document-str bar-start-tag-text)
        expected-bar-start-tag-stop-offset (-> (count bar-start-tag-text)
                                               (+ expected-bar-start-tag-start-offset)
                                               dec)
        expected-baz-start-tag-start-offset (string/index-of html-document-str baz-start-tag-text)
        expected-baz-start-tag-stop-offset (-> (count baz-start-tag-text)
                                               (+ expected-baz-start-tag-start-offset)
                                               dec)

        ;; When
        {actual-foo-start-tag-start-offset :start-offset
         actual-foo-start-tag-stop-offset :stop-offset
         actual-foo-tag-name :tag-name
         actual-foo-start-tag-start-index :start-index
         actual-foo-start-tag-stop-index :stop-index} (obg/start-tag foo-ctx)
        {actual-bar-start-tag-start-offset :start-offset
         actual-bar-start-tag-stop-offset :stop-offset
         actual-bar-tag-name :tag-name
         actual-bar-start-tag-start-index :start-index
         actual-bar-start-tag-stop-index :stop-index} (obg/start-tag bar-ctx)
        {actual-baz-start-tag-start-offset :start-offset
         actual-baz-start-tag-stop-offset :stop-offset
         actual-baz-tag-name :tag-name
         actual-baz-start-tag-start-index :start-index
         actual-baz-start-tag-stop-index :stop-index} (obg/start-tag baz-ctx)]

    ;; Then
    (testing "First HTML element start tag"
      (is (= expected-foo-start-tag-start-offset actual-foo-start-tag-start-offset)
          "Correct start tag start offset")
      (is (= expected-foo-start-tag-stop-offset actual-foo-start-tag-stop-offset)
          "Correct start tag stop offset")
      (is (= expected-foo-tag-name actual-foo-tag-name)
          "Correct tag name")
      (is (= 0 actual-foo-start-tag-start-index)
          "Correct start tag start index")
      (is (= 3 actual-foo-start-tag-stop-index)
          "Correct start tag stop index"))
    (testing "Last HTML element start tag"
      (is (= expected-bar-start-tag-start-offset actual-bar-start-tag-start-offset)
          "Correct start tag start offset")
      (is (= expected-bar-start-tag-stop-offset actual-bar-start-tag-stop-offset)
          "Correct start tag stop offset")
      (is (= expected-bar-tag-name actual-bar-tag-name)
          "Correct tag name")
      (is (= 0 actual-bar-start-tag-start-index)
          "Correct start tag start index")
      (is (= 3 actual-bar-start-tag-stop-index)
          "Correct start tag stop index"))
    (testing "HTML element without end tag"
      (is (= expected-baz-start-tag-start-offset actual-baz-start-tag-start-offset)
          "Correct start tag start offset")
      (is (= expected-baz-start-tag-stop-offset actual-baz-start-tag-stop-offset)
          "Correct start tag stop offset")
      (is (= expected-baz-tag-name actual-baz-tag-name)
          "Correct tag name")
      (is (= 0 actual-baz-start-tag-start-index)
          "Correct start tag start index")
      (is (= 3 actual-baz-start-tag-stop-index)
          "Correct start tag stop index"))))


(deftest end-tag-ctx-test
  ;; Given
  (let [expected-foo-tag-name "h1"
        foo-end-tag-text (str "</" expected-foo-tag-name ">")
        expected-bar-start-tag-name "h2"
        expected-bar-end-tag-name "wrong-end-tag"
        bar-end-tag-text (str "</" expected-bar-end-tag-name ">")
        html-document-str (fmt/render (str "<{{foo-tag-name}} id=\"foo\">\n"
                                           "  Hello!\n"
                                           "{{&foo-end-tag}}\n"
                                           "<{{bar-start-tag-name}} id=\"bar\">\n"
                                           "  Bye!\n"
                                           "  <h3 id=\"baz\">\n"
                                           "    Bye again!\n"
                                           "{{&bar-end-tag}}")
                                      {:foo-end-tag foo-end-tag-text
                                       :foo-tag-name expected-foo-tag-name
                                       :bar-start-tag-name expected-bar-start-tag-name
                                       :bar-end-tag bar-end-tag-text})
        html-document-tree (obg/src->html-document-ctx html-document-str)
        [foo-ctx bar-ctx baz-ctx] (obg/ctx->html-elements-ctxs html-document-tree)
        expected-foo-end-tag-start-offset (string/index-of html-document-str foo-end-tag-text)
        expected-foo-end-tag-stop-offset (-> (count foo-end-tag-text)
                                             (+ expected-foo-end-tag-start-offset)
                                             dec)
        expected-bar-end-tag-start-offset (string/index-of html-document-str bar-end-tag-text)
        expected-bar-end-tag-stop-offset (-> (count bar-end-tag-text)
                                             (+ expected-bar-end-tag-start-offset)
                                             dec)

        ;; When
        {actual-foo-tag-name :tag-name
         actual-foo-end-tag-start-offset :start-offset
         actual-foo-end-tag-stop-offset :stop-offset
         actual-foo-end-tag-start-index :start-index
         actual-foo-end-tag-stop-index :stop-index} (obg/end-tag foo-ctx)
        {actual-bar-tag-name :tag-name
         actual-expected-bar-tag-name :expected-tag-name
         actual-bar-end-tag-start-offset :start-offset
         actual-bar-end-tag-stop-offset :stop-offset
         actual-bar-end-tag-start-index :start-index
         actual-bar-end-tag-stop-index :stop-index} (obg/end-tag bar-ctx)
        actual-baz-end-tag (obg/end-tag baz-ctx)]

    (testing "HTML element with end tag"
      (is (= expected-foo-tag-name actual-foo-tag-name)
          "Correct tag name")
      (is (= expected-foo-end-tag-start-offset actual-foo-end-tag-start-offset)
          "Correct stop tag start offset")
      (is (= expected-foo-end-tag-stop-offset actual-foo-end-tag-stop-offset)
          "Correct stop tag stop offset")
      (is (= 5 actual-foo-end-tag-start-index)
          "Correct stop tag start index")
      (is (= 8 actual-foo-end-tag-stop-index)
          "Correct stop tag stop index"))
    (testing "HTML element with non-matching end tag name"
      (is (= expected-bar-start-tag-name actual-expected-bar-tag-name)
          "Correct expected end tag name, based on start tag")
      (is (= expected-bar-end-tag-name actual-bar-tag-name)
          "Correct actual end tag name, not matching start tag")
      (is (= expected-bar-end-tag-start-offset actual-bar-end-tag-start-offset)
          "Correct stop tag start offset")
      (is (= expected-bar-end-tag-stop-offset actual-bar-end-tag-stop-offset)
          "Correct stop tag stop offset")
      (is (= 5 actual-bar-end-tag-start-index)
          "Correct stop tag start index")
      (is (= 8 actual-bar-end-tag-stop-index)
          "Correct stop tag stop index"))
    (testing "HTML element without end tag"
      (is (nil? actual-baz-end-tag)))))