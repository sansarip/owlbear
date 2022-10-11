import {
  commands as vscodeCommands,
  Disposable,
  ExtensionContext,
  window,
} from "vscode";
import { EditCtx, OwlbearFunction } from "./types";
import { getDocCtx, edit } from "./utilities";
import clipboard from "clipboardy";
import { docUriToTreeIdMap, setNewTreeIdForDocUri } from "./tree";

const ob = require("../../../out/cljs/owlbear");

type Handler = (
  editCtx?: EditCtx | undefined
) => undefined | Thenable<EditCtx | undefined>;

type Command = {
  id: string;
  handler: Handler;
};

type Edit = (
  obOp: OwlbearOperation
) => Promise<EditCtx | undefined> | undefined;

type OwlbearOperation =
  | "BackwardMove"
  | "DownwardMove"
  | "ForwardSlurp"
  | "ForwardBarf"
  | "ForwardMove"
  | "Kill"
  | "Raise"
  | "UpwardMove";

const getOwlbearFunction = (
  operation: OwlbearOperation
): OwlbearFunction | undefined => {
  const editor = window.activeTextEditor;
  const langId = editor?.document?.languageId;
  if (!langId) {
    return;
  }
  let langPrefix = langId;
  switch (langPrefix) {
    case "javascript":
      langPrefix = "js";
      break;
    case "javascriptreact":
      langPrefix = "jsx";
      break;
    case "typescript":
      langPrefix = "ts";
      break;
    case "typescriptreact":
      langPrefix = "tsx";
      break;
  }
  if (!langPrefix) {
    return;
  }
  return ob[langPrefix + operation];
};

export const getEditCtx = (obOp: OwlbearOperation): EditCtx | undefined => {
  const editor = window.activeTextEditor;
  if (!editor) {
    return;
  }
  const { src, offset, docUri } = getDocCtx(editor);
  const obFn = getOwlbearFunction(obOp);
  if (!obFn) {
    return;
  }
  const treeId = docUriToTreeIdMap[docUri] || setNewTreeIdForDocUri(docUri);
  return obFn(src, offset, treeId);
};

const doEditOp: Edit = (obOp: OwlbearOperation) => {
  const editor = window.activeTextEditor;
  const editCtx = getEditCtx(obOp);
  if (!editor || !editCtx || Object.keys(editCtx).length === 0) {
    return;
  }
  return edit(editor, editCtx);
};

const backwardMove: Handler = () => doEditOp("BackwardMove");

const downwardMove: Handler = () => doEditOp("DownwardMove");

const upwardMove: Handler = () => doEditOp("UpwardMove");

const forwardSlurp: Handler = () => doEditOp("ForwardSlurp");

const forwardBarf: Handler = () => doEditOp("ForwardBarf");

const forwardMove: Handler = () => doEditOp("ForwardMove");

const kill: Handler = () => doEditOp("Kill");

const copy: Handler = async (editCtx = undefined) => {
  const ctx = editCtx ?? getEditCtx("Kill");
  const removedText = ctx?.removedText;
  if (!removedText) {
    return;
  }
  clipboard.writeSync(ctx.removedText);
  return ctx;
};

const cut: Handler = async () => {
  const editCtx = await kill();
  return await copy(editCtx);
};

const raise: Handler = () => {
  return doEditOp("Raise");
};

const commands: Command[] = [
  { id: "owlbear.backwardMove", handler: backwardMove },
  { id: "owlbear.copy", handler: copy },
  { id: "owlbear.cut", handler: cut },
  { id: "owlbear.downwardMove", handler: downwardMove },
  { id: "owlbear.forwardBarf", handler: forwardBarf },
  { id: "owlbear.forwardMove", handler: forwardMove },
  { id: "owlbear.forwardSlurp", handler: forwardSlurp },
  { id: "owlbear.kill", handler: kill },
  { id: "owlbear.raise", handler: raise },
  { id: "owlbear.upwardMove", handler: upwardMove },
];

export const registerCommands = (context: ExtensionContext) => {
  context.subscriptions.push(
    ...commands.map(
      ({ id, handler }: Command): Disposable =>
        vscodeCommands.registerCommand(id, handler)
    )
  );
};
