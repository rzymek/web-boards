ScenarioOp = function(data) {
    var scenos = Session.get('scenarios');
    var selected = data.name;
    var setup = scenos[selected];
    var ids = [];
    for (var i = 0; i < setup.length; i++) {
        var it = setup[i];
        var id = '_' + i;
        PlaceOp({
            category: it[0],
            name: it[1],
            hexid: it[2],
            _id: id
        });
        ids.push(id);
    }

    return function() {
        ids.forEach(function(id) {
            RemovePieceOp({
                counterId: id
            });
        });
    };
};
