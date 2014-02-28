Meteor.methods({
    games: function() {
        var games = fs.readdirSync('../client/app/games');
        console.log('avaiable games', games);
        return games;
    },
    join: function(tableId) {
        console.log(this.userId, ' joining ', tableId);
        var changed = Tables.update(tableId, {$addToSet: {players: this.userId}});
        return changed === 1;
    },
    reset: function() {
        console.log('FULL RESET');
        Operations.remove({});
        Tables.remove({});
    },
    undo: function() {
        var last = Operations.findOne({}, {
            sort: {'createdAt': -1}
        });
        if(last.result !== undefined) {
             //'Undo not allowed for '+last.op;
            return;
        }
        if (last === undefined) {
            return;
        }
        Operations.remove(last._id);
    }
});
