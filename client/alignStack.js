function getPlacing(counters, counterDim, areaBBox, spacing) {
    var width = areaBBox.width;
    var maxSlots = (areaBBox.height / (counterDim.height + spacing) + 0.5);
    var rows = Math.ceil(counters.length / width);
    return {
        width: Math.floor(width / (counterDim.width + spacing)),
        height: Math.floor(Math.min(maxSlots, rows))
    };
}

function bringToTop(e) {
    e.parentElement.appendChild(e);
}

alignStack = function(area/*SVGElement*/) {
    var svg = byId('svg');
    var counters = area.stack;
    var areaBBox = area.getBBox();
    var spacing = 3;

    if(counters === undefined || counters.length === 0) {
        return;
    }
    var counterDim = Session.get('gameInfo').counterDim;
    var placing = getPlacing(counters, counterDim, areaBBox, spacing);
    var startx = (areaBBox.width - (placing.width * (counterDim.width + spacing))) / 2;
    var starty = (areaBBox.height - (placing.height * (counterDim.height + spacing))) / 2;

    startx += area.rx;
    starty += area.ry;
    var x = 0;
    var y = 0;
    var stackOffset = 0;
    var layer = 0;
    var countersOnLayer = 0;
    stackRoots = {};
    for (var i = 0; i < counters.length; i++) {
        var counter = counters[i];
        var cx = x + stackOffset;
        var cy = y + stackOffset;
        copyTransformation(area, counter);
        var transformList = ensureTransformListSize(svg, counter.transform.baseVal, 4);
        transformList.getItem(TX.OFFSET).setTranslate(cx,cy);
        if (layer > 0) {
            var id = counter.id;
            var stacksWith = counters[i - countersOnLayer].id;
            stackRoots[stacksWith] = undefined;
            stackRoots[id] = true;
        }
        bringToTop(counter);
        counterDim = getTBBox(counter);
        x += counterDim.width + spacing;
        if (x + counterDim.width + spacing > areaBBox.width) {
            x = 0;
            y += counterDim.height + spacing;
        }
        if (y + counterDim.height + spacing > areaBBox.height) {
            x = 0;
            y = 0;
            stackOffset += 5;
            layer++;
            if (layer === 1) {
                countersOnLayer = i + 1;
            }
        }
    }
    updateSelection();
};

function getSVGElements(stack/*string[] -ids*/) {
    var e = [];
    for (var i = 0; i < stack.length; i++) {
        e.push(svg.getElementById(stack[i]));
    }
    return e;
}

