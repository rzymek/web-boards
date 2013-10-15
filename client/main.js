/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/jquery.d.ts" />
/// <reference path="svg_zoom_and_pan.d.ts"/>
/// <reference path="../common/model.d.ts"/>
/// <reference path="../common/vassal.d.ts"/>
/// <reference path="TypedSession.api.d.ts"/>
/// <reference path="api/core.d.ts"/>
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
    if (ctx.selected === null)
        return;
    var img = document.createElementNS(svgns, 'image');
    img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", ctx.selected.getImage());
    var use = e.currentTarget;
    img.setAttribute('transform', use.getAttribute('transform').replace(new RegExp('scale\(.*\)'), ''));
    img.width.baseVal.value = 75;
    img.height.baseVal.value = 75;
    var svg = document.getElementById('svg');
    svg.getElementById('counters').appendChild(img);
    console.log(img);
}

setupGrid = function () {
    var path = document.getElementById('hex');
    var svg = document.getElementById('svg');
    var layer = svg.getElementById('hexes');
    var hex2hex = { y: 125.29 * boardScale, x: 108.5 * boardScale };
    var board = S.gameInfo().board;

    var A = 2 * 125.29 / Math.sqrt(3);
    var hexes = [];
    var use;
    var yn = board.height / hex2hex.y;
    var xn = board.width / hex2hex.x;
    for (var y = 0; y < yn; y++) {
        var s = A / 2;
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
            data.board.image = 'board-low.jpg';
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
