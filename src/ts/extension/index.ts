// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from "vscode";
import { registerCommands } from "./commands";
import { setContexts } from "./config";
import { deleteTree, editTree } from "./tree";
import { log } from "./utilities";

const ob = require("../../../out/cljs/owlbear");

const logWasmLoadingErr = (err: any, lang: string) => {
  log(`Error loading WASM for ${lang}: ${err}`);
};

const loadWasms = async (context: vscode.ExtensionContext) => {
  // Loading in sequence because an error loading one WASM can affect the loading of others if done concurrently
  // See: https://github.com/sansarip/owlbear/issues/107
  try {
    await ob.loadLanguageWasm(
      ob.htmlLangId,
      `${context.extensionPath}/resources/tree-sitter-html.wasm`
    );
  } catch (err) {
    logWasmLoadingErr(err, ob.htmlLangId);
  }

  try {
    await ob.loadLanguageWasm(
      ob.tsxLangId,
      `${context.extensionPath}/resources/tree-sitter-tsx.wasm`
    );
  } catch (err) {
    logWasmLoadingErr(err, ob.tsxLangId);
  }

  try {
    await ob.loadLanguageWasm(
      ob.tsLangId,
      `${context.extensionPath}/resources/tree-sitter-typescript.wasm`
    );
  } catch (err) {
    logWasmLoadingErr(err, ob.tsLangId);
  }
};

export function activate(context: vscode.ExtensionContext) {
  loadWasms(context);

  context.subscriptions.push(
    vscode.workspace.onDidChangeTextDocument((event) => {
      editTree(event);
    })
  );
  context.subscriptions.push(
    vscode.workspace.onDidCloseTextDocument((document) => {
      deleteTree(document);
    })
  );
  setContexts();
  registerCommands(context);
}

export function deactivate() {}
