// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from "vscode";
import { commands, Command } from "./commands";
import { forwardBarf as htmlForwardBarf } from "./commands/html/tagedit/forward-barf";

export function activate(context: vscode.ExtensionContext) {
  context.subscriptions.push(
    ...commands.map(
      ({ id, handler }: Command): vscode.Disposable =>
        vscode.commands.registerCommand(id, handler)
    )
  );
}

// this method is called when your extension is deactivated
export function deactivate() {}
