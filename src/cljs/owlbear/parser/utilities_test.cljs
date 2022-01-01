(ns owlbear.parser.utilities-test
  (:require [cljs.test :refer [deftest is testing]]
            [owlbear.parser.utilities :as obpu]
            [owlbear.parser.html.rules :as obp-html-rules]))

(def html-element-rule-index (:html-element obp-html-rules/rule-index-map))

(deftest range-in-ctx-test
  (testing "Valid range"
    (is (obpu/range-in-ctx?
         #js {:ruleIndex html-element-rule-index
              :start #js {:start 0}
              :stop #js {:stop 19}}
         10)
        "Offset in context's range")
    (is (obpu/range-in-ctx?
         #js {:ruleIndex html-element-rule-index
              :start #js {:start 0}
              :stop #js {:stop 19}}
         0 19)
        "Context in given range"))
  (testing "Invalid range"
    (is (not (obpu/range-in-ctx?
              #js {:ruleIndex html-element-rule-index
                   :start #js {:start 0}
                   :stop #js {:stop 19}}
              20))
        "Offset out of context's range")
    (is (not (obpu/range-in-ctx?
              #js {:ruleIndex html-element-rule-index}
              0))
        "No start/stop properties")
    (is (not (obpu/range-in-ctx?
              #js {:ruleIndex html-element-rule-index
                   :start #js {:start 5}
                   :stop #js {:stop 19}}
              nil))
        "Nil offset")))
