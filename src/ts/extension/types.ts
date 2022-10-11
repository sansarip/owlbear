export type EditCtx = { src: string; offset: number; removedText: string };
export type DocCtx = {docUri: string} & Pick<EditCtx, "src" | "offset">;
export type OwlbearFunction = (src: string, offset: number, treeId: string) => EditCtx;
export type Point = { row: number; column: number };
