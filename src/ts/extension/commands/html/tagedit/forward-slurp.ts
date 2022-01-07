import { Range, window } from "vscode";

var ob = require("../../../../../../out/cljs/owlbear");

export const forwardSlurp = () => {
  const editor = window.activeTextEditor;
  if (editor) {
    const document = editor.document;
    const cursorOffset: number = editor.document.offsetAt(
      editor.selection.active
    );
    const source: string = document.getText();
    const firstLine = document.lineAt(0);
    const lastLine = document.lineAt(document.lineCount - 1);
    const textRange = new Range(firstLine.range.start, lastLine.range.end);
    editor.edit((editBuilder) => {
      const updatedSource: string = ob.htmlForwardSlurp(source, cursorOffset);
      if (updatedSource) {
        editBuilder.replace(textRange, updatedSource);
      }
    });
  }
};
