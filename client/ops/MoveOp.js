function move(counter, to) {
    removeFromStack(counter.position, counter);
    alignStack(counter.position);
    addToStack(to, counter);
    alignStack(to);
}

MoveOp = {
    run: function(data) {
        var svg = document.getElementById('svg');
        var hex = svg.getElementById(data.to);
        var counter = svg.getElementById(data.counter);
        var trace = svg.getElementById('trace').cloneNode();
        var from = counter.position;
        move(counter, hex);

        var start = trace.pathSegList.getItem(0);
        var to = trace.pathSegList.getItem(1);
        start.x = from.rx;
        start.y = from.ry;
        to.x = counter.position.rx - start.x;
        to.y = counter.position.ry - start.y;
        svg.getElementById('traces').appendChild(trace);

        return {
            counter: counter,
            from: from,
            trace:trace
        };
    },
    undo: function(data, context) {
        move(context.counter, context.from);
        if(context.trace.parentElement) {
            context.trace.parentElement.removeChild(context.trace);
        }
    }
};
