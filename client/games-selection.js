
Deps.autorun(function() {
    
    var game = Session.get('selectedGame');
    console.log("selectedGame",game);
    if (game === undefined || game === null) {
        Session.set('gameInfo', defaultGameInfo);
        Session.set('tableId', null);
        Session.set('boardReady', false);
        return;
    }
    $.get('/games/' + game + '/game.json', function(data) {
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

Meteor.startup(function() {
    Meteor.call('games', function(err, games) {
//    if (games.length === 1)
//        Session.set('selectedGame', games[0]);
//    else
        Session.set('games', games);
    });
});
