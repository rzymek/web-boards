var MoveOpStickyDG = function(original, data) {
    var counter = byId(data.counter);
    if (counter.name.match(/ DG$/)) {
        return original.MoveOp(data);
    }
    var from = counter.position;
    var to = byId(data.to);
    var DGfrom = from.stack ? from.stack.filter(isDGMarker) : [];
    var DGto = to.stack ? to.stack.filter(isDGMarker) : [];

    var undo = [];
    undo.push(cancelBarrage(counter));
    undo.push(original.MoveOp(data));
    if (DGfrom.length > 0 && DGto.length === 0) {
        undo.push(PlaceOp({
            _id: 'DG_'+data._id,
            name: DGfrom[0].name,
            category: DGfrom[0].category,
            hexid: data.to
        }));
    }
    if (from.stack && from.stack.length === DGfrom.length) {
        DGfrom.forEach(function(it) {
            undo.push(RemovePieceOp({counterId: it.id}));
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
