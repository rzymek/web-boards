/// <reference path="core.api.d.ts" />
interface Counter {
    getImage(): string;
}
declare class HTMLCounter implements Counter {
    public img: HTMLImageElement;
    constructor(node: HTMLImageElement);
    public getImage(): string;
}
declare class GameCtx {
    public selected: Counter;
}
