FlipOp = function(data) {
    var counter = byId(data.counterId);
    var img = counter.children[0];
    var oldHref = img.href.baseVal;
    var oldSideIdx = counter.side;
    var newSideIdx = (counter.side + 1) % counter.sides.length;
    var newSide = counter.sides[newSideIdx];
    img.href.baseVal = img.href.baseVal.replace(/([^/])*$/, newSide);
    counter.side = newSideIdx;
    return function() {
        img.href.baseVal = oldHref;
        counter.side = oldSideIdx;
    };
};