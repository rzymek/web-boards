
Operations.before.insert(function(userId, doc) {
    if (!doc)
        return;
    doc.tableId = Session.get('tableId');
    selectById(null);
});

Meteor.startup(function() {
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
            if (is('board.ready')) {
                undoOp(data);
            }
        }
    });
});

Deps.autorun(function(c) {
    var tableId = Session.get('tableId');
    console.log('sprites.ready', 'board.ready', Session.get('sprites.ready'), Session.get('board.ready'), tableId);
    /* The board need to be fully ready before any Ops are executed */
    if (is('sprites.ready', 'board.ready') && tableId) {
        console.log('sub ops...', tableId);
        Meteor.subscribe('operations', tableId);
    }
});


