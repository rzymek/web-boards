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
}
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