
Operations.before.insert(function(userId, doc) {
    doc.tableId = Session.get('tableId');
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
            runOp(data);
        },
        changed: function(data) {
            if (data.result !== undefined) {
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
