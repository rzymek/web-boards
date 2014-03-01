Deps.autorun(function() {
    var tableId = Session.get('tableId');
    if (tableId === null) {
        Session.set('board.ready', false);
        Session.set('gameInfo', defaultGameInfo);
        return;
    }
    var table = Tables.findOne(tableId, {fields: {game: 1}});
    $.get('/games/' + table.game + '/game.json', function(data) {
        Session.set('gameInfo', data);
    });
});
