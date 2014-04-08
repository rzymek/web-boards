menu = {
    'Undo': function() {
        Meteor.call('undo', function(error){
            console.error(error);
            alert(error.reason+'\n'+error.details);
        });
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