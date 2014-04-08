ClearMarkersOp = function(data) {
    var counter = byId(data.counterId);
    var removed = [];
    while (counter.children.length > 1) {
        var child = counter.removeChild(counter.lastChild);
        removed.push(child);
    }
    return function() {
        for (var i = removed.length - 1; i >= 0; i--) {
            counter.appendChild(removed[i]);
        };
    }
};