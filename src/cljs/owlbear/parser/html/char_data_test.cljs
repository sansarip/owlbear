(ns owlbear.parser.html.char-data-test
  (:require [cljs.test :refer [deftest is testing]]
            [owlbear.parser.html.char-data :as obp-html-char]
            [owlbear.parser.html.rules :as obp-html-rules]))

(def html-char-data-rule-index (:char-data obp-html-rules/rule-index-map))

(deftest html-char-data-ctx-test
  (testing "Valid HTML character data contexts"
    (let [html-char-data-ctx #js {:ruleIndex html-char-data-rule-index}]
      (is (= html-char-data-ctx (obp-html-char/html-char-data-ctx html-char-data-ctx))
          "Correct rule index")))
  (testing "Invalid HTML character data contexts"
    (is (nil? (obp-html-char/html-char-data-ctx #js {:ruleIndex (inc html-char-data-rule-index)}))
        "Wrong rule index")
    (is (nil? (obp-html-char/html-char-data-ctx #js {:ruleIndex nil}))
        "Nil rule index")
    (is (nil? (obp-html-char/html-char-data-ctx #js {}))
        "No rule index property")
    (is (nil? (obp-html-char/html-char-data-ctx nil))
        "Nil ctx")))