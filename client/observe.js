
Operations.before.insert(function(userId, doc) {
    doc.tableId = Session.get('tableId');
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
            if(is('board.ready')) {
                undoOp(data);
            }
        }
    });
});

Deps.autorun(function(c) {
    console.log('sprites.ready', 'board.ready', Session.get('sprites.ready'), Session.get('board.ready'));
    /* The board need to be fully ready before any Ops are executed */
    var tableId = Session.get('tableId');
    if (is('sprites.ready', 'board.ready') && tableId) {
        console.log('sub ops...', tableId);
        Meteor.subscribe('operations', tableId);
    }
});


