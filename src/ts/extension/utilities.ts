import {
  commands,
  Position,
  Range,
  Selection,
  TextDocument,
  TextEditor,
  TextEditorEdit,
} from "vscode";

export const textRange = (document: TextDocument): Range => {
  const firstLine = document.lineAt(0);
  const lastLine = document.lineAt(document.lineCount - 1);
  return new Range(firstLine.range.start, lastLine.range.end);
};

export const replace = (
  editor: TextEditor,
  newSrc: string,
  cursorOffset: number,
  newCursorOffset: number
) => {
  editor.edit((editBuilder: TextEditorEdit) => {
    if (newSrc) {
      editBuilder.replace(textRange(editor.document), newSrc);
      if (newCursorOffset && newCursorOffset !== cursorOffset) {
        const newCursorPosition: Position = editor.document.positionAt(newCursorOffset);
        const newCursorRange: Range = new Range(newCursorPosition, newCursorPosition);
        const newCursorSelection: Selection = new Selection(
          newCursorPosition,
          newCursorPosition
        );
        editor.selection = newCursorSelection;
        editor.revealRange(newCursorRange);
      }
      commands.executeCommand("editor.action.formatDocument");
    }
  });
};
