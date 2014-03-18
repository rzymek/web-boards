Meteor.startup(function() {
    Meteor.publish('operations', function(tableId) {
        console.log("ops subscribe " + tableId);
        return Operations.find({'tableId': tableId}, {
            sort: {'createdAt': 1}
        });
    });

    Meteor.publish('tables', function(tableId) {
        var own = {
            //'players.'+this.userId: {$exists: true}
        };
        own['players.'+this.userId] = {$exists: true};
        return Tables.find({
            $or: [
                own,
                {_id: tableId}
            ]
        });
    });
    
    Meteor.publish('edit', function() {
        return Edit.find();
    });
});
