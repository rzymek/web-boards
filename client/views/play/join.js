Template.join.quest = function() {
    var table = getTable();
    if (!table) 
        return false;
    var isInPlayers = Meteor.userId() in table.players;
    return !isInPlayers;
};

Template.join.loggedIn = function() {
    return Meteor.userId() !== null;
};

Template.join.table = function() {
    return getTable();
};

Template.join.players = function() {
    var table = getTable()
    if (!table)
        return [];
    var players = [];
    for (var id in table.players) {
        players.push(table.players[id]);
    }
    return players.join(', ');
};

Template.join.events({
    'click #join':function() {
        Meteor.call('join', Session.get('tableId'));
    },
    'click #leave':function() {
        Router.go('/');
    },
    'click #hide':function() {
        $('#joinNavBar').hide();
    }
});