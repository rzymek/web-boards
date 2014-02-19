
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
        return Tables.find({});
    });
    
})

Meteor.methods({
    games: function() {
        var games = fs.readdirSync('../client/app/games');
        console.log('avaiable games', games);
        return games;
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

Tables.allow({
    insert: function(userId, doc) {
        doc.started = new Date().valueOf();
        return true;
    },
});

Operations.allow({
    insert: function() {
        return true;
    },
});

Operations.deny({
    insert: function(userId, doc) {
        doc.createdAt = new Date().valueOf();
        if (doc.result !== undefined) {
            console.warn('trying to insert doc with result');
            return true;
        }
        if (doc.server !== undefined) {
            //TODO: parse the actual request in doc.server
            doc.result = {
                roll: 1 + Math.floor(Math.random() * 6)
            };
            delete doc['server'];
            console.log('rolled', doc.roll);
        }
        return false;
    },
    update: function() {
        return true;
    },
    remove: function() {
        return true;
    }    
//    update: function(userId, doc, fieldNames, modifier) {
//        if (modifier.$set) {
//            modifier.$set.updated = new Date();
//        }
//        return false;
//    }
});

