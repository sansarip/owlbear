(ns owlbear.grammar
  (:require
   [antlr4 :as a4]
   [owlbear-grammar :as obg]))

(defn lex [])

;; const input = "your text to parse here"
;; const chars = new antlr4.InputStream (input);
;; const lexer = new MyGrammarLexer (chars);
;; const tokens = new antlr4.CommonTokenStream (lexer);
;; const parser = new MyGrammarParser (tokens);
;; parser.buildParseTrees = true;
;; const tree = parser.MyStartRule ();
(comment
  (let [input "<h1>hello world</h1>"
        chars (a4/InputStream. input)
        lexer (obg/HTMLLexer. chars)
        _token-stream (a4/CommonTokenStream lexer)
        tokens (js->clj (.getAllTokens lexer))]
    (cljs.pprint/pprint tokens)))
