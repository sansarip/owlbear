import { commands as vscodeCommands, Disposable, ExtensionContext } from "vscode";
import { forwardBarf as htmlForwardBarf } from "./html/edit/forward-barf";
import { forwardSlurp as htmlForwardSlurp } from "./html/edit/forward-slurp";

export type Command = {
  id: string;
  handler: () => void;
};

export const commands: Command[] = [
  { id: "owlbear.htmlForwardBarf", handler: htmlForwardBarf },
  { id: "owlbear.htmlForwardSlurp", handler: htmlForwardSlurp },
];

export const registerCommands = (context: ExtensionContext) => {
  context.subscriptions.push(
    ...commands.map(
      ({ id, handler }: Command): Disposable =>
        vscodeCommands.registerCommand(id, handler)
    )
  );
};
