const Parser = require("tree-sitter");
const Html = require("tree-sitter-html");
const JavaScript = require("tree-sitter-javascript");
const fs = require("fs");
const htmlParser = new Parser();
htmlParser.setLanguage(Html);

const sourceHtmlCode = fs.readFileSync("./test-data/discord.html", "utf8");
const htmlTree = htmlParser.parse(sourceHtmlCode);
console.log(htmlTree.rootNode.children[2].text.length);

// const jsParser = new Parser();
// jsParser.setLanguage(JavaScript);

// const sourceJsCode = fs.readFileSync("./test-data/hello-world.jsx", "utf8");
// const JsTree = jsParser.parse(sourceJsCode);
// console.log(JsTree.rootNode.children);

// const ob = require("./out/cljs/owlbear");
// ob.htmlInit("./resources/tree-sitter-html.wasm").then(
//   () => {
//     ob.htmlRaise(sourceHtmlCode, 47112);
//   }
// );
