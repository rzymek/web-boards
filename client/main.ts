/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="svg_zoom_and_pan.d.ts"/>
/// <reference path="../common/model.d.ts"/>

var svgns = "http://www.w3.org/2000/svg";

function setupTemplate() {
    var main = Template['main'];
    main['game'] = 'bastogne';
    main['board'] = {w: 6800, h: 4400};
}

function setupGrid() {
    var board = {w: 6800, h: 4400};
    var path = <SVGPathElement><any>document.getElementById('hex');
    var svg = <SVGSVGElement><any>document.getElementById('svg');
    var hex2hex = { y: 125.29, x: 108.5};
    var A = 2 * 125.29 / Math.sqrt(3);
    var hexes = [];
    var use:SVGUseElement;
    var yn = board.h / hex2hex.y;
    var xn = board.w / hex2hex.x;
    for (var y = 0; y < yn; y++) {
        var s = A / 2;
        for (var x = 0; x < xn; x++) {
            var hx = 80 + x * hex2hex.x;
            var hy = 65 + y * hex2hex.y;
            var hscale = A / 100;
            if (x % 2) {
                hy -= hex2hex.y / 2;
            }
            use = <SVGUseElement>document.createElementNS(svgns, 'use');
            use.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", '#hex');
            use.setAttribute('transform', 'translate(' + hx + ' ' + hy + ') scale(' + hscale + ')');
            use.setAttribute('class', 'hex');
            svg.appendChild(use);
        }
    }
    svg.viewBox.baseVal.x = 0;
    svg.viewBox.baseVal.y = 0;
    svg.viewBox.baseVal.width = board.w;
    svg.viewBox.baseVal.height = board.h;
    svg.setAttribute('width', board.w.toString());
    svg.setAttribute('height', board.h.toString());
    svg.style.width = '100%';
    svg.style.height = '100%';
    return svg;
}

var operations = function () {
    Operations.find().observeChanges({
       added: (id,field) => {

       }
    });
};
Meteor.startup(function () {
    var svg = setupGrid();
    svgZoomAndPan.setup(svg);
    operations();

});
setupTemplate();
