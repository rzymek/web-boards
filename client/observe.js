Meteor.startup(function() {
    Operations.before.insert(function(userId, doc) {
        doc.tableId = Session.get('tableId');
    });

    NProgress.configure({
        trickle: false,
        speed: 100
    });

    selectById(null);
});

Deps.autorun(function() {
    if (Session.equals('boardReady', true)) {
        Operations.find().observe({
            added: function(data) {
                if (data.server === undefined) {
                    runOp(data);
                }
            },
            changed: function(data) {
                if (data.result !== undefined) {
                    runOp(data);
                }
            },
            removed: function(data) {
                undoOp(data);
            }
        });
    }
});