const antlr4 = require("antlr4");
const Lexer = require("./gen/HTMLLexer").HTMLLexer;
const Parser = require("./gen/HTMLParser").HTMLParser;

const input =
  "<div>asd {} <![CDATA[ asdghhjagsd ]><!doctype html><html></html></div>";
const chars = new antlr4.InputStream(input);
const lexer = new Lexer(chars);
const tokens = new antlr4.CommonTokenStream(lexer);
const parser = new Parser(tokens);
parser.buildParseTrees = true;
const tree = parser.htmlDocument();
console.log(tree.getText());
