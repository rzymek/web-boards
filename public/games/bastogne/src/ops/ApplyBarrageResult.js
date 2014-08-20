ApplyBarrageResult = function(data) {
    var target = byId(data.targetHex);
    var undo = [];
    undo.push(RemoveElementOp(data));
    if (data.stepLoss) {
        undo.push(applyBattleResult(target, 1));
    }
    if (!isDGHex(target) && (target.stack || []).length > 0) {
        var targetSide = _.chain(target.stack).map(getOwner).filter(notNull).first().value();
        undo.push(PlaceOp({
            _id: 'DG_'+data._id,
            name: (targetSide === 'US' ? 'US' : 'German') + ' DG',
            category: 'Misc Counters',
            hexid: target.id
        }));
    }
    return function() {
        undo.forEach(function(f) {
            f();
        });
    };
};