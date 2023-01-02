const WasmParser = require("web-tree-sitter");

async function main() {
  await WasmParser.init();
  const htmlWasm = await WasmParser.Language.load("./resources/tree-sitter-html.wasm");
  const tsxWasm = await WasmParser.Language.load("./resources/tree-sitter-tsx.wasm"); 
  const Parser = new WasmParser();

  Parser.setLanguage(htmlWasm);

  const tree = Parser.parse("<><p>1</p>");
  console.log(tree.rootNode.firstChild.firstChild.type);
}

main();