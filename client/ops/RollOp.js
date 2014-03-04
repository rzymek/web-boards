RollOp = function(data) {
    var scale = Session.get('gameInfo').counterScale;
    console.info(data.result.roll);
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    var bbox = hex.getBBox();
    var overlay = document.createElementNS(SVGNS, 'image');
    var w = bbox.width * scale;
    var h = bbox.height * scale;
    overlay.width.baseVal.value = w;
    overlay.height.baseVal.value = h;
    overlay.x.baseVal.value = -w/2;
    overlay.y.baseVal.value = -h/2;
    overlay.id = data._id;
    overlay.href.baseVal = '/img/d' + data.result.roll + '.svg';
    var backupOverlays = hex.overlays;
    addOverlay(hex, overlay);
    copyTransformation(hex, overlay);
    svg.getElementById('overlays').appendChild(overlay);
    return function() {
        hex.overlay = backupOverlays;
        overlay.remove();
    };
}