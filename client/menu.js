function nthOp(n) {
    return Operations.find({}, {
        sort: {'createdAt': 1},
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
                NProgress.configure({speed:0});
            }else{
                NProgress.configure({speed:200});
            }
            if (ctx.replayIndex < 0) {
                return;
            }
            var data = nthOp(ctx.replayIndex);
            undoOp(data);
            NProgress.set(ctx.replayIndex / Operations.find({}).count());
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
            NProgress.set((ctx.replayIndex+1) / Operations.find({}).count());
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
        'Remove':function (){
            if(!ctx.selected)return;
            Operations.insert({
                op: 'DropOp',
                counterId: ctx.selected.img.id,
            });
        },
        'Reset': function() {
            Meteor.call('reset');
        }
    };
    S.setMenuItems(Object.keys(ctx.menu));
});