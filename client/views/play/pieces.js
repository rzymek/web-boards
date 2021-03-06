Session.setDefault('piecesCategory', 'Special');

Template.pieces.categories = function() {
    var categories = Session.get('gameInfo').pieces.map(function(pieces) {
        return pieces.category;
    });
    return ["Special"].concat(categories);
};

Template.pieces.isSelected = function(s) {    
    return Session.equals('piecesCategory', s); 
};

Template.piece.selected = function() {
    return Session.equals('selectedPiece', this.id) ? 'pieceSelected' : '';
};

Template.piece.events({
    'click img': function(e) {
        var img = e.currentTarget;
        var val = img.id;
        if (getSelectedId() === val)
            val = null;
        selectById(val);
    }
});
 
Template.pieces.events({
    'change select': function(e) {
        var combo = e.target;
        var category = combo.options[combo.selectedIndex].value;
        Session.set('piecesCategory', category);
        return true;
    }
});


Template.selectedPieces.pieces = function() {
    var cat = Session.get('piecesCategory');
    if (cat === "Special") {
        return Object.keys(Special).map(function(id){
            return {
                src: Special[id].src,
                category: cat,
                id: id
            };
        });
    }
    var info = Session.get('gameInfo');
    var table = getTable({fields: {game: 1}});
    var inCat = info.pieces.filter(function(p) {
        return p.category === cat;
    });
    if (inCat.length > 0) {
        return inCat[0].list.map(function(p) {
            var img = p.images[0];
            return {
                name: p.name,
                category: cat,
                src: '/games/' + table.game + '/images/' + img,
                id: (cat + '_' + p.name).replace(/[^A-Za-z0-9_]/, '')
            };
        });
    } else {
        return [];
    }
};
