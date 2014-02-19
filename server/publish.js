
Meteor.startup(function() {
    Meteor.publish('gamesSub', function() {
        this.added('games', 'theid', {games: games});
        this.ready();
    });

    Meteor.publish('operations', function(tableId) {
        console.log("ops subscribe "+tableId);
        return Operations.find({'tableId': tableId}, {
            sort: {'createdAt': 1}
        });
    });

    Meteor.publish('tables', function() {
        return Tables.find({players: this.userId});
    });
    
});
