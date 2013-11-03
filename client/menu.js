Meteor.startup(function() {
    ctx.menu = {
        'Undo': function() {
            Meteor.call('undo');
        },
        'Toggle units': function() {
            console.log('toggle');
        },
        'Back': function() {
            if (ctx.opBacktrack === null) {
                ctx.opBacktrack = Operations.find({}).count();
            }
            ctx.opBacktrack--;
            if (ctx.opBacktrack < 0) {
                ctx.opBacktrack = 0;
                return;
            }
            console.log("backing up", ctx.opBacktrack);
            //TODO: optimize
            var data = Operations.find({}).fetch()[ctx.opBacktrack];
            runOp(data);
        },
        'Fwd': function() {
            if (ctx.opBacktrack === null) {
                return;
            }
            if (ctx.opBacktrack >= Operations.find({}).count()) {
                ctx.opBacktrack = null;
                return;
            }
            var data = Operations.find({}).fetch()[ctx.opBacktrack];
            undoOp(data);
            ctx.opBacktrack++;
        },
        'Reset': function() {
            Meteor.call('reset');
        }
    };
    S.setMenuItems(Object.keys(ctx.menu));
});