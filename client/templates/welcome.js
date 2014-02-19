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
        Router.go('play', {_id: tableId});
    },
});

welcome.tables = function() {
    return Tables.find({});
};

function getUsername() {
    return Meteor.user().emails[0].address;
}

Deps.autorun(function(){
    var tableId = Session.get('tableId');
    if(tableId !== null) {
        Session.set('selectedGame', Tables.findOne(tableId).game);
    }else{
        Session.set('selectedGame', null);
    }
});