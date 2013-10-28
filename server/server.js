Meteor.publish('gamesSub', function() {
    this.added('games', 'theid', {games: games});
    this.ready();
});

Meteor.publish('operations', function() {
    return Operations.find();
});

Meteor.methods({
    games: function() {
        var games = fs.readdirSync('../client/app/games');
        console.log('avaiable games', games);
        return games;
    },
    reset: function() {
        console.log('removed all operations');
        Operations.remove({});
    },
    undo: function() {
        var last = Operations.findOne({}, {
            sort: {'createdAt': -1}
        });
        Operations.remove(last._id);
    }
});

Operations.allow({
    insert: function() {
        return true;
    },
    update: function() {
        return true;
    }
});

Operations.deny({
    insert: function(userId, doc) {
        doc.createdAt = new Date().valueOf();
        return false;
    },
    update: function(userId, doc, fieldNames, modifier) {
        if (modifier.$set) {
            modifier.$set.updated = new Date();
        }
        return false;
    }
});

