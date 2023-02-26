import { commands, workspace } from "vscode";

export const PROJECT_NAMESPACE = "owlbear";
export const PAREDIT_NAMESPACE = `${PROJECT_NAMESPACE}.paredit`;

export const setContextFromConfig = (
  key: string,
  namespace = PROJECT_NAMESPACE
): void => {
  const value = workspace.getConfiguration().get(`${namespace}.${key}`);
  commands.executeCommand("setContext", `${namespace}:${key}`, value);
};

export const setContexts = (): void => {
  commands.executeCommand(
    "setContext",
    `${PROJECT_NAMESPACE}:supportedLanguages`,
    ["html", "typescriptreact", "javascriptreact", "javascript", "typescript"]
  );
  setContextFromConfig('enabled', PAREDIT_NAMESPACE);
};
