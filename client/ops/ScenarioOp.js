ScenarioOp = function(data) {
    var scenos = Session.get('scenarios');
    var selected = data.name;
    var setup = scenos[selected];
    var undo = [];
    for (var i = 0; i < setup.length; i++) {
        var it = setup[i];
        it._id = '_' + i;
        undo.push(this[it.op](it));
    }

    return function() {
        undo.forEach(function(fn) {
            fn();
        });
    };
};
