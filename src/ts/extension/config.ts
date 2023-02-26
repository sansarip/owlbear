import { commands } from "vscode";

export const NAMESPACE = "owlbear";

export const setContexts = (): void => {
  commands.executeCommand("setContext", `${NAMESPACE}:supportedLanguages`, [
    "html",
    "typescriptreact",
    "javascriptreact",
    "javascript",
    "typescript",
  ]);
};
