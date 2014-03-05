
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