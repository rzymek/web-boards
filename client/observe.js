Operations.before.insert(function(userId, doc) {
    doc.tableId = Session.get('tableId');
    doc.player = userId;
    selectById(null);
    hidePieceMenu();
    hideStackSelector();
});

Meteor.startup(function() {
    /* Ops with `server` request are executed after the server
     * updates the Op with `result`
     */
    Operations.find().observe({
        added: function(data) {
            Session.set('replayIndex', null);
            runOp(data);
        },
        changed: function(data) {
            if (data.result !== undefined) {
                Session.set('replayIndex', null);
                runOp(data);
            }
        },
        removed: function(data) {
            if (is('board.ready')) {
                undoOp(data);
            }
        }
    });
});
