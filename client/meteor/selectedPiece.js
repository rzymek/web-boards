Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    if (sprites === undefined || sprites === null) {
        // svg board is not ready, can happen during code-push
        return;
    }
    drawSelection(selected);
});

selectById = function(id)  {
    Session.set('selectedPiece', id);
};

getSelectedId = function() {
    return Session.get('selectedPiece');
};