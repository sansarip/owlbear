const antlr4 = require("antlr4");
const Lexer = require("./gen/HTMLLexer").HTMLLexer;
const Parser = require("./gen/HTMLParser").HTMLParser;

const input =
  "asd {} < <div><h1>asd sad <div></div> d </h1></div> <![CDATA[ad]]> <!-- --><!-- asd --> <![CDATA[sd]]><!doctype html> <html>1 2 3 4</html> asdasd ";
const chars = new antlr4.InputStream(input);
const lexer = new Lexer(chars);
const tokens = new antlr4.CommonTokenStream(lexer);
const parser = new Parser(tokens);
parser.buildParseTrees = true;
const tree = parser.html();
console.log(tree);
console.log(tree.children[0].children[5].getText());
