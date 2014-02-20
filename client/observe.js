
Operations.before.insert(function(userId, doc) {
    doc.tableId = Session.get('tableId');
});

Deps.autorun(function() {
    if (!is('sprites.ready', 'board.ready'))
        return;
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


