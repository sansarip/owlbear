// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from "vscode";
import { registerCommands } from "./commands";
import { setContexts } from "./config";
import { deleteTree, editTree } from "./tree";

const ob = require("../../../out/cljs/owlbear");

export function activate(context: vscode.ExtensionContext) {
  ob.loadLanguageWasm(
    ob.htmlLangId,
    `${context.extensionPath}/resources/tree-sitter-html.wasm`
  );
  ob.loadLanguageWasm(
    ob.tsLangId,
    `${context.extensionPath}/resources/tree-sitter-typescript.wasm`
  );
  ob.loadLanguageWasm(
    ob.tsxLangId,
    `${context.extensionPath}/resources/tree-sitter-tsx.wasm`
  );
  context.subscriptions.push(vscode.workspace.onDidChangeTextDocument((event) => {
    editTree(event);
  }));
  context.subscriptions.push(vscode.workspace.onDidCloseTextDocument((document) => {
    deleteTree(document);
  }));
  setContexts();
  registerCommands(context);
}

export function deactivate() {}