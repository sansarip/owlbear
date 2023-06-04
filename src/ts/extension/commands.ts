import {
  commands as vscodeCommands,
  Disposable,
  ExtensionContext,
  window,
  workspace,
} from "vscode";
import { EditCtx, OwlbearFunction } from "./types";
import {
  getDocCtx,
  edit,
  isEmptyObj,
  copyRangeToClipboard,
  cutRangeToClipboard,
  moveCursor,
} from "./utilities";
import { docUriToTreeIdMap, setNewTreeIdForDocUri } from "./tree";
import {
  AUTOFORMAT_NAMESPACE,
  PAREDIT_NAMESPACE,
  setContextFromConfig,
} from "./config";
import ob from "./ob";

type Handler = (editCtx?: EditCtx) => undefined | Thenable<EditCtx | undefined>;

type Command = {
  id: string;
  handler: Handler;
};

type Edit = (
  obOp: OwlbearOperation
) => Promise<EditCtx | undefined> | undefined;

type OwlbearOperation =
  | "BackwardDelete"
  | "BackwardMove"
  | "DownwardMove"
  | "ForwardDelete"
  | "ForwardSlurp"
  | "ForwardBarf"
  | "ForwardMove"
  | "Kill"
  | "Raise"
  | "Splice"
  | "UpwardMove";

enum ClipboardOp {
  copy = "Copy",
  cut = "Cut",
}

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
      langPrefix = "ts";
      break;
    case "javascriptreact":
      langPrefix = "tsx";
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
  if (!editor || !editCtx || isEmptyObj(editCtx)) {
    return;
  }
  const shouldFormat = !!workspace
    .getConfiguration()
    .get(`${AUTOFORMAT_NAMESPACE}.enabled`);
  return edit(editor, editCtx, shouldFormat);
};

const doClipboardOp = async (op: ClipboardOp) => {
  const ctx = getEditCtx("Kill");
  const removedText = ctx?.removedText;
  const editor = window.activeTextEditor;
  if (!removedText || !editor) {
    return;
  }
  const startIndex = ctx.offset;
  const endIndex = startIndex + removedText.length;
  const clipboardOpFn =
    op === ClipboardOp.copy ? copyRangeToClipboard : cutRangeToClipboard;
  await clipboardOpFn(editor, startIndex, endIndex);
  return ctx;
};

const deleteLeft = (): void => {
  vscodeCommands.executeCommand("deleteLeft");
};

const deleteRight = (): void => {
  vscodeCommands.executeCommand("deleteRight");
};

const forceBackwardDelete: Handler = async () => {
  deleteLeft();
  return undefined;
};

const forceForwardDelete: Handler = async () => {
  deleteRight();
  return undefined;
};

const backwardDelete: Handler = async () => {
  const editCtx = getEditCtx("BackwardDelete");
  if (!editCtx || isEmptyObj(editCtx)) {
    deleteLeft();
    return;
  }

  const { deleteStartOffset, deleteEndOffset } = editCtx;
  const numCharsToDelete = deleteEndOffset - deleteStartOffset;
  if (numCharsToDelete === 1) {
    deleteLeft();
    return;
  }

  const editor = window.activeTextEditor;
  if (!editor) {
    return;
  }
  return edit(editor, editCtx);
};

const backwardMove: Handler = () => doEditOp("BackwardMove");

const downwardMove: Handler = () => doEditOp("DownwardMove");

const upwardMove: Handler = () => doEditOp("UpwardMove");

const forwardSlurp: Handler = () => doEditOp("ForwardSlurp");

const forwardBarf: Handler = () => doEditOp("ForwardBarf");

const forwardDelete: Handler = async () => {
  const editCtx = getEditCtx("ForwardDelete");
  if (!editCtx || isEmptyObj(editCtx)) {
    deleteRight();
    return;
  }

  const { deleteStartOffset, deleteEndOffset } = editCtx;
  const numCharsToDelete = deleteEndOffset - deleteStartOffset;
  if (numCharsToDelete === 1) {
    deleteRight();
    return;
  }

  const editor = window.activeTextEditor;
  if (!editor) {
    return;
  }
  return edit(editor, editCtx);
};

const forwardMove: Handler = () => doEditOp("ForwardMove");

const kill: Handler = () => doEditOp("Kill");

const copy: Handler = async () => {
  return doClipboardOp(ClipboardOp.copy);
};

const cut: Handler = async () => {
  const ctx = await doClipboardOp(ClipboardOp.cut);
  const editor = window.activeTextEditor;
  if (!ctx?.removedText || !editor) {
    return;
  }
  const startIndex = ctx.offset;
  moveCursor(editor, startIndex);
  return ctx;
};

const raise: Handler = () => {
  return doEditOp("Raise");
};

const splice: Handler = () => {
  return doEditOp("Splice");
};

const toggleAutoformat: Handler = async () => {
  const autoFormatEnabled = workspace
    .getConfiguration()
    .get(`${AUTOFORMAT_NAMESPACE}.enabled`);
  await workspace
    .getConfiguration()
    .update(`${AUTOFORMAT_NAMESPACE}.enabled`, !autoFormatEnabled);
  return undefined;
};

const toggleParedit: Handler = async () => {
  const pareditEnabled = workspace
    .getConfiguration()
    .get(`${PAREDIT_NAMESPACE}.enabled`);
  await workspace
    .getConfiguration()
    .update(`${PAREDIT_NAMESPACE}.enabled`, !pareditEnabled);
  setContextFromConfig("enabled", PAREDIT_NAMESPACE);
  return undefined;
};

const commands: Command[] = [
  { id: "owlbear.backwardDelete", handler: backwardDelete },
  { id: "owlbear.backwardMove", handler: backwardMove },
  { id: "owlbear.copy", handler: copy },
  { id: "owlbear.cut", handler: cut },
  { id: "owlbear.downwardMove", handler: downwardMove },
  { id: "owlbear.forceBackwardDelete", handler: forceBackwardDelete },
  { id: "owlbear.forceForwardDelete", handler: forceForwardDelete },
  { id: "owlbear.forwardBarf", handler: forwardBarf },
  { id: "owlbear.forwardDelete", handler: forwardDelete },
  { id: "owlbear.forwardMove", handler: forwardMove },
  { id: "owlbear.forwardSlurp", handler: forwardSlurp },
  { id: "owlbear.kill", handler: kill },
  { id: "owlbear.raise", handler: raise },
  { id: "owlbear.splice", handler: splice },
  { id: "owlbear.toggleAutoformat", handler: toggleAutoformat },
  { id: "owlbear.toggleParedit", handler: toggleParedit },
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
