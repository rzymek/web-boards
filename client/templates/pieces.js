Template.pieces.categories = function() {
    return [''].concat(S.gameInfo().pieces.map(function(pieces) {
        return pieces.category;
    }));
};

Template.piece.selected = function() {
    return Session.equals('selectedPiece', this.id) ? 'pieceSelected' : '';
};

Template.piece.events({
    'click img': function(e) {
        var img = e.currentTarget;
        var val = img.id;
        if (Session.equals('selectedPiece', val))
            val = null;
        Session.set('selectedPiece', val);
    }
});

Template.pieces.events({
    'change select': function(e) {
        var combo = e.target;
        var category = combo.options[combo.selectedIndex].value;
        S.setPiecesCategory(category);
        return true;
    },
});

Template.selectedPieces.pieces = function() {
    var g = S.selectedGame();
    var cat = S.piecesCategory();
    var inCat = S.gameInfo().pieces.filter(function(p) {
        return p.category === cat;
    });
    if (inCat.length > 0) {
        return inCat[0].list.map(function(p) {
            var name = p.images[0];
            return {
                sides: p.images.join('|'),
                src: '/games/' + g + '/images/' + name,
                id: 'panel-' + name.replace(/[^A-Za-z0-9_]/, '')
            };
        });
    } else {
        return [];
    }
};
