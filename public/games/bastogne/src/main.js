gameModule = function() {
    var original = {
        MoveOp: MoveOp,
        moveTo: moveTo
    };
    moveTo = function(counter, targetHex) {
        console.log(counter, targetHex);
        var targetStackOwner = (targetHex.stack && targetHex.stack.map(function(it) {
            return ownerByCategory[it.category];
        })[0]);
        var counterOwner = ownerByCategory[counter.category];
        console.log(targetStackOwner, counterOwner);
        if (targetStackOwner !== undefined && counterOwner !== undefined && counterOwner !== targetStackOwner) {
            Operations.insert({
                op: 'JoinAttack',
                sourceHex: counter.position.id,
                targetHex: targetHex.id,
            });
        } else {
            original.moveTo(counter, targetHex);
        }
    };

    MoveOp = function(data) {
        var counter = byId(data.counter);
        if (counter.name.match(/ DG$/)) {
            return original.MoveOp(data);
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
        undo.push(original.MoveOp(data));
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
        MoveOp = original.MoveOp;
        hexClicked = original.hexClicked;
        delete CRT;
    };
}; 