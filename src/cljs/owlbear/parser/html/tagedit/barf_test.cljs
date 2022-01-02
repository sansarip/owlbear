(ns owlbear.parser.html.tagedit.barf-test
  (:require [cljs.test :refer [deftest is testing]]
            [owlbear.parser.html :as obp-html]
            [owlbear.parser.html.tagedit.barf :as obp-html-barf]))

(deftest forward-barf-test
  (testing "HTML elements"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obp-html/src-with-cursor-symbol->current-ctx-map
                                   (str "<body>📍\n"
                                        "  <h1>Hello World</h1>\n"
                                        "</body>"))
          expected-src (str "<body>\n"
                            "  </body><h1>Hello World</h1>\n")
          ;; When
          actual-src (obp-html-barf/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place")))
  (testing "HTML character data"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obp-html/src-with-cursor-symbol->current-ctx-map
                                   (str "<body>📍\n"
                                        "  Hello World\n"
                                        "</body>"))
          expected-src (str "<body></body>\n"
                            "  Hello World\n")
          ;; When
          actual-src (obp-html-barf/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place")))
  (testing "HTML comment"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obp-html/src-with-cursor-symbol->current-ctx-map
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
          actual-src (obp-html-barf/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place"))
    (testing "From HTML comment"
      ;; Given
      (let [{src :src-without-cursor-symbol
             :keys [cursor-offset]} (obp-html/src-with-cursor-symbol->current-ctx-map
                                     (str "<!--This 📍is a \n"
                                          "multi-line comment\n"
                                          "<html>\n"
                                          "  <h1>\n"
                                          "    Hello World\n"
                                          "  </h1>\n"
                                          "</html> -->"))
            expected-src (str "<!--This is a \n"
                              "multi-line comment\n"
                              "--><html>\n"
                              "  <h1>\n"
                              "    Hello World\n"
                              "  </h1>\n"
                              "</html> ")
            ;; When
            actual-src (obp-html-barf/forward-barf src cursor-offset)]
        ;; Then
        (is (= expected-src actual-src)
            "End tag moved to correct place"))))
  (testing "HTML (element) style"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obp-html/src-with-cursor-symbol->current-ctx-map
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
          expected-src (str "<html>\n"
                            "  <h1>\n"
                            "    Hello World\n"
                            "  </h1>\n"
                            "  </html><style>\n"
                            "    h1 {\n"
                            "      color: purple;\n"
                            "    }\n"
                            "  </style>\n")
          ;; When
          actual-src (obp-html-barf/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place")))
  (testing "HTML (element) script"
    ;; Given
    (let [{src :src-without-cursor-symbol
           :keys [cursor-offset]} (obp-html/src-with-cursor-symbol->current-ctx-map
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
          actual-src (obp-html-barf/forward-barf src cursor-offset)]
      ;; Then
      (is (= expected-src actual-src)
          "End tag moved to correct place"))))