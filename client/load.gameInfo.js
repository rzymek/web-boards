Deps.autorun(function() {
    var tableId = Session.get('tableId');
    Session.set('gameInfo', defaultGameInfo);
    if(tableId === null) {
        Session.set('board.ready', false);
        return;
    }
    var table = Tables.findOne(tableId);
    $.get('/games/' + table.game + '/game.json', function(data) {
        Session.set('gameInfo', data);
    });
});
