var svgns = "http://www.w3.org/2000/svg";

PlaceOp = {
    run: function(data) {
        var svg = document.getElementById('svg');
        var hex = svg.getElementById(data.hexid);
        if (hex === null) {
            console.warn('Hex not found:', data.hexid);
        } else {
            var coutner = document.createElementNS(svgns, 'image');
            coutner.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", data.image);
            coutner.width.baseVal.value = 75;
            coutner.height.baseVal.value = 75;
            coutner.id = data._id;
            coutner.sides = data.sides;
            coutner.side = 0;
            svg.getElementById('counters').appendChild(coutner);

            addToStack(hex, coutner);
            alignStack(hex);
            return coutner;
        }
    },
    undo: function(data, counter) {
        var svg = document.getElementById('svg');
        removeFromStack(counter.position, counter);
        svg.getElementById('counters').removeChild(counter);
        alignStack(counter.position);
    }
};