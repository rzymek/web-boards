Tables.allow({
    insert: function(userId, table) {
        try {
//            check(userId, String);
            check(table.game, String);
            table.started = new Date().valueOf();
            table.players = {};
            if(userId) {
                table.players[userId] = getUsername(userId);
            }
            return true;
        } catch (err) {
            console.error(err.stack);
            return false;
        }
    },
    update: function(userId, table, fieldNames, modifier) {
        try {
            check(userId, String);
            check(modifier, {$set: {comment: String}});
            return true;
        } catch (err) {
            console.error(err.stack);
            return false;
        }
    }
});

Operations.allow({
    insert: function(userId, op) {
        console.log(op);
        try {
            check(op.op, String);
            check(op.result, undefined);
            check(op.tableId, String);
            
            var table = Tables.findOne(op.tableId);
            if(!_.isEmpty(table.players)) {
                check(userId, String);
            }
            check(op.tableId, Match.Where(function() {
                return ((table !== null) && (table !== undefined) && (userId in table.players))
                    || _.isEmpty(table.players);
            }));
            if (op.server !== undefined) {
                op.result = {};
                if (op.server.roll !== undefined) {
                    var r = op.server.roll;
                    var sum = 0;
                    op.result.dice = [];
                    for (var i = 0; i < r.count; i++) {
                        var die = 1 + Math.floor(Math.random() * r.sides);
                        sum += die;
                        op.result.dice.push(die);
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