RollOp = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    var bbox = hex.getBBox();
    var overlay = document.createElementNS(SVGNS, 'image');
    var w = bbox.width / 2;
    var h = bbox.height / 2;
    overlay.width.baseVal.value = w;
    overlay.height.baseVal.value = h;
    overlay.x.baseVal.value = -w / 2;
    overlay.y.baseVal.value = -h / 2;
    overlay.id = data._id;
    overlay.href.baseVal = '/img/dice.svg?x=' + data.result.roll + '&y=' + data.result.type;
    addOverlay(hex, overlay);
    copyTransformation(hex, overlay);
    svg.getElementById('overlays').appendChild(overlay);
    return function() {
        if (hex.overlay)
            hex.overlay = hex.overlay.splice(hex.overlay.indexOf(overlay));
        overlay.remove();
    };
}