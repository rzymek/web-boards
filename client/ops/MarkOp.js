/*
 MarkOp = {
 run: function(data) {
 var counter = byId(data.counterId);
 var g = document.createElementNS(SVGNS, 'g');
 
 var used = byId('used').cloneNode();
 var w = counter.width.baseVal.value;
 var h = counter.height.baseVal.value / 3;
 used.width.baseVal.value = w;
 used.height.baseVal.value = h;
 used.x.baseVal.value = -w / 2;
 used.y.baseVal.value = -h / 2;
 copyTransformation(counter, g);
 counter.parentElement.replaceChild(g, counter);
 
 g.id = counter.id;
 g.stack = counter.stack;
 counter.id = '';
 counter.removeAttribute('transform');
 g.appendChild(counter);
 g.appendChild(used);
 
 return {
 run: function() {
 overlays.removeChild(used);
 }
 };
 },
 undo: function(data, c) {
 c.run();
 }
 };*/
MarkOp = function(data) {
    var counter = byId(data.counterId);
    var style = counter.style;
    var initial = style.filter;
    style.filter = (initial === '') ? 'url(#mark)' : '';
    return function() {
        style.filter = initial;
    }
};