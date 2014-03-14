Template.scenario.visible = function() {
    return Operations.find({}, {fields: {_id: 1}}).count() === 0
            && !Template.join.guest()
            && Template.scenario.scenarios().length > 0;
};

Template.scenario.events({
    'change .scenarios': function(e) {
        var counters = byId('counters').children;
        var ids = [];
        for (var i = 0; i < counters.length; i++) {
            ids.push(counters[i].id);
        }
        ids.forEach(function(id) {
            RemovePieceOp({
                counterId: id
            });
        });

        var scenos = Session.get('scenarios');
        var selected = e.currentTarget.value;
        if (selected in scenos) {
            var setup = scenos[selected];
            for (var i = 0; i < setup.length; i++) {
                var it = setup[i];
                PlaceOp({
                    category: it[0],
                    name: it[1],
                    hexid: it[2],
                    _id: '_' + i
                });
            }
        }
    },
    'click #hide': function() {
        $('#scenarioNavBar').hide();
    }
});

Template.scenario.scenarios = function() {
    var s = Session.get('scenarios');
    return s ? [''].concat(Object.keys(s)) : [];
//    return [
//        " none ",
//        "1 Campaign",
//        "2 The First Day",
//        "3 The First Two Days",
//        "4 The Second Day",
//        "5 The Third and Fourth Days",
//        "6 Lake Crossing",
//        "7 Firefight on the Ice",
//        "8 The Battle for Kotisaari Island",
//        "9 Battle for the Hotel",
//        "10 The Battle for Hirvasvaara",
//        "11 Two on Two",
//        "12 The Fifth Day",
//        "13 The Fifth Day Historical",
//        "14 Alternate Campaign",
//        "15 Tanks at the Narrows",
//        "16 Overrun",
//        "17 Tutorial First Folly"
//    ];
};