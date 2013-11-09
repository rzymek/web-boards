Meteor.startup(function() {
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

    NProgress.configure({
        trickle: false,
        speed: 100
    });

    Session.set('selectedPiece', null);
});