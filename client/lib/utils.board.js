isOnBoard = function(piece) {
    return piece && piece.parentElement && piece.parentElement.id !== 'piecesPanel';
};

getTable = function(options) {
    var id = Session.get('tableId');
    if (!id)
        return null;
    if (options === undefined) {
        options = {fields: {ops: 0}};
    }
    return Tables.findOne(id, options);
};

getGame = function() {
    var table = getTable({fields: {game: 1}});
    return table ? table.game : null;
};

copyTransformation = function(src, dest) {
    return copyTransformationItem(src, dest, TX.POSITION);
};

copyTransformationItem = function(src, dest, item) {
    if (src.transform === undefined)
        return;
    var srcTx = src.transform.baseVal;
    var destTx = ensureTransformListSize(byId('svg'), dest.transform.baseVal, item + 1);
    destTx.getItem(item).setMatrix(srcTx.getItem(item).matrix);
};

ensureTransformListSize = function(svg, transformList, size) {
    for (var i = transformList.numberOfItems; i < size; i++) {
        transformList.appendItem(svg.createSVGTransform());
    }
    return transformList;
};

getAdjacentIds = function(hexId) {
    function toId(x, y) {
        return 'h' + x + '_' + y;
    }
    var split = /h([0-9]+)_([0-9]+)/.exec(hexId);
    var p = {
        x: parseInt(split[1]),
        y: parseInt(split[2])
    };
    var o = (p.x % 2 === 0) ? 0 : -1;
    return [
        toId(p.x, p.y + 1),
        toId(p.x - 1, p.y + 1 + o), toId(p.x + 1, p.y + 1 + o),
        toId(p.x - 1, p.y + o), toId(p.x + 1, p.y + o),
        toId(p.x, p.y - 1)
    ];
};
/*
Meteor.startup(function() {
    Deps.autorun(function() {
        if (!is('board.ready'))
            return;
        var dyncss = byId('dyncss');
        byId('hexes').onmousemove = function(event) {
            var hex = event.target.correspondingUseElement || event.target;
            dyncss.textContent = getAdjacentIds(hex.id).map(function(it) {
                return '#' + it;
            }).join(',') + ' { fill: black !important }';
        };
    });
});
*/