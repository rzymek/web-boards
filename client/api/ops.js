/// <reference path="../../common/model.d.ts"/>
var svgns = "http://www.w3.org/2000/svg";

var PlaceOperation = (function () {
    function PlaceOperation(data) {
        this.data = data;
    }
    PlaceOperation.prototype.run = function () {
        console.log(this.data);
        var svg = document.getElementById('svg');
        var img = document.createElementNS(svgns, 'image');
        img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.data.image);
        var use = svg.getElementById(this.data.hexid);
        if (use === null) {
            console.warn('Hex not found:', this.data.hexid);
        } else {
            img.setAttribute('transform', use.getAttribute('transform').replace(new RegExp('scale\(.*\)'), ''));
            img.width.baseVal.value = 75;
            img.height.baseVal.value = 75;
            svg.getElementById('counters').appendChild(img);
        }
    };

    PlaceOperation.prototype.undo = function () {
    };
    return PlaceOperation;
})();
window['PlaceOperation'] = PlaceOperation;
