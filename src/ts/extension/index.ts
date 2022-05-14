// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from "vscode";
import { registerCommands } from "./commands";

const ob = require("../../../out/cljs/owlbear");

export function activate(context: vscode.ExtensionContext) {
  ob.loadLanguageWasm(
    ob.htmlLangId,
    `${context.extensionPath}/resources/tree-sitter-html.wasm`
  );
  ob.loadLanguageWasm(
    ob.tsLangKeyId,
    `${context.extensionPath}/resources/tree-sitter-tsx.wasm`
  );
  ob.loadLanguageWasm(
    ob.tsxLangKeyId,
    `${context.extensionPath}/resources/tree-sitter-typescript.wasm`
  );
  registerCommands(context);
}

export function deactivate() {}
