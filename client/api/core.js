/// <reference path="core.api.d.ts"/>
var HTMLCounter = (function () {
    function HTMLCounter(node) {
        this.img = node;
    }
    HTMLCounter.prototype.getImage = function () {
        return this.img.src;
    };
    return HTMLCounter;
})();
window['HTMLCounter'] = HTMLCounter;

var GameCtx = (function () {
    function GameCtx() {
    }
    return GameCtx;
})();
ctx = new GameCtx();
