Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    if (sprites === null) {
        // svg board is not ready, can happen during code-push
        return;
    }
    select(selected);
});