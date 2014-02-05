var welcome = Template['welcome'];

welcome['games'] = function() {
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
        console.log(tableId);
        Session.set('selectedGame', t.value);
    }
});

welcome.tables = function() {
    return Tables.find({});
}


function getUsername() {
    return Meteor.user().emails[0].address;
}