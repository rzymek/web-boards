ClearMarkersOp = function(data) {
    var counter = byId(data.counterId);
    var removed = [];
    while (counter.children.length > 1) {
        var child = counter.removeChild(counter.lastChild);
        removed.push(child);
    }
    return function() {
        console.log(removed);
        for (var i = removed.length - 1; i >= 0; i--) {
            console.log(restoring)
            counter.appendChild(removed[i]);
        };
    }
};