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

Session.setDefault('selectedGame', null);
Session.setDefault('tableId', null);

sprites = null;
