byId = function(id) {
    return document.getElementById(id);
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

getNatural = function(e) {
    if (e.naturalWidth !== undefined && e.naturalHeight !== undefined) {
        return {width: e.naturalWidth, height: e.naturalHeight};
    } else {
        var img = new Image();
        img.src = e.src;
        return {width: img.width, height: img.height};
    }
};

isTouchDevice = function() {
    if (Session.get('config').indexOf('mobile') > 0)
        return true;
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
};

removeChildren = function(node) {
    while (node.firstChild) {
        node.removeChild(node.firstChild);
    }
};

/*
 * Assumtion:
 * transformList is [
 *  translate,
 *  scale,
 *  rotate
 * ]
 */
copyTransformation = function(src, dest) {
    if(src.transform === undefined)
        return;
    var srcTx = src.transform.baseVal;
    var destTx = ensureTransformListSize(byId('svg'), dest.transform.baseVal, 1);
    destTx.getItem(TX.POSITION).setMatrix(srcTx.getItem(TX.POSITION).matrix);
};