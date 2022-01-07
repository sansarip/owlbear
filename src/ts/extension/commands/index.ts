import { commands as vscommands } from "vscode";
import { forwardBarf as htmlForwardBarf } from "./html/tagedit/forward-barf";

export type Command = {
  id: string;
  handler: () => void;
};

export const commands: Command[] = [
  { id: "owlbear.htmlForwardBarf", handler: htmlForwardBarf },
];
