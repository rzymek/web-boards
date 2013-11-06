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
//        var trace = svg.getElementById('trace');
//        var from = trace.pathSegList.getItem(0);
//        var to = trace.pathSegList.getItem(1);
//        from.x = context.from.x.baseVal
//        svg.getElementById('traces')
        return context;
    },
    undo: function(data, context) {
        move(context.counter, context.from);
    }
};
