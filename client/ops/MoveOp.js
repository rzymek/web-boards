function move(counter, to) {
    removeFromStack(counter.position, counter);
    alignStack(counter.position);
    addToStack(to, counter);
    alignStack(to);
}

MoveOp = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.to);
    var counter = svg.getElementById(data.counter);
    var trace = svg.getElementById('trace').cloneNode();
    var from = counter.position;
    move(counter, hex);

    var start = trace.pathSegList.getItem(0);
    var to = trace.pathSegList.getItem(1);
    start.pathSegTypeAsLetter = 'M';
    start.x = from.rx;
    start.y = from.ry;
    to.pathSegTypeAsLetter = 'L';
    to.x = counter.position.rx;
    to.y = counter.position.ry;
    svg.getElementById('traces').appendChild(trace);

    return function() {
        move(counter, from);
        if (trace.parentElement) {
            trace.parentElement.removeChild(trace);
        }
    };
};
