var isAttack = function(counter, targetHex) {
    var targetStackOwner = (targetHex.stack && targetHex.stack.map(function(it) {
        return ownerByCategory[it.category];
    })[0]);
    var counterOwner = ownerByCategory[counter.category];
    return (targetStackOwner !== undefined && counterOwner !== undefined && counterOwner !== targetStackOwner);
};
var isArty = function(counter) {
    return (getUnitInfo(counter).artyType !== undefined);
};
gameModule = function() {
    var original = {
        MoveOp: MoveOp,
        moveTo: moveTo
    };
    moveTo = function(counter, targetHex) {
        if (isAttack(counter, targetHex)) {
            if (isArty(counter)) {
                if (counter.barrage) {
                    if (counter.barrage.target.id === targetHex.id) {
                        Operations.insert({
                            op: 'UndeclareBarrage',
                            counterId: counter.id,
                            targetHex: targetHex.id
                        });
                    } else {
                        Operations.insert({
                            op: 'SwitchBarrage',
                            counterId: counter.id,
                            targetHex: targetHex.id
                        });
                    }
                } else {
                    Operations.insert({
                        op: 'DeclareBarrage',
                        counterId: counter.id,
                        targetHex: targetHex.id
                    });
                }
            } else {
                Operations.insert({
                    op: 'JoinAttack',
                    sourceHex: counter.position.id,
                    targetHex: targetHex.id
                });
            }
        } else {
            var from = counter.position;
            console.log('from.attack',from.attack);
            var targetHexes = [
                targetHex.attack && from.attack.from && targetHex.id,
                from.attack && from.attack.target && from.attack.target.id,
                from.attack && from.attack.from && from.attack.from.id
            ];
            console.log('targetHexes',targetHexes);
            var abandonAttack = {
                op: 'UndeclareAttack',
                targetHexes: targetHexes.filter(function(it) {
                    return it !== undefined;
                })
            };
            console.log(abandonAttack);
            if (abandonAttack.targetHexes.length > 0) {
                Operations.insert(abandonAttack);
            }
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
        if (DGfrom.length > 0 && DGto.length === 0) {
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