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


function svg() {
    return byId('svg');
}

hexClicked = function(e) {
    var selector = Snap(svg()).select('#select');
    selector.node.style.visibility = 'hidden';
    if (selector.atHex) {
        alignStack(selector.atHex, selector.stack);
    }

    var use = e.currentTarget;
    if (ctx.selected === null) {
        var stack = use.stack;
        if (stack === undefined || stack === null)
            return;
        if (stack.length > 1) {
            showStackSelector(use, stack);
        } else if (stack.length === 1) {
            Session.set('selectedPiece', stack[0].id);
        }
        return;
    }

    if (ctx.selected) {
        var data = null;
        if(isOnBoard(ctx.selected.img)) {
            data = {op: 'MoveOp', counter: ctx.selected.img.id, to: use.id};
        }else{
            data = {op: 'PlaceOp', image: ctx.selected.getImage(), hexid: use.id};
        };
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

function isOnBoard(piece) {
    return piece && piece.parentElement && piece.parentElement.id !== 'piecesPanel';
}
Deps.autorun(function() {
    var selected = Session.get('selectedPiece');
    if (ctx.selected) {
        var piece = ctx.selected.img;
        if(isOnBoard(piece)) {
            //deselect current onboard piece
            //pieces in panel are deselected reactivly
            piece.style.filter='';
        }
    }
    if (selected) {
        var piece = document.getElementById(selected);
        if (!piece) {
            // svg board is not ready, can happen during code-push
            ctx.selected = null;
            return;
        }
        if (isOnBoard(piece)) {
            ctx.selected = new SVGCounter(piece);
            piece.style.filter='url(#select)';
        } else {
            ctx.selected = new HTMLCounter(piece);
        }
    } else {
        ctx.selected = null;
    }
});
Meteor.startup(function() {
    Operations.find().observe({
        added: function(data) {
            runOp(data);
        },
        removed: function(data) {
            undoOp(data);
        }
    });
});