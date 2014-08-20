function isGameStarted() {
    return Operations.find({}, {fields: {_id: 1}}).count() !== 0
}

Template.scenario.visible = function() {
    return  Session.equals('ops.ready', true)
            && !isGameStarted()
//            && !Template.join.guest()
            && Template.scenario.scenarios().length > 0;
};

Operations.before.insert(function(userId, doc) {
    if (selectedSceno === null)
        return;
    if (doc.op === 'ScenarioOp')
        return;
    if (!isGameStarted()) {
        callUndoCurrentSceno();
        Operations.insert({
            op: 'ScenarioOp',
            name: selectedSceno
        });
    }
});

function callUndoCurrentSceno() {
    if (undoCurrentSceno) {
        undoCurrentSceno();
        undoCurrentSceno = null;
    }
}

var selectedSceno = null;
var undoCurrentSceno = null;

Template.scenario.events({
    'change .scenarios': function(e) {
        callUndoCurrentSceno();
        var scenos = Session.get('scenarios');
        selectedSceno = e.currentTarget.value;
        if (selectedSceno in scenos) {
            undoCurrentSceno = ScenarioOp({name: selectedSceno});
        }
    },
    'click .selectSceno': function() {
        if (selectedSceno === null) {
            $('#scenarioNavBar').hide();
        } else {
            callUndoCurrentSceno();
            Operations.insert({
                op: 'ScenarioOp',
                name: selectedSceno
            });
        }
    },
    'click #hide': function() {
        $('#scenarioNavBar').hide();
    }
});

Template.scenario.scenarios = function() {
    var s = Session.get('scenarios');
    return s ? [''].concat(Object.keys(s)) : [];
};