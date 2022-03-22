import {
  commands as vscodeCommands,
  Disposable,
  ExtensionContext,
} from "vscode";
import {
  forwardBarf as htmlForwardBarf,
  forwardSlurp as htmlForwardSlurp,
  kill as htmlKill,
  raise as htmlRaise,
} from "./html/edit";

export type Command = {
  id: string;
  handler: () => void;
};

export const commands: Command[] = [
  { id: "owlbear.htmlForwardBarf", handler: htmlForwardBarf },
  { id: "owlbear.htmlForwardSlurp", handler: htmlForwardSlurp },
  { id: "owlbear.htmlKill", handler: htmlKill},
  { id: "owlbear.htmlRaise", handler: htmlRaise },
];

export const registerCommands = (context: ExtensionContext) => {
  context.subscriptions.push(
    ...commands.map(
      ({ id, handler }: Command): Disposable =>
        vscodeCommands.registerCommand(id, handler)
    )
  );
};
