function updateIdRefs(data, idmap) {
    for(var key in data) {
        var value = data[key];
        var updatedId = idmap[value];
        if(updatedId !== undefined) {
            data[key] = updatedId;
        }
    }
}

ScenarioOp = function(data) {
    var scenos = Session.get('scenarios');
    var selected = data.name;
    var setup = scenos[selected];
    var undo = [];
    var idmap = {};
    for (var i = 0; i < setup.length; i++) {
        var data = setup[i];
        var setupId = '_'+i;
        idmap[data._id] = setupId;
        updateIdRefs(data, idmap);
        data._id = setupId;
        undo.push(this[data.op](data));
    }
    return function() {
        undo.forEach(function(fn) {
            fn();
        });
    };
};
