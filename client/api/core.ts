/// <reference path="core.api.d.ts"/>

class Counter {
}

class HTMLCounter extends Counter {
    img:HTMLImageElement;

    constructor(node:HTMLImageElement) {
        super();
        this.img = node;
    }
}
window['HTMLCounter']=HTMLCounter;

class GameCtx {
    public selected:Counter;
}
ctx = new GameCtx();
