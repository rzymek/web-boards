Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    console.log('selected',selected);
    if (ctx.selected) {
        var piece = ctx.selected.img;
        if (isOnBoard(piece)) {
            //deselect current onboard piece
            //pieces in panel are deselected reactivly
            piece.style.filter = '';
        }
    }
    if (selected) {
        var piece = document.getElementById(selected);
        if (!piece) {
            // svg board is not ready, can happen during code-push
            ctx.selected = null;
            return;
        }
        if (isOnBoard(piece)) {
            ctx.selected = new SVGCounter(piece);
            piece.style.filter = 'url(#select)';
        } else {
            ctx.selected = new HTMLCounter(piece);
        }
    } else {
        ctx.selected = null;
    }
});