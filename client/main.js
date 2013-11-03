Session.setDefault('gameInfo', {
    pieces: [],
    board: {
        image: '',
        width: 0,
        height: 0,
        grid: null
    }
});
Session.setDefault('selectedPieces', '');

function byId(id) {
    return document.getElementById(id);
}
;

function svg() {
    return byId('svg');
}
;

hexClicked = function(e) {
    var selector = Snap(svg()).select('#select');
    selector.node.style.visibility = 'hidden';
    if (selector.atHex) {
        alignStack(selector.atHex, selector.stack);
    }

    var use = e.currentTarget;
    if (ctx.selected === null) {
        var stack = ctx.places[use.id];
        if (stack === undefined || stack === null)
            return;
        if (stack.stack.length > 1) {
            showStackSelector(use, stack.stack);
        } else if (stack.stack.length === 1) {
            stack.stack[0].style.filter = 'url(#select)';
        }
        return;
    }
    if (ctx.selected) {
        var data = {op: 'PlaceOp', image: ctx.selected.getImage(), hexid: use.id};
        Operations.insert(data);
        Session.set('selectedPiece', null);
    }
};

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

Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    if (selected === null) {
        ctx.selected = null;
    } else {
        var piece = document.getElementById(selected);
        if (piece !== null) {
            console.log(piece.parentElement);
            if (piece.parentElement.id === 'piecesPanel') {
                ctx.selected = new HTMLCounter(piece);
            } else {
                ctx.selected = new SVGCounter(piece);
            }
        }
    }
});
Meteor.startup(function() {
    Operations.find().observe({
        added: function(data) {
            runOp(data);
        },
        removed: function(doc) {
            undoOp(data);
        }
    });
});