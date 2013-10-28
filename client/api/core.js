/// <reference path="core.api.d.ts"/>
/// <reference path="../../packages/typescript-libs/lib.d.ts"/>
var HTMLCounter = (function() {
    function HTMLCounter(node) {
        this.img = node;
    }
    HTMLCounter.prototype.getImage = function() {
        return this.img.src;
    };
    return HTMLCounter;
})();
this.HTMLCounter = HTMLCounter;

ctx = {
    selected: null,
    menu: {},
    opBacktrack: null,
    places: {}
};

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

var STACKS = 'wb-stacks';
alignStack = function alignStack(area, counters) {
    var areaBBox = area.getBBox();
    var spacing = 3;
    var counterDim = {width: 75, height: 75};
    var placing = getPlacing(counters, counterDim, areaBBox, spacing)
    var startx = (areaBBox.width - (placing.width * (counterDim.width + spacing))) / 2;
    var starty = (areaBBox.height - (placing.height * (counterDim.height + spacing))) / 2;

    startx += areaBBox.x;
    starty += areaBBox.y;
    var x = 0;
    var y = 0;
    var stackOffset = 0;
    var layer = 0;
    var countersOnLayer = 0;
    stackRoots = {};
    for (var i = 0; i < counters.length; i++) {
        var counter = counters[i];
        var cx = startx + x + stackOffset;
        var cy = starty + y + stackOffset;
        counter.x.baseVal.value = cx;
        counter.y.baseVal.value = cy;
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
}
 