PlaceOp = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    if (hex === null) {
        console.warn('Hex not found:', data.hexid);
    } else {
        var counter = document.createElementNS(SVGNS, 'image');
        var boardScaling = getBoardScaling();
        counter.width.baseVal.value = data.size.width / boardScaling;
        counter.height.baseVal.value = data.size.height / boardScaling;
        counter.id = data._id;
        counter.sides = data.sides;
        counter.side = 0;
//            counter.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", data.image);
        counter.href.baseVal = data.imageBase + data.sides[0];
        svg.getElementById('counters').appendChild(counter);

        addToStack(hex, counter);
        alignStack(hex);
        return function() {
            removeFromStack(counter.position, counter);
            svg.getElementById('counters').removeChild(counter);
            alignStack(counter.position);

        }
    }
};