/// <reference path="core.api.d.ts"/>
/// <reference path="../../packages/typescript-libs/lib.d.ts"/>
var HTMLCounter = (function () {
    function HTMLCounter(node) {
        this.img = node;
    }
    HTMLCounter.prototype.getImage = function () {
        return this.img.src;
    };
    return HTMLCounter;
})();
this.HTMLCounter = HTMLCounter;

var GameCtx = (function () {
    function GameCtx() {
        this.selected = null;
        this.menu = {};
        this.opBacktrack = null;
    }
    return GameCtx;
})();
ctx = new GameCtx();
