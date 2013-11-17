updateSelection = function() {
    var rect = byId('selection');
    copyTransformation(rect.target, rect);
}

Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    var rect = byId('selection');
    if (!rect) {
        // svg board is not ready, can happen during code-push
        return;
    }
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
});