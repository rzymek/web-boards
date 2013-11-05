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
        var context = {
            counter: counter,
            from: counter.position
        };
        move(context.counter, hex);
        return context;
    },
    undo: function(data, context) {
        move(context.counter, context.from);
    }
};
