
Deps.autorun(function() {
    var game = Session.get('selectedGame');
    if (game === undefined)
        return;
    $.get('/games/' + game + '/game.json', function(data) {
        data.board.image = '../javadoc.png'; //TODO: remove
        data.board.image = '../board-low.jpg'; //TODO: remove
        Session.set('gameInfo', data);
    });
});

Meteor.call('games', function(err, games) {
    if (games.length === 1)
        Session.set('selectedGame', games[0]);
    else
        Session.set('games', games);
});
