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
    return copyTransformationItem(src, dest, TX.POSITION);
};

copyTransformationItem = function(src, dest, item) {
    if(src.transform === undefined)
        return;
    var srcTx = src.transform.baseVal;
    var destTx = ensureTransformListSize(byId('svg'), dest.transform.baseVal, item+1);
    destTx.getItem(item).setMatrix(srcTx.getItem(item).matrix);
};


/* http://stackoverflow.com/a/13111598/211205 */
getTBBox = function (el) {
    function pointToLineDist(A, B, P) {
        var nL = Math.sqrt((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y));
        return Math.abs((P.x - A.x) * (B.y - A.y) - (P.y - A.y) * (B.x - A.x)) / nL;
    }

    function dist(point1, point2) {
        var xs = 0,
            ys = 0;
        xs = point2.x - point1.x;
        xs = xs * xs;
        ys = point2.y - point1.y;
        ys = ys * ys;
        return Math.sqrt(xs + ys);
    }
    var b = el.getBBox(),
        objDOM = el,
        svgDOM = objDOM.ownerSVGElement;
    // Get the local to global matrix
    var matrix = svgDOM.getTransformToElement(objDOM).inverse(),
        oldp = [[b.x, b.y], [b.x + b.width, b.y], [b.x + b.width, b.y + b.height], [b.x, b.y + b.height]],
        pt, newp = [],
        obj = {},
        i, pos = Number.POSITIVE_INFINITY,
        neg = Number.NEGATIVE_INFINITY,
        minX = pos,
        minY = pos,
        maxX = neg,
        maxY = neg;

    for (i = 0; i < 4; i++) {
        pt = svgDOM.createSVGPoint();
        pt.x = oldp[i][0];
        pt.y = oldp[i][1];
        newp[i] = pt.matrixTransform(matrix);
        if (newp[i].x < minX) minX = newp[i].x;
        if (newp[i].y < minY) minY = newp[i].y;
        if (newp[i].x > maxX) maxX = newp[i].x;
        if (newp[i].y > maxY) maxY = newp[i].y;
    }
    // The next refers to the transformed object itself, not bbox
    // newp[0] - newp[3] are the transformed object's corner
    // points in clockwise order starting from top left corner
    obj.newp = newp; // array of corner points
    obj.RAWwidth = pointToLineDist(newp[1], newp[2], newp[0]) || 0;
    obj.RAWheight = pointToLineDist(newp[2], newp[3], newp[0]) || 0;
    obj.toplen = dist(newp[0], newp[1]);
    obj.rightlen = dist(newp[1], newp[2]);
    obj.bottomlen = dist(newp[2], newp[3]);
    obj.leftlen = dist(newp[3], newp[0]);
    // The next refers to the transformed object's bounding box
    obj.BBx = minX;
    obj.BBy = minY;
    obj.BBx2 = maxX;
    obj.BBy2 = maxY;
    obj.width = maxX - minX;
    obj.height = maxY - minY;
    return obj;
};