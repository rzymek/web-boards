Meteor.methods({
    games: function() {
        var games = fs.readdirSync('../client/app/games');
        console.log('avaiable games', games);
        return games;
    },
    join: function(tableId) {
        console.log(this.userId, ' joining ', tableId);
        Tables.update(tableId, {$push: {players: this.userId}});
    },
    reset: function() {
        console.log('removed all operations');
        Operations.remove({});
        Tables.remove({});
    },
    undo: function() {
        var last = Operations.findOne({}, {
            sort: {'createdAt': -1}
        });
        if (last === undefined) {
            return;
        }
        Operations.remove(last._id);
    }
});
