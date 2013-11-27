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
        'Pieces': function() {
            $('#panel').fadeToggle();
            var p = byId('game-pieces').cloneNode();
            p.x.baseVal.value = 100;
            p.y.baseVal.value = 100;
            p.width.baseVal.value = 300;
            p.height.baseVal.value = 300;
            byId('pieceMenuLayer').appendChild(p);
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
            var meta = document.getElementsByTagName('meta')[0];
            if(meta.content ==='')
                meta.content = "initial-scale=1.0, user-scalable=no";
            else
                meta.content = '';
            alert(meta.content);
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
    Session.set('menuItems',Object.keys(menu));
});