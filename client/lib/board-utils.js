isOnBoard = function(piece) {
    return piece && piece.parentElement && piece.parentElement.id !== 'piecesPanel';
};