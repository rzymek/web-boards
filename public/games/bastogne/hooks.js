gameModule = function() {
    var origMoveOp = MoveOp;
    MoveOp = function(data) {
        var counter = byId(data.counter);       
        if (counter.name.match(/ DG$/)) {
            return origMoveOp(data);
        }
        var from = counter.position;
        var to = byId(data.to);
        var DGfrom = from.stack ? from.stack.filter(
                function(c) {
                    return c.name.match(/ DG$/);
                }) : [];
        var DGto = to.stack ? to.stack.filter(
                function(c) {
                    return c.name.match(/ DG$/);
                }) : [];

        var undo = [];
        undo.push(origMoveOp(data));
        if (DGfrom.length > 0) {
            undo.push(PlaceOp({
                _id: (new Meteor.Collection.ObjectID()).toHexString(),
                name: DGfrom[0].name,
                category: DGfrom[0].category,
                hexid: data.to
            }));
        }
        if (from.stack && from.stack.length === DGfrom.length) {
            DGfrom.forEach(function(it) {
                undo.push(RemoveElementOp({element: it.id}));
            });
        }
        return function() {
            undo.reverse().forEach(function(fn) {
                fn();
            });
        };
    };

    return function() {
        MoveOp = origMoveOp;
    };
};


