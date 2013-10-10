/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/jquery.d.ts" />
/// <reference path="svg_zoom_and_pan.d.ts"/>
/// <reference path="../common/model.d.ts"/>

var svgns = "http://www.w3.org/2000/svg";

var boardScale = 1;
var board = {w: 6800 * boardScale, h: 4400 * boardScale };

function setupTemplate() {
    Template['main']['gameSelected'] = () => Session.get('selectedGame') != null
    Template['welcome']['games'] = () => Session.get('games')

    var play = Template['play'];
//    play['game'] = () => Session.get('selectedGame');
    play['board'] = {w: board.w, h: board.h};
    play['boardImg'] = () => {
        var gameInfo = Session.get('gameInfo');
        if (gameInfo !== undefined) {
            return '/games/' + Session.get('selectedGame') + '/' + gameInfo.board.image;
        } else {
            return '';
        }
    }
    if (!isTouchDevice()) {
        play['svgWidth'] = '100%';
        play['svgHeight'] = '100%';
    } else {
        play['svgWidth'] = board.w;
        play['svgHeight'] = board.h;
    }
    play['status'] = () => Meteor.status();

    Template['controls'].events({
        'click button': (event:MouseEvent) => {
            var button = <HTMLButtonElement>event.currentTarget;
            var txt = button.textContent;
            if (txt === 'Menu') {
                $('div.menu-folded').hide();
                $('div.menu-unfolded').show();
            } else if (txt === 'Hide') {
                $('div.menu-folded').show();
                $('div.menu-unfolded').hide();
            }
        }
    })
    Template['pieces'].events({
        'click img': (e:MouseEvent) => {
            $('#panel .panel-body img').removeClass('pieceSelected');
            $(e.currentTarget).addClass('pieceSelected')
        }
    });

    Template['welcome'].events({
        'click button': (e:MouseEvent) => {
            var t = <HTMLButtonElement>e.currentTarget;
            Session.set('selectedGame', t.value);
        }
    })
}

function setupGrid() {
    var path = <SVGPathElement><any>document.getElementById('hex');
    var svg = <SVGSVGElement><any>document.getElementById('svg');
    var hex2hex = { y: 125.29 * boardScale, x: 108.5 * boardScale};

    var A = 2 * 125.29 / Math.sqrt(3);
    var hexes = [];
    var use:SVGUseElement;
    var yn = board.h / hex2hex.y;
    var xn = board.w / hex2hex.x;
    for (var y = 0; y < yn; y++) {
        var s = A / 2;
        for (var x = 0; x < xn; x++) {
            var hx = (80 * boardScale + x * hex2hex.x);
            var hy = (65 * boardScale + y * hex2hex.y);
            var hscale = A / 100 * boardScale;
            if (x % 2) {
                hy -= hex2hex.y / 2;
            }
            use = <SVGUseElement>document.createElementNS(svgns, 'use');
            use.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", '#hex');
            use.setAttribute('transform', 'translate(' + hx + ' ' + hy + ') scale(' + hscale + ')');
            use.setAttribute('class', 'hex');
            svg.appendChild(use);
        }
    }
    return svg;
}

function isTouchDevice() {
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
}

var operations = function () {
    Operations.find().observeChanges({
        added: (id, field) => {

        }
    });
};
declare var gameInfo;

Deps.autorun(()=> {
    var game = Session.get('selectedGame');
    if (game !== undefined) {
        console.log('game selected:', game);
        $.get('/games/' + game + '/game.json', (data:any) => {
            console.log('got game info:', data.board);
            data.board.image = 'board-low.jpg'; //TODO: remove
            Session.set('gameInfo', data);
        });
    }
});

Meteor.call('games', (err, games:String[]) => {
    if (games.length === 1)
        Session.set('selectedGame', games[0])
    else
        Session.set('games', games);
})

Template['play'].rendered = () => {
    console.log('play rendered');
    var svg = setupGrid();
    if (!isTouchDevice()) {
        svgZoomAndPan.setup(svg);
        jsSetup();
    } else {
        document.getElementById('panel').style.display = 'none';
    }
    operations();
}

declare function jsSetup();
setupTemplate();
