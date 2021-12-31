(ns owlbear.tagedit-test
  (:require [cljs.test :refer [deftest is testing]]
            [owlbear.grammar :as obg]
            [owlbear.tagedit :as obp]))

(deftest forward-slurp-test
  (testing "HTML elements"
    (testing "Root level sibling HTML elements"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<body>📍</body>\n"
                                          "<h1>Hello World</h1>"))
            expected-src (str "<body>\n"
                              "<h1>Hello World</h1></body>")
            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place")))
    (testing "Scope elevation when current context has no siblings"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<body>\n"
                                          "  <h1>📍</h1>\n"
                                          "</body>\n"
                                          "<h1>Hello World</h1>"))
            expected-src (str "<body>\n"
                              "  <h1></h1>\n"
                              "\n"
                              "<h1>Hello World</h1></body>")
            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place")))
    (testing "Nested sibling HTML elements"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
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
        (is (= expected-src actual-src)
            "End tag moved to correct place"))))

  (testing "HTML character data"
    (testing "Root level sibling HTML element and character data"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<html>📍</html>\n"
                                          "Hello World"))
            expected-src (str "<html></html>\n")

            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (nil? actual-src)
            "Extraneous character data gets dropped")))
    (testing "Nested sibling HTML element and character data"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<html>\n"
                                          "  <h1>📍</h1>\n"
                                          "  Hello\n"
                                          "  <h2>World</h2>\n"
                                          "</html>"))
            expected-src (str "<html>\n"
                              "  <h1>\n"
                              "  Hello\n"
                              "  </h1><h2>World</h2>\n"
                              "</html>")
            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place"))))
  (testing "HTML comment"
    (testing "Root level sibling HTML element and comment"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<html>📍\n"
                                          "  <h1>\n"
                                          "    Hello World\n"
                                          "  </h1>\n"
                                          "</html>\n"
                                          "<!-- This is a \n"
                                          "multi-line comment -->\n"))
            expected-src (str "<html>\n"
                              "  <h1>\n"
                              "    Hello World\n"
                              "  </h1>\n"
                              "\n"
                              "<!-- This is a \n"
                              "multi-line comment --></html>\n")
            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place")))
    (testing "Nested sibling HTML element and comment"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<html>\n"
                                          "  <h1>📍\n"
                                          "    Hello World\n"
                                          "  </h1>\n"
                                          "<!-- This is a \n"
                                          "multi-line comment -->\n"
                                          "</html>"))
            expected-src (str "<html>\n"
                              "  <h1>\n"
                              "    Hello World\n"
                              "  \n"
                              "<!-- This is a \n"
                              "multi-line comment --></h1>\n"
                              "</html>")
            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place")))
    (testing "HTML element into HTML comment"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<!-- This 📍is a \n"
                                          "multi-line comment -->\n"
                                          "<html>\n"
                                          "  <h1>\n"
                                          "    Hello World\n"
                                          "  </h1>\n"
                                          "</html>"))
            expected-src (str "<!-- This is a \n"
                              "multi-line comment \n"
                              "<html>\n"
                              "  <h1>\n"
                              "    Hello World\n"
                              "  </h1>\n"
                              "</html>-->")
            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place"))))
  (testing "HTML (element) style"
    (testing "Root level sibling HTML element and style"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<html>📍\n"
                                          "  <h1>\n"
                                          "    Hello World\n"
                                          "  </h1>\n"
                                          "</html>\n"
                                          "<style>\n"
                                          "  h1 {\n"
                                          "    color: purple;\n"
                                          "  }\n"
                                          "</style>"))
            expected-src (str "<html>\n"
                              "  <h1>\n"
                              "    Hello World\n"
                              "  </h1>\n"
                              "\n"
                              "<style>\n"
                              "  h1 {\n"
                              "    color: purple;\n"
                              "  }\n"
                              "</style></html>")
            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place")))
    (testing "Nested sibling HTML element and style"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<html>\n"
                                          "<header>📍</header>\n"
                                          "<style>\n"
                                          "  h1 {\n"
                                          "    font-size: 1.5em;\n"
                                          "  }\n"
                                          "</style>\n"
                                          "</html>"))
            expected-src (str "<html>\n"
                              "<header>\n"
                              "<style>\n"
                              "  h1 {\n"
                              "    font-size: 1.5em;\n"
                              "  }\n"
                              "</style></header>\n"
                              "</html>")
            ;; When
            actual-src (obp/forward-slurp src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place")))))

(deftest forward-barf-test
  (testing "HTML elements"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                   (str "<body>📍\n"
                                        "  <h1>Hello World</h1>\n"
                                        "</body>"))
          expected-src (str "<body>\n"
                            "  </body><h1>Hello World</h1>")
          ;; When
          actual-src (obp/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place")))
  (testing "HTML character data"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                   (str "<body>📍\n"
                                        "  Hello World\n"
                                        "</body>"))
          expected-src (str "<body>\n"
                            "  </body>Hello World\n")
          ;; When
          actual-src (obp/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place")))
  (testing "HTML comment"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                   (str "<html>📍\n"
                                        "  <h1>\n"
                                        "    Hello World\n"
                                        "  </h1>\n"
                                        "  <!-- This is a \n"
                                        "  multi-line comment -->\n"
                                        "</html>"))
          expected-src (str "<html>\n"
                            "  <h1>\n"
                            "    Hello World\n"
                            "  </h1>\n"
                            "  </html><!-- This is a \n"
                            "  multi-line comment -->\n")
          ;; When
          actual-src (obp/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place"))
    (testing "From HTML comment"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                     (str "<!-- This 📍is a \n"
                                          "multi-line comment\n"
                                          "<html>\n"
                                          "  <h1>\n"
                                          "    Hello World\n"
                                          "  </h1>\n"
                                          "</html> -->"))
            expected-src (str "<!-- This is a \n"
                              "multi-line comment\n"
                              "--><html>\n"
                              "  <h1>\n"
                              "    Hello World\n"
                              "  </h1>\n"
                              "</html> ")
            ;; When
            actual-src (obp/forward-barf src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place"))))
  (testing "HTML (element) style"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                   (str "<html>📍\n"
                                        "  <h1>\n"
                                        "    Hello World\n"
                                        "  </h1>\n"
                                        "  <style>\n"
                                        "    h1 {\n"
                                        "      color: purple;\n"
                                        "    }\n"
                                        "  </style>\n"
                                        "</html>"))
          expected-src (str "<html>📍\n"
                            "  <h1>\n"
                            "    Hello World\n"
                            "  </h1>\n"
                            "  </html><style>\n"
                            "    h1 {\n"
                            "      color: purple;\n"
                            "    }\n"
                            "  </style>\n")
          ;; When
          actual-src (obp/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place")))
  (testing "HTML (element) script"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obg/src-with-cursor-symbol->current-ctx-map
                                   (str "<html>📍\n"
                                        "  <h1>\n"
                                        "    Hello World\n"
                                        "  </h1>\n"
                                        "  <script>\n"
                                        "    alert('Hello World!');\n"
                                        "  </script>\n"
                                        "</html>"))
          expected-src (str "<html>\n"
                            "  <h1>\n"
                            "    Hello World\n"
                            "  </h1>\n"
                            "  </html><script>\n"
                            "    alert('Hello World!');\n"
                            "  </script>\n")
          ;; When
          actual-src (obp/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place"))))