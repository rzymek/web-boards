defaultGameInfo = {
    pieces: [],
    board: {
        image: '',
        width: 1,
        height: 1,
        grid: null
    }
}

Session.setDefault('gameInfo', defaultGameInfo);
Session.setDefault('tableId', null);
Session.setDefault('selectedGame', null);
Session.setDefault('config', '');
Session.setDefault('boardReady', false);

sprites = null;
