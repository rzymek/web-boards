var isAttack = function(counter, targetHex) {
    var targetStackOwner = _.first(targetHex.stack && targetHex.stack.map(function(it) {
        return getOwner(it);
    }));
    var counterOwner = getOwner(counter);
    return (targetStackOwner && counterOwner && counterOwner !== targetStackOwner);
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
                Operations.insert({
                    op: 'DeclareBarrage',
                    counterId: counter.id,
                    targetHex: targetHex.id
                });
            } else {
                Operations.insert({
                    op: 'DeclareAttack',
                    sourceHex: counter.position.id,
                    targetHex: targetHex.id
                });
            }
        } else {
            original.moveTo(counter, targetHex);
        }
    };
    function getAttackTarget(hex) {
        if (hex.attack) {
            return hex;
        } else if (hex.attackSource) {
            return hex.attackSource.target;
        } else {
            return null;
        }
    }
    function abandonAttack(hex) {
        if (!hex)
            return function() {
            };
        var undoData = hex.attack;
        _.values(hex.attack.arrows).forEach(function(el) {
            el.remove();
        });
        _.values(hex.attack.from).forEach(function(sourceHex) {
            delete sourceHex.attackSource;
        });
        hex.attack.odds.remove();
        delete hex.attack;
        return function() {
            hex.attack = undoData;
            var overlays = byId('overlays');
            _.values(hex.attack.arrows).forEach(function(el) {
                overlays.appendChild(el);
            });
            _.values(hex.attack.from).forEach(function(sourceHex) {
                sourceHex.attackSource = {
                    target: hex
                };
            });
            overlays.appendChild(hex.attack.odds);
        };
    }
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
        undo.push(cancelBarrage(counter));
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
        undo.push(abandonAttack(getAttackTarget(from)));
        undo.push(abandonAttack(getAttackTarget(to)));
        if ((from.stack || []).length === 0) {
            undo.push(abbortBarrage1(from.barrage));
        }
        return function() {
            undo.reverse().forEach(function(fn) {
                fn();
            });
        };
    };
    console.log('here');

    var deps = [];
    deps.push(showMovement());
//    deps.push(showRoadMovement());

    return function() {
        deps.forEach(function(c) {
            c.stop();
        });
        MoveOp = original.MoveOp;
        hexClicked = original.hexClicked;
        delete CRT;
    };
};