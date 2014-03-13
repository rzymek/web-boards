Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    if (sprites === undefined || sprites === null) {
        // svg board is not ready, can happen during code-push
        return;
    }
    drawSelection(selected);
});
selectedPiece = null;
selectById = function(id) {
    selectedPiece = id;
    drawSelection(id);
//    Session.set('selectedPiece', id);
};

getSelectedId = function() {
    return selectedPiece;
//    return Session.get('selectedPiece');
};