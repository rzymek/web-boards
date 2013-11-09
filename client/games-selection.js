
Deps.autorun(function() {
    var game = S.selectedGame();
    if (game === undefined)
        return;
    $.get('/games/' + game + '/game.json', function(data) {
        data.board.image = '../javadoc.png'; //TODO: remove
        data.board.image = '../board-low.jpg'; //TODO: remove
        S.setGameInfo(data);
    });
});

Meteor.call('games', function(err, games) {
    if (games.length === 1)
        S.setSelectedGame(games[0]);
    else
        S.setGames(games);
});
