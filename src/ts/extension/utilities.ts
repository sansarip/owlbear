import {
  commands,
  Position,
  Range,
  Selection,
  TextDocument,
  TextEditor,
  TextEditorEdit,
} from "vscode";
import { DocCtx, EditCtx, Point } from "./types";
import output from "./output";
import path = require("path");

export const textRange = (document: TextDocument): Range => {
  const firstLine = document.lineAt(0);
  const lastLine = document.lineAt(document.lineCount - 1);
  return new Range(firstLine.range.start, lastLine.range.end);
};

export const getCurrentOffset = (editor: TextEditor): number => {
  return editor.document.offsetAt(editor.selection.active);
};

export const moveCursor = (
  editor: TextEditor,
  newCursorOffset: number
): boolean => {
  const currentOffset = getCurrentOffset(editor);
  if (isNaN(newCursorOffset) || newCursorOffset === currentOffset) {
    return false;
  }
  const newCursorPosition: Position =
    editor.document.positionAt(newCursorOffset);
  const newCursorRange: Range = new Range(newCursorPosition, newCursorPosition);
  const newCursorSelection: Selection = new Selection(
    newCursorPosition,
    newCursorPosition
  );
  editor.selection = newCursorSelection;
  editor.revealRange(newCursorRange);
  return true;
};

export const edit = async (
  editor: TextEditor,
  editCtx: EditCtx,
  shouldFormat = true
): Promise<EditCtx | undefined> => {
  const { src: newSrc, offset: newCursorOffset } = editCtx;
  const didEdit = await editor.edit((editBuilder: TextEditorEdit) => {
    let didReplace = false;
    // Replace the entire document with the new text
    if (newSrc) {
      editBuilder.replace(textRange(editor.document), newSrc);
      didReplace = true;
    }

    // Move the cursor to the new offset
    if (newCursorOffset) {
      moveCursor(editor, newCursorOffset);
    }

    const didEdit = didReplace || !!newCursorOffset;
    return didEdit;
  });

  // Format doc if src/cursor edits were made
  if (shouldFormat && didEdit) {
    await commands.executeCommand("editor.action.formatDocument");
  }

  return didEdit ? editCtx : undefined;
};

export const getFileExtension = (document: TextDocument | undefined) =>
  document?.uri.toString().split(".")?.pop()?.toLowerCase();

export const getDocCtx = (editor: TextEditor): DocCtx => {
  const document = editor.document;
  const cursorOffset = document.offsetAt(editor.selection.active);
  const source = document.getText();
  const docUri = document.uri.toString();
  return { src: source, offset: cursorOffset, docUri };
};

export const asPoint = (pos: Position): Point => ({
  row: pos.line,
  column: pos.character,
});

export const log = (msg: string) => {
  output.append(msg);
};

export const makePath = (...pathNames: string[]): string =>
  pathNames.join(path.sep);

export const isEmptyObj = (obj: object): boolean =>
  Object.keys(obj).length === 0;

export const localTimeNow = (): string => new Date().toLocaleTimeString();

export const selectRange = (
  editor: TextEditor,
  startIndex: number,
  endIndex: number
): void => {
  const document = editor.document;
  const startPosition = document.positionAt(startIndex);
  const endPosition = document.positionAt(endIndex);
  const customSelection = new Selection(startPosition, endPosition);
  editor.selection = customSelection;
};

export const copyRangeToClipboard = async (
  editor: TextEditor,
  startIndex: number,
  endIndex: number
): Promise<void> => {
  selectRange(editor, startIndex, endIndex);
  await commands.executeCommand("editor.action.clipboardCopyAction");
};

export const cutRangeToClipboard = async (
  editor: TextEditor,
  startIndex: number,
  endIndex: number
): Promise<void> => {
  selectRange(editor, startIndex, endIndex);
  await commands.executeCommand("editor.action.clipboardCutAction");
};
