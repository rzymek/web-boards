/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/jquery.d.ts" />
/// <reference path="../common/model.d.ts"/>
/// <reference path="../common/vassal.d.ts"/>
/// <reference path="TypedSession.d.ts"/>
/// <reference path="svg_zoom_and_pan.d.ts"/>
/// <reference path="main.d.ts"/>

function isTouchDevice() {
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
}

var main = Template['main'];
var welcome = Template['welcome'];
var play = Template['play'];
var pieces = Template['pieces'];
var controls = Template['controls'];

main['gameSelected']  = () => S.selectedGame() != null;

play['board'] = () => {
    var gameInfo = S.gameInfo();
    return {w: gameInfo.board.width, h: gameInfo.board.height};
};
play['boardImg'] = () => {
    return '/games/' + S.selectedGame() + '/' + S.gameInfo().board.image;
}
if (!isTouchDevice()) {
    play['svgWidth'] = '100%';
    play['svgHeight'] = '100%';
} else {
    play['svgWidth'] = () => S.gameInfo().board.width;
    play['svgHeight'] = () => S.gameInfo().board.height;
}
play['status'] = () => Meteor.status();
play.rendered = () => {
    var svg = setupGrid();
    if (!isTouchDevice()) {
        svgZoomAndPan.setup(svg);
        jsSetup();
    } else {
        document.getElementById('panel').style.display = 'none';
    }
}
play.events({
    'click': (e:MouseEvent) => {
        console.log(e.currentTarget);
    }
});


controls.events({
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
pieces.events({
    'click img': (e:MouseEvent) => {
        $('#panel .panel-body img').removeClass('pieceSelected');
        $(e.currentTarget).addClass('pieceSelected')
    }
});

welcome['games'] = () => S.games();
welcome.events({
    'click button': (e:MouseEvent) => {
        var t = <HTMLButtonElement>e.currentTarget;
        S.setSelectedGame(t.value);
    }
})