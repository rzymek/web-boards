function nthOp(n) {
    return Operations.find({}, {
        skip: n,
        limit: 1,
        reactive: false
    }).fetch()[0];
}
Meteor.startup(function() {
    ctx.menu = {
        'Undo': function() {
            Meteor.call('undo');
        },
        'Toggle units': function() {
            console.log('toggle');
        },
        'Back': function() {
            if (ctx.replayIndex === null) {
                ctx.replayIndex = Operations.find({}).count() - 1;
            }
            if (ctx.replayIndex < 0) {
                return;
            }
            var data = nthOp(ctx.replayIndex);
            undoOp(data);
            ctx.replayIndex--;
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
        },
        'Reset': function() {
            Meteor.call('reset');
        }
    };
    S.setMenuItems(Object.keys(ctx.menu));
});