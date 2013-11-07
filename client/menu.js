function nthOp(n) {
    return Operations.find({}, {
        sort: {'createdAt': 1},
        skip: n,
        limit: 1,
        reactive: false
    }).fetch()[0];
}
opRange = function opRange(first, last) {
    if(first > last) {
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
    });
}
normalizeRangeTest = function() {
    //  [1] [2] [3] [4] [5] 
    // ^   ^   ^   ^   ^   ^ 
    // 0   1   2   3   4   5      
    //     <-----------|
    // 
    //  [5] [4] [3] [2] [1] 
    // ^   ^   ^   ^   ^   ^ 
    // 0   1   2   3   4   5      
    //     |----------->
    var count = 5;
    [
        {a: 5, b: 0, exp: [0, 5, -1]},
        {a: 0, b: 5, exp: [0, 4, 1]},
        {a: 2, b: 4, exp: [2, 3, 1]},
        {a: 1, b: 4, exp: [1, 3, 1]},
        {a: 4, b: 1, exp: [1, 3, -1]},
        {a: 3, b: null, exp: [4, 4, -1]},
        {a: 5, b: 3, exp: [4, 5, 1]},
        {a: 3, b: null, exp: [4, 5, -1]},
    ].forEach(function(test) {
        var result = normalizeRange(test.a, test.b, count);
        console.log(test.exp.join(',') === result.join(','), 'a=' + test.a + ' b=' + test.b, test.exp, result);
    });
}

normalizeRange = function(a, b, count) {
    return [4, 5, -1];
}

lastReplayIndex = null;
Meteor.startup(function() {
    Session.set('replayIndex', null);
    Deps.autorun(function() {
        var idx = Session.get('replayIndex');
        var last = lastReplayIndex;
        if (idx === null && lastReplayIndex === null) {
            return;
        }
        var count = Operations.find({}).count();
        if (last === null) {
            last = count;
        }
        var start = Math.min(idx, last) + 1;
        var end = Math.max(idx, last);
        if (start < 0)
            start = 0;
        if (end > count - 1)
            end = count - 1;
        var op = 'runOp';
        if (idx < last)
            op = 'undoOp';
        lastReplayIndex = idx;
        console.log(start, '..', end, op, ' lRI=', lastReplayIndex);
    });
});
Meteor.startup(function() {
    ctx.menu = {
        'Undo': function() {
            Meteor.call('undo');
        },
        'Toggle units': function() {
            console.log('toggle');
        },
        'Back': function() {
            var idx = Session.get('replayIndex');
            if (idx === undefined) {
                idx = Operations.find({}).count() - 1;
                NProgress.configure({speed: 0});
            } else {
                NProgress.configure({speed: 200});
            }
            if (idx < 0) {
            }
            var data = nthOp(idx);
            undoOp(data);
            NProgress.set(idx / Operations.find({}).count());
            idx--;
            Session.set('replayIndex', idx);
        },
        'Fwd': function() {
            if (ctx.replayIndex === null) {
                return;
            }
            if (ctx.replayIndex >= Operations.find({}).count() - 1) {
                ctx.replayIndex = null;
                return;
            }
            var data = nthOp(ctx.replayIndex + 1);
            runOp(data);
            ctx.replayIndex++;
            NProgress.set((ctx.replayIndex + 1) / Operations.find({}).count());
        },
        'Flip': function() {
            console.log('flip', ctx.selected);
            if (!ctx.selected)
                return;
            Operations.insert({
                op: 'FlipOp',
                counterId: ctx.selected.img.id,
            });
        },
        'Remove': function() {
            if (!ctx.selected)
                return;
            Operations.insert({
                op: 'DropOp',
                counterId: ctx.selected.img.id,
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
    S.setMenuItems(Object.keys(ctx.menu));
});