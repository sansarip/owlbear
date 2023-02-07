const WasmParser = require("web-tree-sitter");

async function main() {
  await WasmParser.init();
  const htmlWasm = await WasmParser.Language.load("./resources/tree-sitter-html.wasm");
  const tsxWasm = await WasmParser.Language.load("./resources/tree-sitter-tsx.wasm"); 
  const Parser = new WasmParser();

  Parser.setLanguage(tsxWasm);

  const tree = Parser.parse(`function foo () {
    return <div></div>
  }`);
  console.log(tree.rootNode.firstChild.tree.rootNode);
}

main();