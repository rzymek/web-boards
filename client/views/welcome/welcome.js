Template.welcome.games = function() {
    return Session.get('games');
};
Template.welcome.events({
    'click button': function(e) {
        var t = e.currentTarget;
        var tableId = Tables.insert({
            players: [Meteor.userId()],
            turn: 1,
            'current': 'US',
            game: t.value
        });
        Router.go('play', {_id: tableId});
    },
    'click #config': function(e) {
        var config = window.prompt("config",Session.get('config'));
        if(config !== null) {
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

Deps.autorun(function() {
    var tableId = Session.get('tableId');
    if (tableId !== null) {
        var table = Tables.findOne(tableId);
        if (!table) {
            if (window.confirm('Do you want to join this game?')) {
                Meteor.call('join', tableId);
            } else {
                Router.go('welcome');
            }
        } else {
            Session.set('selectedGame', table.game);
        }
    } else {
        Session.set('selectedGame', null);
    }
});