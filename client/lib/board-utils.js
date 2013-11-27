getBoardScaling = function() {
    if (isTouchDevice()) {
        var MAX_BOARD_WIDTH = 3000;
        var gameInfo = Session.get('gameInfo');
        return gameInfo.board.width / MAX_BOARD_WIDTH;
    } else {
        return 1;
    }
};

isOnBoard = function(piece) {
    return piece && piece.parentElement && piece.parentElement.id !== 'piecesPanel';
};