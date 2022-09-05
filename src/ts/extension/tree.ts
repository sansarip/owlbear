import { randomUUID } from "crypto";
import { TextDocumentChangeEvent, TextDocument } from "vscode";
import { asPoint } from "./utilities";

const ob = require("../../../out/cljs/owlbear");
export const docUriToTreeIdMap: { [key: string]: string } = {};

export const setNewTreeIdForDocUri = (docUri: string): string => {
  const treeId = randomUUID();
  docUriToTreeIdMap[docUri] = treeId;
  return treeId;
};

// Code [mostly] copied from https://github.com/georgewfraser/vscode-tree-sitter/blob/471169a992a8222329f9649e7e15f5c42d9097a1/src/extension.ts#L105
export const editTree = (event: TextDocumentChangeEvent) => {
  const document = event.document;
  const docUri = document.uri.toString();
  const treeId = docUriToTreeIdMap[docUri];
  if (event.contentChanges.length === 0 || !treeId) {
    return;
  }
  for (const e of event.contentChanges) {
    const startIndex = e.rangeOffset;
    const oldEndIndex = e.rangeOffset + e.rangeLength;
    const newEndIndex = e.rangeOffset + e.text.length;
    const startPos = document.positionAt(startIndex);
    const oldEndPos = document.positionAt(oldEndIndex);
    const newEndPos = document.positionAt(newEndIndex);
    const startPosition = asPoint(startPos);
    const oldEndPosition = asPoint(oldEndPos);
    const newEndPosition = asPoint(newEndPos);
    const delta = {
      startIndex,
      oldEndIndex,
      newEndIndex,
      startPosition,
      oldEndPosition,
      newEndPosition,
    };
    ob.editTree(document.getText(), treeId, delta);
  }
};

export const deleteTree = (document: TextDocument) => {
  const docUri = document.uri.toString();
  const treeId = docUriToTreeIdMap[docUri];
  if (!treeId) {
    return;
  }
  ob.deleteTree(treeId);
  delete docUriToTreeIdMap[document.uri.toString()];
};
