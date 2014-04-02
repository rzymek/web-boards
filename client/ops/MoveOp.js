function move(counter, to) {
    removeFromStack(counter.position, counter);
    alignStack(counter.position);
    addToStack(to, counter);
    alignStack(to);
}

placeArrow = function(trace, from, target, layer) {
    var start = trace.pathSegList.getItem(0);
    var to = trace.pathSegList.getItem(1);
    start.pathSegTypeAsLetter = 'M';
    start.x = from.rx;
    start.y = from.ry;
    to.pathSegTypeAsLetter = 'L';
    to.x = target.rx;
    to.y = target.ry;
    //TOOD: replace 45 with counterDim
    var point = trace.getPointAtLength(trace.getTotalLength() - 45);
    to.x = point.x;
    to.y = point.y;
    byId(layer).appendChild(trace);
    return trace;
};

MoveOp = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.to);
    var counter = svg.getElementById(data.counter);
    var trace = sprites.trace.cloneNode(true);
    var from = counter.position;
    move(counter, hex);
    placeArrow(trace, from, counter.position, 'traces');
    return function() {
        move(counter, from);
        trace.remove();
    };
};
