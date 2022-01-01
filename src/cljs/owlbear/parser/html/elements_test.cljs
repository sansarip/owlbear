(ns owlbear.parser.html.elements-test
  (:require [cljs.test :refer [deftest is testing]]
            [oops.core :refer [ocall]]
            [owlbear.parser.html.document :as obp-html-doc]
            [owlbear.parser.html.elements :as obp-html-ele]
            [owlbear.parser.html.rules :as obp-html-rules]))

(def html-elements-rule-index (:html-elements obp-html-rules/rule-index-map))
(def html-element-rule-index (:html-element obp-html-rules/rule-index-map))

(deftest html-element-ctx-test
  (testing "Valid HTML element contexts"
    (let [html-element-ctx #js {:ruleIndex html-element-rule-index}
          html-elements-ctx #js {:ruleIndex html-elements-rule-index
                                 :children #js [html-element-ctx]}]
      (is (= html-element-ctx (obp-html-ele/html-element-ctx html-element-ctx)))
      (is (= html-element-ctx (obp-html-ele/html-element-ctx html-elements-ctx))
          "HTML elements context coerced to HTML element context")))
  (testing "Invalid HTML element contexts"
    (is (nil? (obp-html-ele/html-element-ctx #js {:ruleIndex (inc html-element-rule-index)}))
        "Wrong rule index")
    (is (nil? (obp-html-ele/html-element-ctx #js {:ruleIndex nil}))
        "Nil rule index")
    (is (nil? (obp-html-ele/html-element-ctx #js {}))
        "No rule index property")
    (is (nil? (obp-html-ele/html-element-ctx nil))
        "Nil ctx")))

(deftest ctx->current-html-element-ctxs-test
  ;; Given
  (let [{:keys [cursor-offset root-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                          (str "<html>\n"
                                               "  <header>Ignored</header>"
                                               "  <body>\n"
                                               "    <h1>📍Hello World</h1>\n"
                                               "  </body>\n"
                                               "</html>"))
        ;; When
        current-html-element-ctxs (obp-html-ele/ctx->current-html-element-ctxs root-ctx cursor-offset)]
    ;; Then
    (testing "Current HTML element ctxs"
      (is (= 3 (count current-html-element-ctxs))
          "Correct number of HTML element ctxs returned")
      (is (= "<h1>Hello World</h1>" (ocall (last current-html-element-ctxs) :?getText))
          "Least specific to most specific order"))))

(deftest next-sibling-html-element-ctx-test
  ;; Given
  (let [html-document-tree (obp-html-doc/src->html-document-ctx
                            (str "<html id=\"foo\">\n"
                                 "  <div id=\"baz\">\n"
                                 "    <h1 id=\"quuz\">hello!</h1>\n"
                                 "    <h2 id=\"corge\">from tests!</h2>\n"
                                 "  </div>\n"
                                 "  <div id=\"qux\">weee!</div>\n"
                                 "  <div id=\"quux\">wooo!</div>\n"
                                 "</html>"
                                 "<div id=\"bar\"></div>"))
        [foo-ctx baz-ctx quuz-ctx corge-ctx qux-ctx quux-ctx bar-ctx] (obp-html-ele/ctx->html-elements-ctxs html-document-tree)
        ;; When
        next-sibling-element-to-foo (obp-html-ele/next-sibling-html-element-ctx foo-ctx)
        next-sibling-element-to-baz (obp-html-ele/next-sibling-html-element-ctx baz-ctx)
        next-sibling-element-to-qux (obp-html-ele/next-sibling-html-element-ctx qux-ctx)
        next-sibling-element-to-quuz (obp-html-ele/next-sibling-html-element-ctx quuz-ctx)
        next-sibling-element-to-quux (obp-html-ele/next-sibling-html-element-ctx quux-ctx)
        next-sibling-element-to-corge (obp-html-ele/next-sibling-html-element-ctx corge-ctx)]
    ;; Then
    (testing "HTML elements with HTML element siblings"
      (is (= bar-ctx next-sibling-element-to-foo)
          "The next sibling to foo-ctx is bar-ctx")
      (is (= qux-ctx next-sibling-element-to-baz)
          "The next sibling to baz-ctx is qux")
      (is (= quux-ctx next-sibling-element-to-qux)
          "The next sibling to qux is quux")
      (is (= corge-ctx next-sibling-element-to-quuz)
          "The next sibling to quuz is corge"))
    (testing "HTML elements without siblings"
      (is (nil? next-sibling-element-to-quux)
          "No sibling element to quux")
      (is (nil? next-sibling-element-to-corge)
          "No sibling element to corge"))))

(deftest html-element-start-tag-ctx-map-test
  (testing "HTML element start tag"
    ;; Given
    (let [{expected-start-tag-start-offset :cursor-offset
           :keys [current-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                 (str "<html>\n"
                                      "  <📍h1>\n"
                                      "    Hello World\n"
                                      "  </h1>\n"
                                      "</html>")
                                 {:ctx-type :html-element})
          ;; When
          {:keys [tag-name
                  start-offset
                  stop-offset
                  start-index
                  stop-index]} (obp-html-ele/html-element-ctx-start-tag-map current-ctx)]

      ;; Then
      (is (= "h1" tag-name)
          "Correct tag name")
      (is (= expected-start-tag-start-offset start-offset)
          "Correct start tag start offset")
      (is (= (+ 3 expected-start-tag-start-offset) stop-offset)
          "Correct start tag stop offset")
      (is (= 0 start-index)
          "Correct start tag start index")
      (is (= 2 stop-index)
          "Correct start tag stop index")))
  (testing "HTML element without end tag"
    ;; Given
    (let [{expected-start-tag-start-offset :cursor-offset
           :keys [current-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                 (str "<html>\n"
                                      "  <📍h1>\n"
                                      "    Hello World\n"
                                      "</html>")
                                 {:ctx-type :html-element})
          ;; When
          {:keys [tag-name
                  start-offset
                  stop-offset
                  start-index
                  stop-index]} (obp-html-ele/html-element-ctx-start-tag-map current-ctx)]
      ;; Then
      (is (= "h1" tag-name)
          "Correct tag name")
      (is (= expected-start-tag-start-offset start-offset)
          "Correct start tag start offset")
      (is (= (+ 3 expected-start-tag-start-offset) stop-offset)
          "Correct start tag stop offset")
      (is (= 0 start-index)
          "Correct start tag start index")
      (is (= 2 stop-index)
          "Correct start tag stop index"))))

(deftest html-element-end-tag-ctx-map-test
  (testing "HTML element with end tag"
    ;; Given
    (let [{expected-end-tag-start-offset :cursor-offset
           :keys [current-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                 (str "<html>\n"
                                      "  <h1>\n"
                                      "    Hello World\n"
                                      "  <📍/h1>\n"
                                      "</html>")
                                 {:ctx-type :html-element})

          ;; When
          {:keys [tag-name
                  start-offset
                  stop-offset
                  start-index
                  stop-index]} (obp-html-ele/html-element-ctx-end-tag-map current-ctx)]

      ;; Then
      (is (= "h1" tag-name)
          "Correct tag name")
      (is (=  expected-end-tag-start-offset start-offset)
          "Correct end tag start offset")
      (is (= (+ 4 expected-end-tag-start-offset) stop-offset)
          "Correct end tag stop offset")
      (is (= 4 start-index)
          "Correct end tag start index")
      (is (= 7 stop-index)
          "Correct end tag stop index")))
  (testing "HTML element with non-matching end tag"
    ;; Given
    (let [{expected-end-tag-start-offset :cursor-offset
           :keys [current-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                 (str "<html>\n"
                                      "  <h1>\n"
                                      "    Hello World\n"
                                      "  <📍/h2>\n"
                                      "</html>")
                                 {:ctx-type :html-element})
          ;; When
          {:keys [tag-name
                  expected-tag-name
                  start-offset
                  stop-offset
                  start-index
                  stop-index]} (obp-html-ele/html-element-ctx-end-tag-map current-ctx)]
      ;; Then
      (is (= "h2" tag-name)
          "Correct tag name")
      (is (= "h1" expected-tag-name)
          "Correct expected tag name")
      (is (=  expected-end-tag-start-offset start-offset)
          "Correct end tag start offset")
      (is (= (+ 4 expected-end-tag-start-offset) stop-offset)
          "Correct end tag stop offset")
      (is (= 4 start-index)
          "Correct end tag start index")
      (is (= 7 stop-index)
          "Correct end tag stop index")))
  (testing "HTML element without end tag"
    ;; Given
    (let [{:keys [current-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                 (str "<html>\n"
                                      "  <h1>📍\n"
                                      "    Hello World\n"
                                      "</html>")
                                 {:ctx-type :html-element})
          ;; When
          end-tag-ctx-map (obp-html-ele/html-element-ctx-end-tag-map current-ctx)]
      ;; Then
      (is (nil? end-tag-ctx-map)
          "End tag not found"))))