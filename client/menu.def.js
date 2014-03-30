menu = {
    'Undo': function() {
        Meteor.call('undo');
    },
    'Pieces': function() {
        $('#panel').fadeToggle();
    },
    'Toggle':function() {
        $('#counters').fadeToggle();
    },
    'Replay': function() {
        toggle('show.playback');
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
    },
    'Remove': function(piece) {
        Operations.insert({
            op: 'RemovePieceOp',
            counterId: piece.id
        });
    }
};