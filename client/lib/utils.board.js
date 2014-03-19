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