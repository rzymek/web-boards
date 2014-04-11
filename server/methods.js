function getPlayerNames(table) {
    return table.players.map(function(it) {
        return Meteor.users.findOne(it).emails[0].address;
    })
}
;

Meteor.methods({
    games: function() {
        var fs = Npm.require('fs');
        var games = fs.readdirSync('../client/app/games');
        console.log('avaiable games', games);
        return games;
    },
    getPlayerNames: function(tableId) {
        return getPlayerNames(Tables.findOne(tableId, {reactive: false}));
    },
    getTableInfo: function(tableId) {
        var table = Tables.findOne(tableId, {reactive: false});
        console.log('getTableInfo', table);
        if (table) {
            table.playersInfo = getPlayerNames(table);
        }
        return table;
    },
    join: function(tableId) {
        var user = getUsername(this.userId);
        var table = Tables.findOne(tableId, {reactive: false});
        var mail = Assets.getText('join-email.txt')
                .replace('{{game}}', table.game)
                .replace('{{id}}', table.id)
                .replace('{{url}}', Meteor.absoluteUrl('/play/' + table._id))
                .replace('{{user}}', user);
        Email.send({
            from: 'eerzymek@gmail.com',
            bcc: _.values(table.players),
            subject: '[WebBoards] ' + user + ' joined ' + table.game,
            text: mail
        });
        var action = {};
        action['players.' + this.userId] = user;
        var changed = Tables.update(tableId, {$set: action});
        return changed === 1;
    },
    leave: function(tableId) {
        var unset = {};
        unset['players.' + Meteor.userId()] = "";
        Tables.update(tableId, {
            $unset: unset
        });
        var table = Tables.findOne(tableId);
        if (Object.keys(table.players).length === 0 && !table.ops) {
            console.log('deleteing empty table', tableId);
            Tables.remove(tableId);
        }
    },
    reset: function() {
        console.log('FULL RESET');
        Operations.remove({});
        Tables.remove({});
        ErrorLog.remove({});
    },
    undo: function() {
        var last = Operations.findOne({}, {
            sort: {'createdAt': -1}
        });
        if (last.result !== undefined) {
            console.log('Undo not allowed for ' + last.op);
            throw new Meteor.Error(500, 'Undo not allowed for ' + last.op,
                    "The action includes a random result. Undoing that would be considered cheating.");
        }
        if (last === undefined) {
            return;
        }
        Operations.remove(last._id);
    },
    logError: function(data) {
        data.user = getCurrentUsername();
        data.userId = Meteor.userId();
        data.time = new Date();
        ErrorLog.insert(data);
        console.warn('client error', data);
    }
});
