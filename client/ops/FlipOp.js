FlipOp = function(data) {
    var svg = document.getElementById('svg');
    var counter = svg.getElementById(data.counterId);
    var oldHref = counter.href.baseVal;
    var newSideIdx = (counter.side + 1) % counter.sides.length;
    var newSide = counter.sides[newSideIdx];
    counter.href.baseVal = counter.href.baseVal.replace(/([^/])*$/, newSide);
    counter.side = newSideIdx;
    return function() {
        counter.href.baseVal = oldHref;
    };
};