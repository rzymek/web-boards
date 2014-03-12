updateSelection = function() {
    var rect = sprites.selection;
    if (rect.target) {
        copyTransformation(rect.target, rect);
//        if(rect.target.transform.baseVal.numberOfItems > 3) {
//            var tf = ensureTransformListSize(byId('svg'), rect.transform.baseVal, 4);
//            tf.getItem(3).setMatrix(rect.target.transform.baseVal.getItem(3).matrix);
//        }
//        
//        if (rect.x.baseVal && rect.target.x.baseVal) {
//            rect.x.baseVal.value = rect.target.x.baseVal.value;
//            rect.y.baseVal.value = rect.target.y.baseVal.value;
//        }
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
        byId('overlays').appendChild(rect);
        rect.style.visibility = 'visible';
    } else {
        rect.style.visibility = 'hidden';
    }
};