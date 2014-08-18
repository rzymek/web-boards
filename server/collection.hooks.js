function updateTableOpsCount(userId, doc) {
    Tables.update(doc.tableId, {
        $set: {ops: Operations.find({tableId: doc.tableId}).count()}
    });
}

Operations.after.insert(updateTableOpsCount);
Operations.after.remove(updateTableOpsCount); //update on undo

Tables.after.update(function(userId, table) {
    if (table.players.length === 0 && table.ops === undefined) {
        console.log('removed unused table: ' + table);
        Tables.remove(table._id);
    }
});
