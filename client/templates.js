/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/jquery.d.ts" />
/// <reference path="../common/model.d.ts"/>
/// <reference path="../common/vassal.d.ts"/>
/// <reference path="TypedSession.d.ts"/>
/// <reference path="svg_zoom_and_pan.d.ts"/>
/// <reference path="main.d.ts"/>
/// <reference path="api/core.d.ts"/>
function isTouchDevice() {
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
}

var main = Template['main'];
var welcome = Template['welcome'];
var play = Template['play'];
var pieces = Template['pieces'];
var controls = Template['controls'];

main['gameSelected'] = function () {
    return S.selectedGame() != null;
};

play['board'] = function () {
    var gameInfo = S.gameInfo();
    return { w: gameInfo.board.width, h: gameInfo.board.height };
};
play['boardImg'] = function () {
    return '/games/' + S.selectedGame() + '/' + S.gameInfo().board.image;
};
if (!isTouchDevice()) {
    play['svgWidth'] = '100%';
    play['svgHeight'] = '100%';
} else {
    play['svgWidth'] = function () {
        return S.gameInfo().board.width;
    };
    play['svgHeight'] = function () {
        return S.gameInfo().board.height;
    };
}
play['status'] = function () {
    return Meteor.status();
};

play.rendered = function () {
    if (S.gameInfo().board.grid === null) {
        return;
    }
    console.log('rendered', S.gameInfo());
    var svg = setupGrid();
    console.log('grid setup');
    if (!isTouchDevice()) {
        svgZoomAndPan.setup(svg);
        jsSetup();
    } else {
        document.getElementById('panel').style.display = 'none';
    }
    Meteor.subscribe('operations');
};
controls.events({
    'click button': function (event) {
        var button = event.currentTarget;
        var txt = button.textContent;
        if (txt === 'Menu') {
            $('div.menu-folded').hide();
            $('div.menu-unfolded').show();
        } else if (txt === 'Hide') {
            $('div.menu-folded').show();
            $('div.menu-unfolded').hide();
        }
    }
});
pieces['categories'] = function () {
    return S.gameInfo().pieces.map(function (pieces) {
        return pieces.category;
    });
};
Template['selectedPieces']['pieces'] = function () {
    var g = S.selectedGame();
    var cat = S.piecesCategory();
    var inCat = S.gameInfo().pieces.filter(function (p) {
        return p.category === cat;
    });
    if (inCat.length > 0)
        return inCat[0].list.map(function (p) {
            return p.images[0];
        }).map(function (name) {
            return '/games/' + g + '/images/' + name;
        });
else
        return [];
};
pieces.events({
    'change select': function (e) {
        var combo = e.target;
        var category = combo.options[combo.selectedIndex].value;
        S.setPiecesCategory(category);
        return true;
    }
});

pieces.events({
    'click img': function (e) {
        var img = e.currentTarget;
        $('#panel .panel-body img').removeClass('pieceSelected');
        $(img).addClass('pieceSelected');
        ctx.selected = new HTMLCounter(img);
    }
});

welcome['games'] = function () {
    return S.games();
};
welcome.events({
    'click button': function (e) {
        var t = e.currentTarget;
        S.setSelectedGame(t.value);
    }
});
