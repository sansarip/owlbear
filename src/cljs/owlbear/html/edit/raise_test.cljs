(ns owlbear.html.edit.raise-test
  (:require [cljs.test :refer [deftest is testing]]
            [owlbear.utilities :as obu]
            [owlbear.html.edit.raise :as ob-html-raise]))
(deftest raise-test
  (let [test-result (ob-html-raise/raise "<div><h1></h1></div>" 7)]
    (is (= "<h1></h1>" (obu/noget+ test-result :src)))))