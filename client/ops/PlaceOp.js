PlaceOp = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    if (hex === null) {
        console.warn('Hex not found:', data.hexid);
    } else {
        var info = Session.get('gameInfo');
        var counter = document.createElementNS(SVGNS, 'image');
        counter.width.baseVal.value = info.counterDim.width;
        counter.height.baseVal.value = info.counterDim.height;
        counter.id = data._id;
        counter.sides = data.sides;
        counter.side = 0;
        counter.href.baseVal = '/games/' + getTable({fields:{game:1}}).game + '/images/' + data.sides[0];
        svg.getElementById('counters').appendChild(counter);

        addToStack(hex, counter);
        alignStack(hex);
        return function() {
            removeFromStack(counter.position, counter);
            svg.getElementById('counters').removeChild(counter);
            alignStack(counter.position);
        };
    }
};