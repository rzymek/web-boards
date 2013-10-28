/// <reference path="../TypedSession.api.d.ts"/>
/// <reference path="../api/core.d.ts"/>

function isTouchDevice() {
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
}

declare function jsSetup();
declare function setupGrid():SVGSVGElement;
declare var svgZoomAndPan:(e:SVGSVGElement)=>void;

var play = Template['play'];

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
            Meteor.call('undo');
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