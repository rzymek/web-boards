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

Template.welcome.fmtDate = function(millis) {
    function fmt(c, s) {
        while (s.toString().length < c) {
            s = '0' + s;
        }
        return s;
    }
    var d = new Date(millis);
    return d.getFullYear() + "-" + fmt(2, d.getMonth() + 1) + "-" + fmt(2, d.getDay()) + " "
            + fmt(2, d.getHours()) + ":" + fmt(2, d.getMinutes());
};

Template.welcome.events({
    'click .start-game': function(e) {
        var t = e.currentTarget;
        var tableId = Tables.insert({
            players: [Meteor.userId()],
            turn: 1,
            'current': 'US',
            game: t.value
        });
        Router.go('play', {_id: tableId});
    },
    'click .leave-game': function(e) {
        var id = e.currentTarget.value;
        if (window.confirm('Leave game ' + id + '?')) {
            Tables.update(id, {
                $pull: {players: Meteor.userId()}
            });
        }
    },
    'click #config': function(e) {
        var config = window.prompt("config", Session.get('config'));
        if (config !== null) {
            Session.set('config', config);
        }
    }
});
Template.welcome.config = function() {
    return Session.get('config');
};

Template.welcome.tables = function() {
    return Tables.find({});
};

function getUsername() {
    return Meteor.user().emails[0].address;
}
