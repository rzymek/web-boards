Template.welcome.games = function() {
    return Session.get('__games');
};

Meteor.startup(function() {
    Meteor.call('games', function(err, games) {
        Session.set('__games', games);
    });
});

Template.welcome.loggedIn = function() {
    return Meteor.userId() !== null;
};

Template.welcome.listPlayers = function(table) {
    var players = [];
    for (var id in table.players) {
        if (id !== Meteor.userId()) {
            players.push(table.players[id]);
        }
    }
    return players;
};

Template.welcome.fmtDate = function(millis) {
    return dateFmt(new Date(millis));
};

Template.welcome.events({
    'click .start-game': function(e) {
        var t = e.currentTarget;
        var tableId = Tables.insert({
            game: t.value
        });
        Router.go('play', {_id: tableId});
    },
    'click .leave-game': function(e) {
        var id = e.currentTarget.value;
        if (window.confirm('Leave game ' + id + '?')) {
            Meteor.call('leave', id, function(err) {
                if (err) {
                    window.alert(err);
                }
            });
        }
    },
    'click #config': function(e) {
        var config = window.prompt("config", Session.get('config'));
        if (config !== null) {
            if (config === 'r') {
                Meteor.call('reset');
            } else {
                Session.set('config', config);
            }
        }
    },
    'click .take-a-tour': function() {
        if(Meteor.userId() === null) {
            hopscotch.startTour(TOUR1);
        }else{
            tour2();
        }
    }
});

Template.welcome.config = function() {
    return Session.get('config');
};

Template.welcome.tables = function() {
    return Tables.find({}, {
        sort: ['started', 'asc']
    });
};