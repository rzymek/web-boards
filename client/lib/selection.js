updateSelection = function() {
    var rect = sprites.selection;
    if (rect.target) {
        copyTransformationItem(rect.target, rect, TX.POSITION);
        copyTransformationItem(rect.target, rect, TX.OFFSET);
    }
};

drawSelection = function(selected) {
    var rect = sprites.selection;
    if (!rect)
        return; //not loaded yet
    if (selected) {
        rect.target = byId(selected);
        if (rect.target.getBBox === undefined)
            return;
        var bbox = rect.target.getBBox();
        var w = bbox.width;
        var h = bbox.height;
        rect.width.baseVal.value = w;
        rect.height.baseVal.value = h;
        rect.x.baseVal.value = -w / 2;
        rect.y.baseVal.value = -h / 2;
        updateSelection();
        byId('selectionLayer').appendChild(rect);
        rect.style.visibility = 'visible';
    } else {
        rect.style.visibility = 'hidden';
    }
};