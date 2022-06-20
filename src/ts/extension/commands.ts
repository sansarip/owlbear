import {
  commands as vscodeCommands,
  Disposable,
  ExtensionContext,
  TextEditor,
  window,
} from "vscode";
import { EditCtx, OwlbearFunction } from "./types";
import { getEditCtx, getFileExtension, replace } from "./utilities";
import clipboard from "clipboardy";

const ob = require("../../../out/cljs/owlbear");

type Handler = (editCtx?: EditCtx | undefined) => undefined | Thenable<EditCtx | undefined>;

type Command = {
  id: string;
  handler: Handler;
};

type Edit = (
  editor: TextEditor,
  obFn: OwlbearFunction
) => Promise<EditCtx | undefined> | undefined;

const editDoc: Edit = (editor, obFn) => {
  const editCtx = getEditCtx(editor, obFn);
  if (!editCtx) {
    return;
  }
  return replace(editor, editCtx);
};

const forwardSlurp: Handler = () => {
  const editor = window.activeTextEditor;
  if (!editor) {
    return;
  }
  const fileExtension = getFileExtension(editor?.document);
  switch (fileExtension) {
    case "html":
      return editDoc(editor, ob.htmlForwardSlurp);
    case "ts":
    case "js":
      return editDoc(editor, ob.tsForwardSlurp);
    case "tsx":
    case "jsx":
      return editDoc(editor, ob.tsxForwardSlurp);
  }
};

const forwardBarf: Handler = () => {
  const editor = window.activeTextEditor;
  if (!editor) {
    return;
  }
  const fileExtension = getFileExtension(editor?.document);
  switch (fileExtension) {
    case "html":
      return editDoc(editor, ob.htmlForwardBarf);
    case "ts":
    case "js":
      return editDoc(editor, ob.tsForwardBarf);
    case "tsx":
    case "jsx":
      return editDoc(editor, ob.tsxForwardBarf);
  }
};

const kill: Handler = () => {
  const editor = window.activeTextEditor;
  if (!editor) {
    return;
  }
  const fileExtension = getFileExtension(editor?.document);
  switch (fileExtension) {
    case "html":
      return editDoc(editor, ob.htmlKill);
    case "ts":
    case "js":
      return editDoc(editor, ob.tsKill);
    case "tsx":
    case "jsx":
      return editDoc(editor, ob.tsxKill);
  }
};

const copy: Handler = async (editCtx = undefined) => {
  const editor = window.activeTextEditor;
  if (!editor) {
    return undefined;
  }
  const fileExtension = getFileExtension(editor.document);
  const doCopy = (obFn: OwlbearFunction) => {
    const ctx = editCtx ?? getEditCtx(editor, obFn);
    const removedText = ctx?.removedText;
    if (!removedText) {
      return;
    }
    clipboard.writeSync(ctx.removedText);
    return ctx;
  };
  switch (fileExtension) {
    case "html":
      return doCopy(ob.htmlKill);
    case "ts":
    case "js":
      return doCopy(ob.tsKill);
    case "tsx":
    case "jsx":
      return doCopy(ob.tsxKill);
  }
};

const cut: Handler = async () => {
  const editCtx = await kill();
  return await copy(editCtx);
};

const raise: Handler = () => {
  const editor = window.activeTextEditor;
  if (!editor) {
    return;
  }
  const fileExtension = getFileExtension(editor?.document);
  switch (fileExtension) {
    case "html":
      return editDoc(editor, ob.htmlRaise);
    case "ts":
    case "js":
      return editDoc(editor, ob.tsRaise);
    case "tsx":
    case "jsx":
      return editDoc(editor, ob.tsxRaise);
  }
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
