import {
  commands,
  Position,
  Range,
  Selection,
  TextDocument,
  TextEditor,
  TextEditorEdit,
} from "vscode";
import { EditCtx } from "./types";

export const textRange = (document: TextDocument): Range => {
  const firstLine = document.lineAt(0);
  const lastLine = document.lineAt(document.lineCount - 1);
  return new Range(firstLine.range.start, lastLine.range.end);
};

export const replace = async (
  editor: TextEditor,
  cursorOffset: number,
  editCtx: EditCtx
): Promise<EditCtx | undefined> => {
  const { src: newSrc, offset: newCursorOffset } = editCtx;
  const didEdit = await editor.edit((editBuilder: TextEditorEdit) => {
    if (!newSrc) {
      return false;
    }
    editBuilder.replace(textRange(editor.document), newSrc);

    if (isNaN(newCursorOffset) || newCursorOffset === cursorOffset) {
      return false;
    }
    const newCursorPosition: Position =
      editor.document.positionAt(newCursorOffset);
    const newCursorRange: Range = new Range(
      newCursorPosition,
      newCursorPosition
    );
    const newCursorSelection: Selection = new Selection(
      newCursorPosition,
      newCursorPosition
    );
    editor.selection = newCursorSelection;
    editor.revealRange(newCursorRange);
    commands.executeCommand("editor.action.formatDocument");
    return true;
  });
  return didEdit ? editCtx : undefined;
};

export const getFileExtension = (document: TextDocument | undefined) =>
  document?.uri.toString().split(".")?.pop()?.toLowerCase();
