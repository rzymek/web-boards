/// <reference path="core.api.d.ts"/>
var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
var Counter = (function () {
    function Counter() {
    }
    return Counter;
})();

var HTMLCounter = (function (_super) {
    __extends(HTMLCounter, _super);
    function HTMLCounter(node) {
        _super.call(this);
        this.img = node;
    }
    return HTMLCounter;
})(Counter);
window['HTMLCounter'] = HTMLCounter;

var GameCtx = (function () {
    function GameCtx() {
    }
    return GameCtx;
})();
ctx = new GameCtx();
