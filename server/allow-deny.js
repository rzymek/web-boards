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
    },
    update: function(userId, doc, fields, mods) {
        check(table.players, Match.Where(function(arr) {
            return arr !== null && arr.length > 0 && arr.indexOf(userId) !== -1;
        }));
        return (fields.length === 1 && fields[0] === 'players') &&
                (mods.$pull !== undefined) &&
                (mods.$pull.players === userId);
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
                return (table !== null) && (userId in table.players);
            }));
            if (op.server !== undefined) {
                //TODO: parse the actual request in doc.server
                op.result = {
                    roll: 1 + Math.floor(Math.random() * 6)
                };
                delete op['server'];
                console.log('rolled', op.roll);
            }
            op.createdAt = new Date().valueOf();
            return true;
        } catch (err) {
            console.error(err.stack);
            return false;
        }
    }
});