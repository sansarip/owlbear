import { TextDocument, window } from "vscode";
import { replace } from "../../utilities";

var ob = require("../../../../../out/cljs/owlbear");

const editDoc = (getEditCtx: Function) => {
  const editor = window.activeTextEditor;
  if (editor) {
    const document: TextDocument = editor.document;
    const cursorOffset: number = document.offsetAt(editor.selection.active);
    const source: string = document.getText();
    const editCtx = getEditCtx(source, cursorOffset);
    if (!editCtx) { return; }
    replace(editor, editCtx.src, cursorOffset, editCtx.offset);
  }
};

export const forwardBarf = () => {
  editDoc(ob.htmlForwardBarf);
};

export const forwardSlurp = () => {
  editDoc(ob.htmlForwardSlurp);
};

export const kill = () => {
  editDoc(ob.htmlKill);
};

export const raise = () => {
  editDoc(ob.htmlRaise);
};
