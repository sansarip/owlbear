const Parser = require("tree-sitter");
const Html = require("tree-sitter-html");
const fs = require("fs");
const htmlParser = new Parser();
htmlParser.setLanguage(Html);

const sourceHtmlCode = fs.readFileSync("./test-data/discord.html", "utf8");
const htmlTree = htmlParser.parse(sourceHtmlCode);
console.log(htmlTree.rootNode);
