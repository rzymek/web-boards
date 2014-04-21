ApplyAttackResult = function(data) {
    var target = byId(data.targetHex);
    var undo = [];
    undo.push(RemoveElementOp(data));
    var killSteps = parseInt(data.combatResult[1]);
    if (killSteps > 0) {
        undo.push(applyBattleResult(target, killSteps));
    }
    if(!isEmpty(target) && data.combatResult.match(/..r[1-9]/)) {
        var retreat = data.combatResult[3];
        undo.push(placeAutoRemoveBoom(target, retreat, 'back'));
    }
    return function() {
        undo.forEach(function(f) {
            f();
        });
    };
};