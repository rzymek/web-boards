Template.join.guest = function() {
    var table = getTable({fields: {players: 1}});
    if (!table || table.player === undefined)
        return false;
    var isInPlayers = Meteor.userId() in table.players;
    return !isInPlayers;
};

Template.join.loggedIn = function() {
    return Meteor.userId() !== null;
};

Template.join.game = function() {
    return getTable({fields: {game: 1}}).game;
};

Template.join.players = function() {
    var table = getTable({fields: {players: 1}})
    if (!table)
        return [];
    var players = [];
    for (var id in table.players) {
        players.push(table.players[id]);
    }
    return players.join(', ');
};

Template.join.events({
    'click #join': function() {
        Meteor.call('join', Session.get('tableId'));
    },
    'click #leave': function() {
        Router.go('/');
    },
    'click #hide': function() {
        $('#joinNavBar').hide();
    }
});