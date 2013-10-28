/// <reference path="../TypedSession.api.d.ts"/>
/// <reference path="../../packages/typescript-libs/jquery.d.ts" />
/// <reference path="../api/core.d.ts"/>

var pieces = Template['pieces'];
pieces['categories'] = () => [''].concat(S.gameInfo().pieces.map((pieces:Pieces) => pieces.category));
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
