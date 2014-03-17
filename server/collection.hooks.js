Operations.after.insert(function(userId, doc) {
    Tables.update(doc.tableId, {
        $set: {ops: Operations.find({tableId: doc.tableId}).count()}
    });
});

Tables.after.update(function(userId, table) {
    if (table.players.length === 0 && table.ops === undefined) {
        console.log('removed unused table: ' + table);
        Tables.remove(table._id);
    }
});
