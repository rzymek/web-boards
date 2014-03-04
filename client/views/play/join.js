Template.join.quest = function() {
    var info = Session.get('gameInfo');
    if (!info) {
        return false;
    }    
    var table = Tables.findOne(info.table._id);
    var initiallyAbsent = (info.table.players.indexOf(Meteor.userId()) === -1);
    var currentlyAbsent = (!table || table.players.indexOf(Meteor.userId()) === -1);
    return initiallyAbsent && currentlyAbsent;
};

Template.join.loggedIn = function() {
    return Meteor.userId() !== null;
};

Template.join.info = function() {
    return Session.get('gameInfo');
};

Template.join.events({
    'click #join':function() {
        Meteor.call('join', Session.get('gameInfo').table._id, function(err,result){
            
        });
    },
    'click #leave':function() {
        Router.go('/');
    },
    'click #hide':function() {
        $('#joinNavBar').hide();
    }
});