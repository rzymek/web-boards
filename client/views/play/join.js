Template.join.quest = function() {
    var info = Session.get('gameInfo');
    if (!info) 
        return false;
    var notInPlayers = info.table.players.indexOf(Meteor.userId()) === -1;
    return notInPlayers;
};

Deps.autorun(function updateGameInfoTable(){
    var info = Session.get('gameInfo');
    if (info) {
        var table = Tables.findOne(info.table._id, {
            fields: { ops: 0 }
        });
        if(table) {
            console.log('updated table');
            info.table = table;
            Meteor.call('getPlayerNames', table._id, function(err,playersInfo) {
                info.table.playersInfo = playersInfo;
                console.log('playersInfo:',playersInfo)
                Session.set('gameInfo', info);
            })
        }
    }
});

Template.join.loggedIn = function() {
    return Meteor.userId() !== null;
};

Template.join.info = function() {
    return Session.get('gameInfo');
};

getTable = function() {
    var info = Session.get("gameInfo");
    if(!info) return null;
    return Tables.findOne(info.tableId);
}
Template.join.table = function() {
    return getTable();
};

Template.join.events({
    'click #join':function() {
        Meteor.call('join', Session.get('gameInfo').table._id);
    },
    'click #leave':function() {
        Router.go('/');
    },
    'click #hide':function() {
        $('#joinNavBar').hide();
    }
});