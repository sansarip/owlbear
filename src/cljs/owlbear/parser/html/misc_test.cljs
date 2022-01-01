(ns owlbear.parser.html.misc-test
  (:require [cljs.test :refer [deftest is testing]]
            [owlbear.parser.html.document :as obp-html-doc]
            [owlbear.parser.html.misc :as obp-html-misc]
            [owlbear.parser.html.rules :as obp-html-rules]))

(def html-misc-rule-index (:html-misc obp-html-rules/rule-index-map))
(def html-comment-rule-index (:html-comment obp-html-rules/rule-index-map))

(deftest html-comment-ctx-test
  (testing "Valid HTML comment contexts"
    (let [html-comment-ctx #js {:ruleIndex html-comment-rule-index}
          html-misc-ctx #js {:ruleIndex html-misc-rule-index
                             :children #js [html-comment-ctx]}]
      (is (= html-comment-ctx (obp-html-misc/html-comment-ctx html-comment-ctx)))
      (is (= html-comment-ctx (obp-html-misc/html-comment-ctx html-misc-ctx))
          "HTML comment context")))
  (testing "Invalid HTML comment contexts"
    (is (nil? (obp-html-misc/html-comment-ctx #js {:ruleIndex (inc html-comment-rule-index)}))
        "Wrong rule index")
    (is (nil? (obp-html-misc/html-comment-ctx #js {:ruleIndex nil}))
        "Nil rule index")
    (is (nil? (obp-html-misc/html-comment-ctx #js {}))
        "No rule index property")
    (is (nil? (obp-html-misc/html-comment-ctx nil))
        "Nil ctx")))

(deftest html-comment-start-tag-ctx-map-test
  (testing "HTML comment"
    (let [{:keys [current-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                 (str "<!-- 📍comment -->\n"
                                      "<html>\n"
                                      "</html>"))
          ;; When
          {:keys [tag-name
                  start-offset
                  stop-offset]} (obp-html-misc/html-comment-ctx-start-tag-map current-ctx)]
      ;; Then
      (is (= "<!--" tag-name)
          "Correct tag name")
      (is (= 0 start-offset)
          "Correct end tag start offset")
      (is (= 3 stop-offset)
          "Correct end tag stop offset"))))

(deftest html-comment-end-tag-ctx-map-test
  (testing "HTML comment with end tag"
    (let [{expected-end-tag-start-offset :cursor-offset
           :keys [current-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                 (str "<!-- comment -📍->\n"
                                      "<html>\n"
                                      "</html>"))
          ;; When
          {:keys [tag-name
                  start-offset
                  stop-offset]} (obp-html-misc/html-comment-ctx-end-tag-map current-ctx)]
      ;; Then
      (is (= "-->" tag-name)
          "Correct tag name")
      (is (=  expected-end-tag-start-offset start-offset)
          "Correct end tag start offset")
      (is (= (+ 2 expected-end-tag-start-offset) stop-offset)
          "Correct end tag stop offset")))
  (testing "HTML comment without end tag"
    ;; Given
    (let [{:keys [current-ctx]} (obp-html-doc/src-with-cursor-symbol->current-ctx-map
                                 (str "<!-- comment📍\n"
                                      "<html>\n"
                                      "</html>"))
          ;; When
          end-tag-ctx-map (obp-html-misc/html-comment-ctx-end-tag-map current-ctx)]
      ;; Then
      (is (nil? end-tag-ctx-map)
          "End tag not found"))))