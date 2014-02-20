Deps.autorun(function() {   
    var tableId = Session.get('tableId');
    if(tableId === null) {
        Session.set('boardReady', false);
        return;
    }
    var table = Tables.findOne(tableId);
    if(table === null){
        Session.set('gameInfo', defaultGameInfo);
        return;
    }    
    $.get('/games/' + table.game + '/game.json', function(data) {
        var config = Session.get('config');
        if (config.indexOf('sfw') >= 0) {
            data.board.image = '../javadoc.png'; //TODO: remove
        } else if (config.indexOf('empty') >= 0) {
            data.board.image = '../../../empty.png'; //TODO: remove
        } else if (config.indexOf('hires') < 0) {
            data.board.image = '../board-low.jpg'; //TODO: remove
        }
        Session.set('gameInfo', data);
    });
});
