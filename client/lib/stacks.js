
removeFromStack = function(target, img) {
    target.stack = target.stack.filter(function(it) {
        return it !== img;
    });
};

addToStack = function(hex, counter) {
    counter.position = hex;
    if (hex.stack)
        hex.stack.push(counter);
    else
        hex.stack = [counter];
};