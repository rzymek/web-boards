HTMLCounter = (function() {
    function HTMLCounter(node) {
        this.img = node;
    }
    HTMLCounter.prototype.getImage = function() {
        return this.img.src;
    };
    return HTMLCounter;
})();
SVGCounter = (function() {
    function SVGCounter(node) {
        this.img = node;
    }
    SVGCounter.prototype.getImage = function() {
        return this.img.href.baseVal;
    };
    return SVGCounter;
})();

ctx = {
    selected: null,
    menu: {},
    replayIndex: null,
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

function copyTransformation(src, dest) {
    dest.setAttribute('transform', src.getAttribute('transform').replace(new RegExp('scale\(.*\)'), ''));
}

var STACKS = 'wb-stacks';
alignStack = function(area/*SVGElement*/) {
    var counters = area.stack;
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
        copyTransformation(area, counter);
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
    var maxCounterSize = {width: 75, height: 75};//TODO::getMaxCounterSize(gstack);
    width = margin + width * maxCounterSize.width + margin;
    height = margin + Math.floor(height * maxCounterSize.height) + margin;
    selector.width.baseVal.value = width;
    selector.height.baseVal.value = height;
    svg.getElementById('overlays').appendChild(selector);
    selector.stack = gstack;
    alignStack(selector);
    selector.atHex = hexElement;
    stack.forEach(function(it){
       it.style.pointerEvents='auto'; 
       it.onclick = function(evt) {
           var val = evt.target.id;
           if(Session.equals('selectedPiece', val))
               val = null;
           Session.set('selectedPiece', val);
       };
    });
    //selector.style.display = '';
//		showingStackSelector = position;
//		getSVGElement(position.getSVGId()).removeAttribute(STACKS);
//		updateSelectionRect();
//		stackSelectorContents = stack;
//		stackSelectorPosition = position;
};
