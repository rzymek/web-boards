Meteor.startup(function() {
    Meteor.publish('operations', function(tableId) {
        console.log("ops subscribe " + tableId);
        return Operations.find({'tableId': tableId}, {
            sort: {'createdAt': 1}
        });
    });

    Meteor.publish('tables', function(tableId) {
        return Tables.find({
            $or: [
                {players: this.userId},
                {_id: tableId}
            ]
        });
    });
});
