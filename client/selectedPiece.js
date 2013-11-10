var selectedPiece = null;
Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    if (selectedPiece) {
        if (selectedPiece.style.filter !== undefined) {
            //deselect current onboard piece
            //pieces in panel are deselected reactivly
            selectedPiece.style.filter = '';
        }
    }
    if (selected) {
        selectedPiece = document.getElementById(selected);
        if (!selectedPiece) {
            // svg board is not ready, can happen during code-push
            return;
        }
        if (selectedPiece.style.filter !== undefined) {
            selectedPiece.style.filter = 'url(#select)';
        }
    }
});