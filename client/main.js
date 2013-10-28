var svgns = "http://www.w3.org/2000/svg";
var boardScale = 1;

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

function hexClicked(e) {
    if(sprites) {
        selector.node.style.visibility='hidden';
        alignStack(selector.atHex, selector.stack);
        console.log(selector.node);
    }
    var use = e.currentTarget;
    if (ctx.selected === null) {        
        var stack = ctx.places[use.id];
        if(stack !== undefined && stack.stack.length > 1) {
            showStackSelector(use, stack.stack);
        }
        return;
    }
    if (ctx.selected) {
        var op = new PlaceOperation({ image: ctx.selected.getImage(), hexid: use.id });
        Operations.insert(op.data);
        ctx.selected = null;
    }
}

setupGrid = function () {
    var path = document.getElementById('hex');
    var svg = document.getElementById('svg');
    var layer = svg.getElementById('hexes');
    if (layer.childNodes.length > 0) {
        return svg;
    }
    var hex2hex = { y: 125.29 * boardScale, x: 108.5 * boardScale };
    var board = S.gameInfo().board;

    var A = 2 * 125.29 / Math.sqrt(3);
    var use;
    var yn = board.height / hex2hex.y;
    var xn = board.width / hex2hex.x;
    for (var y = 0; y < yn; y++) {
        for (var x = 0; x < xn; x++) {
            var hx = (80 * boardScale + x * hex2hex.x);
            var hy = (65 * boardScale + y * hex2hex.y);
            var hscale = A / 100 * boardScale;
            if (x % 2) {
                hy -= hex2hex.y / 2;
            }
            use = document.createElementNS(svgns, 'use');
            use.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", '#hex');
            use.setAttribute('transform', 'translate(' + hx + ' ' + hy + ') scale(' + hscale + ')');
            use.setAttribute('class', 'hex');
            use.id = 'h' + x + '_' + y;
            use.onclick = hexClicked;
            layer.appendChild(use);
        }
    }
    return svg;
};

Deps.autorun(function () {
    var game = S.selectedGame();
    if (game !== undefined) {
        $.get('/games/' + game + '/game.json', function (data) {
            data.board.image = 'javadoc.png'; //TODO: remove
            S.setGameInfo(data);
        });
    }
});

Meteor.call('games', function (err, games) {
    if (games.length === 1)
        S.setSelectedGame(games[0]);
else
        S.setGames(games);
});

Operations.find().observe({
    'removed': function (doc) {
        var op = new PlaceOperation(doc);
        op.undo();
    }
});

// Deps.autorun(function () {
//     var status = Meteor.status();
//     console.log(status);
//     if (status.connected) {
//         // alert(status.status);
//     }
// });
