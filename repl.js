const fs = require("fs");
const jqueryContents = fs.readFileSync("./playground/jquery.js", "utf8");

const WasmParser = require("web-tree-sitter");
WasmParser.init()
  .then(() => WasmParser.Language.load("./resources/tree-sitter-tsx.wasm"))
  .then((Lang) => {
    const Parser = new WasmParser();
    Parser.setLanguage(Lang);
    const tree = Parser.parse("const x = 1;");
  });

const ob = require("./out/cljs/owlbear");
ob.loadLanguageWasm(
  ob.tsLangId,
  `./resources/tree-sitter-typescript.wasm`
);
ob.loadLanguageWasm(
  ob.tsxLangId,
  `./resources/tree-sitter-tsx.wasm`
);
ob.tsForwardMove(jqueryContents, "132303");