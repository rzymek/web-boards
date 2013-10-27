/// <reference path="../../common/model.d.ts"/>

var svgns = "http://www.w3.org/2000/svg";

interface PlaceOperationData {
    image:string;
    hexid:string;
}
class PlaceOperation implements Operation {
    data:PlaceOperationData;

    constructor(data:PlaceOperationData) {
        this.data = data;
    }

    run():void {
        var svg = <SVGSVGElement><any>document.getElementById('svg');
        var use = svg.getElementById(this.data.hexid);
        if (use === null) {
            console.warn('Hex not found:', this.data.hexid);
        } else {
            var img = <SVGImageElement>document.createElementNS(svgns, 'image');
            img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.data.image);
            img.setAttribute('transform', use.getAttribute('transform').replace(new RegExp('scale\(.*\)'), ''));
            img.width.baseVal.value = 75;
            img.height.baseVal.value = 75;
            svg.getElementById('counters').appendChild(img);
        }
    }

    undo():void {
    }

}
window['PlaceOperation'] = PlaceOperation;