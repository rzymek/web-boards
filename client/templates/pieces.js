/// <reference path="../TypedSession.api.d.ts"/>
/// <reference path="../../packages/typescript-libs/jquery.d.ts" />
/// <reference path="../api/core.d.ts"/>
var pieces = Template['pieces'];
pieces['categories'] = function () {
    return [''].concat(S.gameInfo().pieces.map(function (pieces) {
        return pieces.category;
    }));
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
