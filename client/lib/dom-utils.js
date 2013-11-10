byId = function(id) {
    return document.getElementById(id);
};

isOnBoard = function(piece) {
    return piece && piece.parentElement && piece.parentElement.id !== 'piecesPanel';
};

getNatural = function (e) {
    if (e.naturalWidth !== undefined && e.naturalHeight !== undefined) {
        return {width: img.naturalWidth, height: img.naturalHeight};
    } else {
        var img = new Image();
        img.src = e.src;
        return {width: img.width, height: img.height};
    }
};