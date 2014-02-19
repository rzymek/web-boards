var welcome = Template['welcome'];

welcome.games = function() {
    return Session.get('games');
};
welcome.events({
    'click button': function(e) {
        var t = e.currentTarget;
        var tableId = Tables.insert({
            players: {
                'US':getUsername(),
                'GE':null
            },
            turn:1,
            'current': 'US',
            game: t.value
        });
        Session.set('tableId', tableId);
        Session.set('selectedGame', t.value);
    },
    'click .table' : function(e) {
        var t = e.currentTarget;
        Session.set('tableId', t.getAttribute('tableId'));
        Session.set('selectedGame', t.getAttribute('game'));
    }
});

welcome.tables = function() {
    return Tables.find({});
};

function getUsername() {
    return Meteor.user().emails[0].address;
}