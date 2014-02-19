Template.welcome.games = function() {
    return Session.get('games');
};
Template.welcome.events({
    'click button': function(e) {
        var t = e.currentTarget;
        var tableId = Tables.insert({
            players: [Meteor.userId()],
            turn:1,
            'current': 'US',
            game: t.value
        });
        Router.go('play', {_id: tableId});
    },
});

Template.welcome.tables = function() {
    return Tables.find({});
};

function getUsername() {
    return Meteor.user().emails[0].address;
}

Deps.autorun(function(){
    var tableId = Session.get('tableId');
    if(tableId !== null) {
        var table = Tables.findOne(tableId);    
        if (!table) {
            if (window.confirm('Do you want to join this game?')) {
                Meteor.call('join', tableId);
            }else{
                Router.go('welcome');
            }
        }else{
            Session.set('selectedGame', table.game);            
        }        
    }else{
        Session.set('selectedGame', null);
    }
});