export type EditCtx = { src: string; offset: number; removedText: string };
export type DocCtx = Pick<EditCtx, "src" | "offset">;
export type OwlbearFunction = (src: string, offset: number) => EditCtx;
