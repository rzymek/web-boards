
Deps.autorun(function() {
    var game = Session.get('selectedGame');
    console.log("selectedGame",game);
    if (game === undefined || game === null)
        return;
    $.get('/games/' + game + '/game.json', function(data) {
        if (location.search.indexOf('sfw') >= 0) {
            data.board.image = '../javadoc.png'; //TODO: remove
        } else if (location.search.indexOf('empty') >= 0) {
            data.board.image = '../../../empty.png'; //TODO: remove
        } else if (location.search.indexOf('hires') < 0) {
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
