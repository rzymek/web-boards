
Operations.before.insert(function(userId, doc) {
    doc.tableId = Session.get('tableId');
});

Deps.autorun(function() {
    /* The board need to be fully ready before any Ops are executed */
    if (!is('sprites.ready', 'board.ready'))
        return;
    /* Ops with `server` request are executed after the server
     * updates the Op with `result`
     */
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
});


