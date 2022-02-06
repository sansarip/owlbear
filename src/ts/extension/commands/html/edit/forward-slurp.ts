import { TextDocument, window } from "vscode";
import { replace } from "../../../utilities";

var ob = require("../../../../../../out/cljs/owlbear");

export const forwardSlurp = () => {
  const editor = window.activeTextEditor;
  if (editor) {
    const document: TextDocument = editor.document;
    const cursorOffset: number = document.offsetAt(
      editor.selection.active
    );
    const source: string = document.getText();
    const ctx = ob.htmlForwardSlurp(source, cursorOffset);
    replace(editor, ctx.src, cursorOffset, ctx.offset);
  }
};
