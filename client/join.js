Deps.autorun(function() {
    if(!is('tables.ready'))
        return; 
    var tableId = Session.get('tableId');
    if (tableId !== null) {
        var table = Tables.findOne(tableId, {fields: {game: 1}});
        console.log(tableId + ' -> ' + table);
        if (!table) {            
            Session.set('selectedGame', null);
            if (window.confirm('Do you want to join this game?')) {
                Meteor.call('join', tableId, function(error, result) {
                    if(!result) {
                        window.alert('No such game');
                        Router.go('welcome');
                    }
                });
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