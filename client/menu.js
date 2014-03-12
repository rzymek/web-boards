menu = {
    'Undo': function() {
        Meteor.call('undo');
    },
    'Pieces': function() {
        $('#panel').fadeToggle();
    },
    'Back': function() {
        var idx = Session.get('replayIndex');
        if (idx === null)
            idx = Operations.find({}).count();
        idx--;
        if (idx < 0)
            return;
        Session.set('replayIndex', idx);
    },
    'Fwd': function() {
        var idx = Session.get('replayIndex');
        if (idx === null)
            return;
        idx++;
        if (idx > Operations.find({}).count())
            idx = null;
        Session.set('replayIndex', idx);
    }
};

pieceMenu = {
    'Flip': function(piece) {
        Operations.insert({
            op: 'FlipOp',
            counterId: piece.id
        });
    },
    'Unmark': function(piece) {
        Operations.insert({
            op: 'ClearMarkersOp',
            counterId: piece.id
        });
    }
};