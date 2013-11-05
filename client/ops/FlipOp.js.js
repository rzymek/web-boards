var svgns = "http://www.w3.org/2000/svg";

FlipOp = {
    run: function(data) {
        var svg = document.getElementById('svg');
        var counter = svg.getElementById(data.counterId);
        var oldCounterSide = counter.side;
        var newSideIdx = (counter.side+1) % counter.sides.length;
        var newSide = counter.sides[newSideIdx];
        counter.href.baseVal.replace(/([^/])*$/, newSide);
        counter.side = newSideIdx;
        return {
            counter: counter,
            oldCounterSide: oldCounterSide
        }
    },
    undo: function(data, c) {
        var newSide = c.counter.sides[c.oldCounterSide];
        counter.href.baseVal.replace(/([^/])*$/, newSide);
    }
};