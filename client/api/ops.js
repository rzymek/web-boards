/// <reference path="../../common/model.d.ts"/>
var svgns = "http://www.w3.org/2000/svg";

var PlaceOperation = (function () {
    function PlaceOperation(data) {
        this.data = data;
    }
    PlaceOperation.prototype.run = function () {
        var svg = document.getElementById('svg');
        var use = svg.getElementById(this.data.hexid);
        if (use === null) {
            console.warn('Hex not found:', this.data.hexid);
        } else {
            var img = document.createElementNS(svgns, 'image');
            img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.data.image);
            img.setAttribute('transform', use.getAttribute('transform').replace(new RegExp('scale\(.*\)'), ''));
            img.width.baseVal.value = 75;
            img.height.baseVal.value = 75;
            img.id = "at_"+use.id;
            svg.getElementById('counters').appendChild(img);
            
            var target = ctx.getPlace(this.data.hexid);
            target.stack.push(img);
            alignStack(use, target.stack);
        }
    };

    PlaceOperation.prototype.undo = function () {
        var svg = document.getElementById('svg');
        var img = svg.getElementById("at_"+this.data.hexid);
        svg.getElementById('counters').removeChild(img);
        var target = ctx.getPlace(this.data.hexid);
        target.stack = target.stack.filter(function (it){
            return it !== img; 
        });
        console.log(ctx.places[this.data.hexid]);
        alignStack(svg.getElementById(this.data.hexid), target.stack);
    };
    return PlaceOperation;
})();
window['PlaceOperation'] = PlaceOperation;
