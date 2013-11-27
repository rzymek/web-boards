byId = function(id) {
    return document.getElementById(id);
};

isOnBoard = function(piece) {
    return piece && piece.parentElement && piece.parentElement.id !== 'piecesPanel';
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
    if (location.search.indexOf('mobile') > 0)
        return true;
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
}
getBoardScaling = function() {
    if (isTouchDevice()) {
        var MAX_BOARD_WIDTH = 3000;
        var gameInfo = Session.get('gameInfo');
        return gameInfo.board.width / MAX_BOARD_WIDTH;
    } else {
        return 1;
    }
}

removeChildren = function(node) {
    while (node.firstChild) {
        node.removeChild(node.firstChild);
    }
}