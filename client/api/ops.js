var svgns = "http://www.w3.org/2000/svg";

function hrefToId(href) {
    return '_'+href.substring(href.lastIndexOf('/')+1, href.length);
}
PlaceOp = {
    run: function(data) {
        var svg = document.getElementById('svg');
        var use = svg.getElementById(data.hexid);
        if (use === null) {
            console.warn('Hex not found:', data.hexid);
        } else {
            var img = document.createElementNS(svgns, 'image');
            img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", data.image);
            img.width.baseVal.value = 75;
            img.height.baseVal.value = 75;            
            img.id = hrefToId(data.image);
            svg.getElementById('counters').appendChild(img);

            var target = ctx.getPlace(data.hexid);
            target.stack.push(img);
            alignStack(use, target.stack);
        }
    },
    undo: function(data) {
        var svg = document.getElementById('svg');
        var img = svg.getElementById(hrefToId(data.image));
        svg.getElementById('counters').removeChild(img);
        var target = ctx.getPlace(data.hexid);
        target.stack = target.stack.filter(function(it) {
            return it !== img;
        });
        alignStack(svg.getElementById(data.hexid), target.stack);
    }
};

runOp = function (data){
    this[data.op].run(data);
}
undoOp = function (data){
    this[data.op].run(data);
}