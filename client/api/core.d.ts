/// <reference path="core.api.d.ts" />
declare class Counter {
}
declare class HTMLCounter extends Counter {
    public img: HTMLImageElement;
    constructor(node: HTMLImageElement);
}
declare class GameCtx {
    public selected: Counter;
}
