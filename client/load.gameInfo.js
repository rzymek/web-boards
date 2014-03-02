Deps.autorun(function() {
    var tableId = Session.get('tableId');
    if (tableId === null) {
        Session.set('board.ready', false);
        Session.set('gameInfo', defaultGameInfo);
        Session.set('scenarios', null);
        Session.set('selectedGame', null);
        return;
    }
    var table = Tables.findOne(tableId, {fields: {game: 1}});
    if (!table) {
        return; // Tables are not ready yet. This method will be rerun after they're ready
    }
    $.get('/games/' + table.game + '/game.json', function(data) {
        Session.set('gameInfo', data);
    });
});


Deps.autorun(function() {
    if (!is('board.ready', 'sprites.ready'))
        return;
    var scenos = Session.get('gameInfo').scenarios;
    var game = Session.get('selectedGame');
    if (!scenos || !game)
        return;    
    var s = scenos['5.1 Battle for Noville'];
    for (var i = 0; i < s.length; i++) {
        var c = Object.keys(s[i])[0];
        var arg = {
            _id: 'c'+i,
            imageBase: '/games/' + game + '/images/',
            sides: [c],
            size: {
                width: 45,
                height: 45
            },
            hexid: s[i][c]
        };
        console.log(arg);
        PlaceOp(arg);
    }
    Session.set('scenario.ready', true);
});