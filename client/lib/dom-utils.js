byId = function(id) {
    return document.getElementById(id);
};

isOnBoard = function(piece) {
    return piece && piece.parentElement && piece.parentElement.id !== 'piecesPanel';
};