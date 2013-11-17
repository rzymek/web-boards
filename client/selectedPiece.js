Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    var rect = byId('selection');
    if (!rect) {
        // svg board is not ready, can happen during code-push
        return;
    }
    if (selected) {
        var selectedPiece = byId(selected);
        var w = selectedPiece.width.baseVal.value;
        var h = selectedPiece.height.baseVal.value;
        rect.width.baseVal.value = w;
        rect.height.baseVal.value = h;
        rect.x.baseVal.value = -w / 2;
        rect.y.baseVal.value = -h / 2;
        copyTransformation(selectedPiece, rect);
        byId('overlays').appendChild(rect);
        rect.style.visibility = 'visible';
    } else {
        rect.style.visibility = 'hidden';
    }
});