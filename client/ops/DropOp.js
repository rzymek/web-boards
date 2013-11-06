DropOp = {
    run: function(data) {
        var svg = document.getElementById('svg');
        var counter = svg.getElementById(data.counterId);
        counter.parentElement.removeChild(counter);
        return counter;
    },
    undo: function(data, c) {
        var svg = document.getElementById('svg');
        var counters = svg.getElementById('counters');
        counters.appendChild(c);
    }
};