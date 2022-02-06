// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from "vscode";
import { registerCommands } from "./commands";

const ob = require("../../../out/cljs/owlbear");

export function activate(context: vscode.ExtensionContext) {
  ob.htmlInit(
    `${context.extensionPath}/resources/tree-sitter-html.wasm`
  );
  registerCommands(context);
}

export function deactivate() {}
