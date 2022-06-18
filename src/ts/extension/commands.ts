import {
  commands as vscodeCommands,
  Disposable,
  ExtensionContext,
  TextEditor,
  window,
} from "vscode";
import { EditCtx } from "./types";
import { getFileExtension, replace } from "./utilities";
import clipboard from "clipboardy";

const ob = require("../../../out/cljs/owlbear");

type Handler = () => undefined | Thenable<EditCtx | undefined>;

type Command = {
  id: string;
  handler: Handler;
};

type GetEditCtx = (src: string, offset: number) => EditCtx;

type Edit = (
  editor: TextEditor | undefined,
  getEditCtx: GetEditCtx
) => Promise<EditCtx | undefined> | undefined;

const editDoc: Edit = (editor, getEditCtx) => {
  if (!editor) {
    return;
  }
  const document = editor.document;
  const cursorOffset = document.offsetAt(editor.selection.active);
  const source = document.getText();
  const editCtx = getEditCtx(source, cursorOffset);
  if (!editCtx) {
    return;
  }
  return replace(editor, cursorOffset, editCtx);
};

const forwardSlurp: Handler = () => {
  const editor = window.activeTextEditor;
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

const cut: Handler = async () => {
  const editCtx = await kill();
  const removedText = editCtx?.removedText;
  if (!removedText) {
    return;
  }
  clipboard.writeSync(removedText);
  return editCtx;
};

const raise: Handler = () => {
  const editor = window.activeTextEditor;
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
