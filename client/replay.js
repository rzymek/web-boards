var lastReplayIndex = null;
Meteor.startup(function() {
    Session.set('replayIndex', null);
    Deps.autorun(function() {
        var to = Session.get('replayIndex');
        if (to === null && lastReplayIndex === null) {
            return;
        }
        var current = lastReplayIndex;
        var count = Operations.find({}, {reactive: false}).count();
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
        var style = byId('boardImg').style;
        if (to === count) {
            style.filter = '';
        } else if (style.filter !== 'url(#mark)') {
            style.filter = 'url(#mark)';
        }
        lastReplayIndex = to;
    });
});

function opRange(first, last) {
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
