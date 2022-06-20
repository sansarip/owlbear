import {
  commands as vscodeCommands,
  Disposable,
  ExtensionContext,
  window,
} from "vscode";
import { EditCtx, OwlbearFunction } from "./types";
import { getDocCtx, getFileExtension, replace } from "./utilities";
import clipboard from "clipboardy";

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

type OwlbearOperation = "ForwardSlurp" | "ForwardBarf" | "Kill" | "Raise";

export const getEditCtx = (obOp: OwlbearOperation): EditCtx | undefined => {
  const editor = window.activeTextEditor;
  if (!editor) {
    return;
  }
  const { src, offset } = getDocCtx(editor);
  const obFn = getOwlbearFunction(obOp);
  if (!obFn) {
    return;
  }
  return obFn(src, offset);
};

const getOwlbearFunction = (
  operation: OwlbearOperation
): OwlbearFunction | undefined => {
  const editor = window.activeTextEditor;
  const fileExtension = getFileExtension(editor?.document);
  if (!editor || !fileExtension) {
    return;
  }
  let langPrefix = fileExtension;
  switch (fileExtension) {
    case "jsx":
      langPrefix = "tsx";
      break;
    case "js":
      langPrefix = "ts";
      break;
  }
  return ob[langPrefix + operation];
};

const editDoc: Edit = (obOp: OwlbearOperation) => {
  const editor = window.activeTextEditor;
  const editCtx = getEditCtx(obOp);
  if (!editor || !editCtx) {
    return;
  }
  return replace(editor, editCtx);
};

const forwardSlurp: Handler = () => {
  return editDoc("ForwardSlurp");
};

const forwardBarf: Handler = () => {
  return editDoc("ForwardBarf");
};

const kill: Handler = () => {
  return editDoc("Kill");
};

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
  return editDoc("Raise");
};

const commands: Command[] = [
  { id: "owlbear.copy", handler: copy },
  { id: "owlbear.cut", handler: cut },
  { id: "owlbear.forwardBarf", handler: forwardBarf },
  { id: "owlbear.forwardSlurp", handler: forwardSlurp },
  { id: "owlbear.kill", handler: kill },
  { id: "owlbear.raise", handler: raise },
];

export const registerCommands = (context: ExtensionContext) => {
  context.subscriptions.push(
    ...commands.map(
      ({ id, handler }: Command): Disposable =>
        vscodeCommands.registerCommand(id, handler)
    )
  );
};
