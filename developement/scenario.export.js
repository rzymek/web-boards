if (Meteor.isClient) {
    var saveSceno = function() {
        var scenos = Session.get('scenarios');
        return Operations.find().fetch().map(function(data) {
            if (data.op === 'ScenarioOp') {
                var selected = data.name;
                var setup = scenos[selected];
                console.log(data, selected, setup);
                return setup;
            } else {
                return [data];
            }
        }).reduce(function(a, b) {
            return a.concat(b);
        }).map(function(it) {
            delete it.createdAt;
            delete it.tableId;
            return it;
        });
    };
    scenoExport = function() {
        var output = saveSceno();
        var json = JSON.stringify(output);//,null, ' ');
        window.open('data:application/json;' + (window.btoa ? 'base64,' + btoa(json) : json));
    };
}