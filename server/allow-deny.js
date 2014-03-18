Tables.allow({
    insert: function(userId, table) {
        try {
            check(userId, String);
            check(table.game, String);
            table.started = new Date().valueOf();
            table.players = {};
            table.players[userId] = getUsername(userId);
            return true;
        } catch (err) {
            console.error(err.stack);
            return false;
        }
    }
});

Operations.allow({
    insert: function(userId, op) {
        try {
            check(userId, String);
            check(op.op, String);
            check(op.result, undefined);
            check(op.tableId, String);
            check(op.tableId, Match.Where(function(tableId) {
                var table = Tables.findOne(tableId);
                return (table !== null) && (table !== undefined) && (userId in table.players);
            }));
            if (op.server !== undefined) {
                op.result = {};
                if (op.server.roll !== undefined) {
                    var r = op.server.roll;
                    var sum = 0;
                    for (var i = 0; i < r.count; i++) {
                        sum += (1 + Math.floor(Math.random() * r.sides))
                    }
                    op.result.roll = sum;
                    op.result.type = r.count + 'd' + r.sides;
                }
                delete op['server'];
                console.log('rolled', op.result.roll);
            }
            op.createdAt = new Date().valueOf();
            return true;
        } catch (err) {
            console.error(err.stack);
            return false;
        }
    }
});

Edit.allow({
    insert: function() {
        return true;
    },
    remove: function() {
        return true;
    }
});
