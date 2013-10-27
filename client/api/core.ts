/// <reference path="core.api.d.ts"/>
/// <reference path="../../packages/typescript-libs/lib.d.ts"/>

interface Counter {
    getImage():string;
}

class HTMLCounter implements Counter {
    img:HTMLImageElement;

    constructor(node:HTMLImageElement) {
        this.img = node;
    }

    getImage():string {
        return this.img.src;
    }
}
this.HTMLCounter = HTMLCounter;

class GameCtx {
    public selected:Counter = null;
    public menu:Object={};
}
ctx = new GameCtx();
