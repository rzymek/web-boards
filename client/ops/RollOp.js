RollOp = function(data) {
    console.info(data.result.roll);
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    var counter = document.createElementNS(SVGNS, 'image');
    counter.width.baseVal.value = data.size.width;
    counter.height.baseVal.value = data.size.height;
    counter.id = data._id;
    counter.href.baseVal = '/img/d'+data.result.roll+'.svg';
    svg.getElementById('counters').appendChild(counter);

    addToStack(hex, counter);
    alignStack(hex);
    return function() {
        removeFromStack(counter.position, counter);
        svg.getElementById('counters').removeChild(counter);
        alignStack(counter.position);
    };
}