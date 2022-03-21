const fs = require("fs");

const WasmParser = require("web-tree-sitter");
WasmParser.init()
  .then(() => WasmParser.Language.load("./resources/tree-sitter-html.wasm"))
  .then((Html) => {
    const htmlParser = new WasmParser();
    htmlParser.setLanguage(Html);
    const htmlTree = htmlParser.parse("<table><table><table>0</table><table>0</table><table>0</table><canvas>-</canvas></table><table><table>0</table></table></table>");
    console.log(htmlTree.rootNode.children[0].children[1].children[4].children[2].text);
  });

// const ob = require("./out/cljs/owlbear");
// ob.htmlInit("./resources/tree-sitter-html.wasm").then(
//   () => {
//     ob.htmlRaise(sourceHtmlCode, 47112);
//   }
// );

const Parser = require("tree-sitter");
const Tsx = require("tree-sitter-typescript").tsx;

const parser = new Parser();
parser.setLanguage(Tsx);
parser.parse(fs.readFileSync("./test-data/hello-world.jsx"));
