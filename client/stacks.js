
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

showingStackSelector = function() {
    var selector = sprites.stackSelector;
    return (selector.style.visibility !== 'hidden');
};

hideStackSelector = function() {
    var selector = sprites.stackSelector;
    if (selector.style.visibility === 'hidden')
        return;
    selector.style.visibility = 'hidden';
    
    var layer = byId('counters');
    if (selector.atHex) {
        selector.stack.forEach(function(it) {
            it.style.pointerEvents = '';
            it.onclick = undefined;
            layer.appendChild(it);
        });
        alignStack(selector.atHex);
    }
};

function getMaxCounterSize(stack) {
    return stack.map(function(c){
        return getTBBox(c);
    }).reduce(function(a,b){
        return {
            width: Math.max(a.width, b.width),
            height: Math.max(a.height, b.height)
        };
    });
}

showStackSelector = function(hexElement/*SVGUseElement*/, stack) {
    var margin = 10;
    var svg = byId('svg');
    var selector = sprites.stackSelector;
    copyTransformation(hexElement, selector);
    var info = Session.get('gameInfo');
    var maxCounterSize = getMaxCounterSize(stack);

    selector.x.baseVal.value = -info.counterDim.width / 2 - margin;
    selector.y.baseVal.value = -info.counterDim.height / 2 - margin;
    selector.style.visibility = 'visible';
    var gstack = stack.concat().reverse();
    var size = Math.sqrt(gstack.length);
    var width = Math.ceil(size);
    var height = Math.floor(size + 0.5);

    width = margin + width * maxCounterSize.width + margin;
    height = margin + Math.floor(height * maxCounterSize.height) + margin;
    selector.width.baseVal.value = width;
    selector.height.baseVal.value = height;
    var stackSelectorLayer = svg.getElementById('stackSelectorLayer');
    stackSelectorLayer.appendChild(selector);
    stack.forEach(function(it) {
        stackSelectorLayer.appendChild(it);
    });
    selector.stack = gstack;
    alignStack(selector);
    selector.atHex = hexElement;
    stack.forEach(function(counter) {
        counter.style.pointerEvents = 'auto';
        counter.onclick = function(evt) {
            var counterId = counter.id;
            var currectlySelectedId = getSelectedId();
            if (counterId === currectlySelectedId) {
                togglePieceMenu(counter);
            }else{
                var currentSelection = byId(currectlySelectedId);
                if(currentSelection && currentSelection.getAttribute('category') === 'Special') {
                    Special[currentSelection.id].action(counter.position, counter);
                }else{
                    selectById(counterId);   
                }
            }
            evt.stopPropagation();
        };
    });
};
