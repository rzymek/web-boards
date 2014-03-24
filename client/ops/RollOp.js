RollOp = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    var bbox = getTBBox(hex);
    var overlay = byId(data._id);
    if (overlay === null) {
        overlay = document.createElementNS(SVGNS, 'image');
        var w = bbox.width*0.6;
        var h = bbox.height*0.6;
        overlay.width.baseVal.value = w;
        overlay.height.baseVal.value = h;
        overlay.x.baseVal.value = -w / 2;
        overlay.y.baseVal.value = -h / 2;
        overlay.id = data._id;
    }


    var param = data.result
            ? '?x=' + data.result.roll + '&y=' + data.result.type
            : '?y=' + data.server.roll.count + 'd' + data.server.roll.sides;
    overlay.href.baseVal = '/img/dice.svg' + param;
    overlay.style.pointerEvents = 'auto';
    overlay.onclick = function() {
        Operations.insert({
            op: 'RemoveElementOp',
            element: overlay.id
        });
    };
    copyTransformation(hex, overlay);
    svg.getElementById('overlays').appendChild(overlay);
    return function() {
        overlay.remove();
    };
};