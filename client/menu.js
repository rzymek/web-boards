Meteor.startup(function() {
    menu = {
        'Undo': function() {
            Meteor.call('undo');
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
        },
        'Flip': function() {
            var selected = Session.get('selectedPiece');
            if (!selected)
                return;
            Operations.insert({
                op: 'FlipOp',
                counterId: selected,
            });
        },
        'Remove': function() {
            var selected = Session.get('selectedPiece');
            if (!selected)
                return;
            Operations.insert({
                op: 'DropOp',
                counterId: selected,
            });
        },
        'Test': function() {
            for (var i = 1; i <= 5; i++) {
                Operations.insert({op: 'NoOp', info: i});
            }
        },
        '1d6': function() {
            Operations.insert({
                op: 'RollOp',
                server: {
                    roll: '1d6'
                }
            });
        },
        'Reset': function() {
            Meteor.call('reset');
        }
    };
    S.setMenuItems(Object.keys(menu));
});