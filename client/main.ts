/// <reference path="../private/lib.d.ts" />
declare var Meteor:any;

var svgns = "http://www.w3.org/2000/svg";

Meteor.startup(function () {
    // var path = <SVGPathElement>document.getElementsByTagNameNS('http://www.w3.org/2000/svg','path').item(0);
    var path = <SVGPathElement><any>document.getElementById('hex');
    var svg = <SVGSVGElement><any>document.getElementById('svg');
    // window.console.log(path.getBBox().width)
    var hex2hex = { y:125.29, x:108.5};
    var A = 2 * 125.29 / Math.sqrt(3) / 100;
    var hexes = [];
    var use:SVGUseElement;
    for(var y=0;y<10;y++)  {
        var h = {
            'x': 80,
            'y': 65 + y * hex2hex.y,
            'scale': A
        };
        use = <SVGUseElement>document.createElementNS(svgns, 'use');
        use.setAttributeNS("http://www.w3.org/1999/xlink","xlink:href",'#hex');
        use.setAttribute('transform','translate('+h.x+' '+h.y+') scale('+A+')');
        svg.appendChild(use);
    }
});
