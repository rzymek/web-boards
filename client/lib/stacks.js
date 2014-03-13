
removeFromStack = function(target, img) {
    target.stack = target.stack.filter(function(it) {
        return it !== img;
    });
};

addToStack = function(hex, counter) {
    counter.position = hex;
    hex.stack = addTo(hex.stack, counter);
};

addOverlay = function(hex, overlay) {
    hex.overlays = addTo(hex.overlays, overlay);
};

addTo = function(list, element) {
    if (list) {
        list.push(element);
    } else {
        list = [element];
    }
    return list;
};

hideStackSelector = function() {
    var selector = sprites.stackSelector;
    if(selector.style.visibility === 'hidden')
        return;
    selector.style.visibility = 'hidden';
//    var counters = byId('counters');
//    (selector.stack || []).forEach(function(it) {
//        if (it.restoreRotation !== undefined) {
//            it.transform.baseVal.insertItemBefore(it.restoreRotation, 3);
//        }
//        delete it.restoreRotation;
//        counters.appendChild(it);
//    });
};

showStackSelector = function(hexElement/*SVGUseElement*/, stack/*SVGImageElement[]*/) {
    var margin = 10;
    var svg = byId('svg');
    var selector = sprites.stackSelector;
    copyTransformation(hexElement, selector);
    var info = Session.get('gameInfo');
    var maxCounterSize = info.counterDim;

    selector.x.baseVal.value = -info.counterDim.width/2 - margin;
    selector.y.baseVal.value = -info.counterDim.height/2 - margin;
    selector.style.visibility = 'visible';
    var gstack = stack.concat().reverse();
    var size = Math.sqrt(gstack.length);
    var width = Math.ceil(size);
    var height = Math.floor(size + 0.5);

    width = margin + width * maxCounterSize.width + margin;
    height = margin + Math.floor(height * maxCounterSize.height) + margin;
    selector.width.baseVal.value = width;
    selector.height.baseVal.value = height;
    var layer = svg.getElementById('stackSelectorLayer');
    layer.appendChild(selector);
    stack.forEach(function(it) {
        layer.appendChild(it);
//        console.log(it, it.transform.baseVal.numberOfItems);
//        if (it.transform.baseVal.numberOfItems > 2) {
//            it.restoreRotation = it.transform.baseVal.getItem(TX.ROTATE);
////            .setRotate(0,0,0);
//        }
    });
    selector.stack = gstack;
    alignStack(selector);
    selector.atHex = hexElement;
    stack.forEach(function(it) {
        it.style.pointerEvents = 'auto';
        it.onclick = function(evt) {
            console.log('stack click', evt);
            var val = it.id;
            if (getSelectedId() === val)
                val = null;
            selectById(val);
            evt.stopPropagation();
        };
    });
};
