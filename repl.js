const fs = require("fs");

const WasmParser = require("web-tree-sitter");
WasmParser.init()
  .then(() => WasmParser.Language.load("./resources/tree-sitter-tsx.wasm"))
  .then((Lang) => {
    const Parser = new WasmParser();
    Parser.setLanguage(Lang);
    const tree = Parser.parse("interface foo {}");
    console.log(tree.rootNode.children[0].childForFieldName('body').text);
    tree.rootNode.id === tree.rootNode.children[0].tree.rootNode.id;
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
// const tsxTree = parser.parse(fs.readFileSync("./samples/hello-world.jsx", "utf-8"));
const tsxTree = parser.parse("interface foo {}");
console.log(tsxTree.rootNode.children[0].bodyNode.text);

require("colors");
const Diff = require("diff");

const one = "beep boop";
const other = "beep boob blah";

const diff = Diff.diffChars(one, other);

diff.forEach((part) => {
  // green for additions, red for deletions
  // grey for common parts
  const color = part.added ? "green" : part.removed ? "red" : "grey";
  process.stdout.write(part.value[color]);
});

console.log();