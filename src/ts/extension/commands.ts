import {
  commands as vscodeCommands,
  Disposable,
  ExtensionContext,
  TextEditor,
  window,
} from "vscode";
import { getFileExtension, replace } from "./utilities";

const ob = require("../../../out/cljs/owlbear");

type Handler = () => void;

type Command = {
  id: string;
  handler: Handler;
};

const editDoc = (editor: TextEditor | undefined, getEditCtx: Function) => {
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
  replace(editor, editCtx.src, cursorOffset, editCtx.offset);
};

const forwardSlurp: Handler = () => {
  const editor = window.activeTextEditor;
  const fileExtension = getFileExtension(editor?.document);
  switch (fileExtension) {
    case "html":
      editDoc(editor, ob.htmlForwardSlurp);
      return;
    case "ts":
    case "js":
      editDoc(editor, ob.tsForwardSlurp);
      return;
    case "tsx":
    case "jsx":
      editDoc(editor, ob.tsxForwardSlurp);
      return;
  }
};

const forwardBarf: Handler = () => {
  const editor = window.activeTextEditor;
  const fileExtension = getFileExtension(editor?.document);
  switch (fileExtension) {
    case "html":
      editDoc(editor, ob.htmlForwardBarf);
      return;
  }
};

const kill: Handler = () => {
  const editor = window.activeTextEditor;
  const fileExtension = getFileExtension(editor?.document);
  switch (fileExtension) {
    case "html":
      editDoc(editor, ob.htmlKill);
      return;
  }
};

const raise: Handler = () => {
  const editor = window.activeTextEditor;
  const fileExtension = getFileExtension(editor?.document);
  switch (fileExtension) {
    case "html":
      editDoc(editor, ob.htmlRaise);
      return;
  }
};

const commands: Command[] = [
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
