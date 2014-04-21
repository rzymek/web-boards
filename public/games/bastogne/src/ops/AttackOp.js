Attack = function(data) {
    console.log('attack', data.server, data.result);
    if (data.server) {
        return;
    }
    var hex = byId(data.target);
    var initialAttack = hex.attack;

    var combatResult = getCombatResult(data.result.roll, hex.attack.oddsValue);
    var boom = sprites.boom.cloneNode(true);
    boom.id = data._id;
    var tspan = boom.getElementsByTagNameNS(SVGNS, 'tspan');
    tspan[1].textContent = combatResult;
    tspan[0].textContent = data.result.roll;
    boom.style.pointerEvents = 'auto';
    boom.onclick = function() {
        if (getSelectedId() !== null) {
            $(hex).click();
            return;
        }
        if (combatResult.match(/^D[1-9]/)) {
            Operations.insert({
                op: 'ApplyAttackResult',
                targetHex: data.target,
                element: boom.id,
                combatResult: combatResult
            });
        }else{
            Operations.insert({
                op: 'RemoveElementOp',
                element: boom.id
            });
        }
    };
    copyTransformation(hex, boom);
    byId('overlays').appendChild(boom);

    var undo = RemoveElementOp({
        elements: _.values(hex.attack.arrows).map(function(e) {
            return e.id;
        }).concat(hex.attack.odds.id)
    });
    delete hex.attack;
    return function() {
        undo();
        hex.attack = initialAttack;
        boom.remove();
    };
};