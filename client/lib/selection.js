updateSelection = function() {
    var rect = sprites.selection;
    if (rect.target)
        copyTransformation(rect.target, rect);
};

drawSelection = function(selected) {
    var rect = sprites.selection;
    if (selected) {
        rect.target = byId(selected);
        var w = rect.target.width.baseVal.value;
        var h = rect.target.height.baseVal.value;
        rect.width.baseVal.value = w;
        rect.height.baseVal.value = h;
        rect.x.baseVal.value = -w / 2;
        rect.y.baseVal.value = -h / 2;
        updateSelection();
        byId('overlays').appendChild(rect);
        rect.style.visibility = 'visible';
    } else {
        rect.style.visibility = 'hidden';
    }
};