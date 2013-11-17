function getPlacing(counters, counterDim, areaBBox, spacing) {
    var width = areaBBox.width;
    var maxSlots = (areaBBox.height / (counterDim.height + spacing) + 0.5);
    var rows = Math.ceil(counters.length / width);
    return {
        width: Math.floor(width / (counterDim.width + spacing)),
        height: Math.floor(Math.min(maxSlots, rows))
    }
}
function bringToTop(e) {
    e.parentElement.appendChild(e);
}

copyTransformation = function(src, dest) {
    dest.setAttribute('transform', src.getAttribute('transform').replace(new RegExp('scale\(.*\)'), ''));
};

var STACKS = 'wb-stacks';
alignStack = function(area/*SVGElement*/) {
    var counters = area.stack;
    var areaBBox = area.getBBox();
    var spacing = 3;

    if(counters === undefined || counters.length === 0) {
        return;
    }
    var counterDim = { //TODO: get max dim
        width: counters[0].width.baseVal.value,
        height: counters[0].height.baseVal.value
    };
    var placing = getPlacing(counters, counterDim, areaBBox, spacing)
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
        counter.x.baseVal.value = -counter.width.baseVal.value / 2 + cx;
        counter.y.baseVal.value = -counter.height.baseVal.value / 2 + cy;
        if (layer > 0) {
            var id = counter.id;
            var stacksWith = counters[i - countersOnLayer].id;
            stackRoots[stacksWith] = undefined;
            stackRoots[id] = true;
            counter.setAttribute(STACKS, stacksWith);
        } else {
            counter.removeAttribute(STACKS);
        }
        bringToTop(counter);
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
    area.setAttribute(STACKS, Object.keys(stackRoots).join(' '));
};

function getSVGElements(stack/*string[] -ids*/) {
    var e = [];
    for (var i = 0; i < stack.length; i++) {
        e.push(svg.getElementById(stack[i]));
    }
    return e;
}

showStackSelector = function(hexElement/*SVGUseElement*/, stack/*SVGImageElement[]*/) {
    var margin = 10;
    var hexBB = hexElement.getBBox();
    var svg = byId('svg');
    var selector = svg.getElementById('stackSelector');
    copyTransformation(hexElement, selector);

    var img = stack[0];//open at
    selector.x.baseVal.value = img.x.baseVal.value - margin;
    selector.y.baseVal.value = img.y.baseVal.value - margin;
    selector.style.visibility = 'visible';
    var gstack = stack.concat().reverse();
    var size = Math.sqrt(gstack.length);
    var width = Math.ceil(size);
    var height = Math.floor(size + 0.5);
    //TODO::getMaxCounterSize(gstack);
    var maxCounterSize = {
        width: gstack[0].width.baseVal.value, 
        height: gstack[0].height.baseVal.value
    };
    width = margin + width * maxCounterSize.width + margin;
    height = margin + Math.floor(height * maxCounterSize.height) + margin;
    selector.width.baseVal.value = width;
    selector.height.baseVal.value = height;
    svg.getElementById('overlays').appendChild(selector);
    selector.stack = gstack;
    alignStack(selector);
    selector.atHex = hexElement;
    stack.forEach(function(it) {
        it.style.pointerEvents = 'auto';
        it.onclick = function(evt) {
            var val = evt.target.id;
            if (Session.equals('selectedPiece', val))
                val = null;
            Session.set('selectedPiece', val);
        };
    });
};
