(ns owlbear.paredit-test
  (:require [cljs.test :refer [deftest is testing]]
            [owlbear.grammar :as obg]
            [owlbear.paredit :as obp]))

(deftest forward-slurp-test
  (testing "Non-nested sibling HTML elements"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obg/src-with-cursor-symbol->html-element-ctx-map
                                   (str "<body>📍</body>\n"
                                        "<h1>Hello World</h1>"))
          expected-src (str "<body>\n"
                            "<h1>Hello World</h1></body>")

          ;; When
          actual-src (obp/forward-slurp src cursor-offset)]

      ;; Then
      (is (= expected-src actual-src))))

  (testing "Nested sibling HTML elements"
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obg/src-with-cursor-symbol->html-element-ctx-map
                                   (str "<html>\n"
                                        "  <form action=\"/action_page.php\">📍\n"
                                        "    <label for=\"fname\">First name:</label><br>\n"
                                        "    <input type=\"text\" id=\"fname\" value=\"John\"><br>\n"
                                        "    <label for=\"lname\">Last name:</label><br>\n"
                                        "    <input type=\"text\" id=\"lname\" value=\"Doe\"><br>\n"
                                        "  </form>\n"
                                        "    <input type=\"submit\" value=\"Submit\">\n"
                                        "</html>"))
          expected-src (str "<html>\n"
                            "  <form action=\"/action_page.php\">\n"
                            "    <label for=\"fname\">First name:</label><br>\n"
                            "    <input type=\"text\" id=\"fname\" value=\"John\"><br>\n"
                            "    <label for=\"lname\">Last name:</label><br>\n"
                            "    <input type=\"text\" id=\"lname\" value=\"Doe\"><br>\n"
                            "  \n"
                            "    <input type=\"submit\" value=\"Submit\"></form>\n"
                            "</html>")

          ;; When
          actual-src (obp/forward-slurp src cursor-offset)]

      ;; Then
      (is (= expected-src actual-src)))))
