// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from "vscode";
import { registerCommands } from "./commands";
import { setContexts } from "./config";
import { deleteTree, editTree } from "./tree";
import { localTimeNow, log, makePath } from "./utilities";
import { owlbearAscii } from "./constants";
import ob from "./ob";

const logWasmLoadingErr = (err: any, lang: string) => {
  log(`Error loading WASM for ${lang}: ${err}`);
};

const loadWasms = async (context: vscode.ExtensionContext) => {
  log(`${localTimeNow()} ⏳ Loading assets...\n`);

  // Loading in sequence because an error loading one WASM can affect the loading of others if done concurrently
  // See: https://github.com/sansarip/owlbear/issues/107
  try {
    await ob.loadLanguageWasm(
      ob.htmlLangId,
      makePath(context.extensionPath, "resources", "tree-sitter-html.wasm")
    );
  } catch (err) {
    logWasmLoadingErr(err, ob.htmlLangId);
  }

  try {
    await ob.loadLanguageWasm(
      ob.tsxLangId,
      makePath(context.extensionPath, "resources", "tree-sitter-tsx.wasm")
    );
  } catch (err) {
    logWasmLoadingErr(err, ob.tsxLangId);
  }

  try {
    await ob.loadLanguageWasm(
      ob.tsLangId,
      makePath(
        context.extensionPath,
        "resources",
        "tree-sitter-typescript.wasm"
      )
    );
  } catch (err) {
    logWasmLoadingErr(err, ob.tsLangId);
  }

  log(`${localTimeNow()} ⌛️ Finished loading assets!`);
};

export function activate(context: vscode.ExtensionContext) {
  log(owlbearAscii);
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
