var svgns = "http://www.w3.org/2000/svg";

function removeFromStack(target, img) {
    target.stack = target.stack.filter(function(it) {
        return it !== img;
    });
}

function hrefToId(href) {
    return '_' + href.substring(href.lastIndexOf('/') + 1, href.length);
}

function addToStack(hex, counter) {
    counter.position = hex;
    if (hex.stack)
        hex.stack.push(counter);
    else
        hex.stack = [counter];
}

var map = {};
PlaceOp = {
    counter: null,
    run: function(data) {
        var svg = document.getElementById('svg');
        var hex = svg.getElementById(data.hexid);
        if (hex === null) {
            console.warn('Hex not found:', data.hexid);
        } else {
            var img = document.createElementNS(svgns, 'image');
            img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", data.image);
            img.width.baseVal.value = 75;
            img.height.baseVal.value = 75;
            img.id = hrefToId(data.image);
            svg.getElementById('counters').appendChild(img);
            
            addToStack(hex, img);
            alignStack(hex);
            map[this._id] = img;
        }
    },
    undo: function(data) {
        var svg = document.getElementById('svg');
//        var img = svg.getElementById(hrefToId(data.image));
        var counter = map[this._id];
        console.log('undo',counter);
        removeFromStack(counter.position, counter);
        svg.getElementById('counters').removeChild(counter);
        alignStack(counter.position);
//        var target = ctx.getPlace(data.hexid);
//        removeFromStack(target, img);
//        alignStack(svg.getElementById(data.hexid), target.stack);
    }
};

MoveOp = {
    run: function(data) {
        var svg = document.getElementById('svg');
        var hex = svg.getElementById(data.to);
        var counter = svg.getElementById(data.counter);
        var target = ctx.getPlace(data.to);
        removeFromStack(counter.target, counter);
        target.stack.push(counter);
        counter.target = target;
        counter.position = hex;
        alignStack(hex, target.stack);
    },
    undo: function(data) {

    }
};

runOp = function(data) {
    this[data.op].run(data);
};
undoOp = function(data) {
    this[data.op].undo(data);
};