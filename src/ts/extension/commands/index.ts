import { commands as vscommands } from "vscode";
import { forwardBarf as htmlForwardBarf } from "./html/tagedit/forward-barf";
import { forwardSlurp as htmlForwardSlurp } from "./html/tagedit/forward-slurp";

export type Command = {
  id: string;
  handler: () => void;
};

export const commands: Command[] = [
  { id: "owlbear.htmlForwardBarf", handler: htmlForwardBarf },
  { id: "owlbear.htmlForwardSlurp", handler: htmlForwardSlurp },
];
