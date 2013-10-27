/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/jquery.d.ts" />
/// <reference path="../common/model.d.ts"/>
/// <reference path="../common/vassal.d.ts"/>
/// <reference path="TypedSession.api.d.ts"/>
/// <reference path="api/core.d.ts"/>

function isTouchDevice() {
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
}

var main = Template['main'];
var welcome = Template['welcome'];
var play = Template['play'];
var pieces = Template['pieces'];
var controls = Template['controls'];

main['gameSelected'] = () => S.selectedGame() != null;

play['board'] = () => {
    var gameInfo = S.gameInfo();
    return {w: gameInfo.board.width, h: gameInfo.board.height};
};
play['boardImg'] = () => {
    if (S.selectedGame())
        return '/games/' + S.selectedGame() + '/' + S.gameInfo().board.image;
    else
        return null;
}
if (!isTouchDevice()) {
    play['svgWidth'] = '100%';
    play['svgHeight'] = '100%';
} else {
    play['svgWidth'] = () => S.gameInfo().board.width;
    play['svgHeight'] = () => S.gameInfo().board.height;
}
play['status'] = () => Meteor.status();

declare function jsSetup();
declare function setupGrid():SVGSVGElement;
declare var svgZoomAndPan:(e:SVGSVGElement)=>void;

play.rendered = () => {
    if (S.gameInfo().board.grid === null) {
        return;
    }
    var svg = setupGrid();
    if (!isTouchDevice()) {
        svgZoomAndPan(svg);
        jsSetup();
    } else {
        document.getElementById('panel').style.display = 'none';
    }
    Meteor.subscribe('operations');
    ctx.menu = {
        'Undo': () => {

            console.log('undo');
        },
        'Toggle units': () => {
            console.log('toggle');
        }
    };
    S.setMenuItems((() => {
        var list = [];
        var menu = ctx.menu;
        for (var key in menu) {
            if (menu.hasOwnProperty(key))
                list.push(key);
        }
        return list;
    })());
}
controls['menu'] = () => S.menuItems();

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
        } else {
            ctx.menu[txt]();
        }
    }
})
pieces['categories'] = () => [''].concat(S.gameInfo().pieces.map((pieces:Pieces) => pieces.category));

Template['selectedPieces']['pieces'] = () => {
    var g = S.selectedGame();
    var cat = S.piecesCategory();
    var inCat = S.gameInfo().pieces.filter((p:Pieces)=>
            p.category === cat
    );
    if (inCat.length > 0)
        return inCat[0].list
            .map((p:Piece) => p.images[0])
            .map((name:string) => '/games/' + g + '/images/' + name);
    else
        return [];
}
pieces.events({
    'change select': (e:Event) => {
        var combo = <HTMLSelectElement>e.target;
        var category = combo.options[combo.selectedIndex].value;
        S.setPiecesCategory(category);
        return true;
    }
});

pieces.events({
    'click img': (e:MouseEvent) => {
        var img = <HTMLImageElement>e.currentTarget;
        $('#panel .panel-body img').removeClass('pieceSelected');
        $(img).addClass('pieceSelected');
        ctx.selected = <Counter>new HTMLCounter(img);
    }
});

welcome['games'] = () => S.games();
welcome.events({
    'click button': (e:MouseEvent) => {
        var t = <HTMLButtonElement>e.currentTarget;
        S.setSelectedGame(t.value);
    }
})