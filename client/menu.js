function nthOp(n) {
    return Operations.find({}, {
        sort: {'createdAt': 1},
        skip: n,
        limit: 1,
        reactive: false
    }).fetch()[0];
}
opRange = function opRange(first, last) {
    if (first > last) {
        var tmp = first;
        first = last;
        last = tmp;
    }
    if (first < 0)
        first = 0;
    var limit = last - first;
    if (limit === 0)
        return [];
    return Operations.find({}, {
        sort: {'createdAt': 1},
        skip: first,
        limit: limit,
        reactive: false
    }).fetch();
}


normalizeRange = function(a, b, count) {
    return [4, 5, -1];
}

lastReplayIndex = null;
Meteor.startup(function() {
    Session.set('replayIndex', null);
    Deps.autorun(function() {
        var to = Session.get('replayIndex');
        if (to === null && lastReplayIndex === null) {
            return;
        }
        var current = lastReplayIndex;
        var count = Operations.find({}).count();
        if (to === null)
            to = count;
        if (current === null)
            current = count;
        var ops = opRange(current, to);
        console.log('replaying', to, current, ops.length);
        if (current <= to) {
            for (var i = 0; i < ops.length; i++) {
                runOp(ops[i]);
            }
        } else {
            for (var i = ops.length - 1; i >= 0; i--) {
                undoOp(ops[i]);
            }
        }
        NProgress.set(to / count);
        lastReplayIndex = to;
    });
});
Meteor.startup(function() {
    menu = {
        'Undo': function() {
            Meteor.call('undo');
        },
        'Toggle units': function() {
            console.log('toggle');
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
                return;
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