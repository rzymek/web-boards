JoinAttack = function(data) {
    function sum(a, b) {
        return (a || 0) + b;
    }

    var targetHex = byId(data.targetHex);
    var sourceHex = byId(data.sourceHex);
    if (!targetHex.attacking) {
        targetHex.attacking = {};
    }
    if (sourceHex.id in targetHex.attacking) {
        delete targetHex.attacking[sourceHex.id];
        $(byId('overlays')).children().filter(function(element) {
            return element.from && element.to;
        }).each(function(element) {
            if (element.from.id === sourceHex.id && element.to.id === targetHex.id) {
                element.remove();
            }
        });
    } else {
        targetHex.attacking[sourceHex.id] = sourceHex;
    }

    var defence = targetHex.stack.map(function(unit) {
        return getUnitInfo(unit).defence;
    }).reduce(sum);

    var attack = _.values(targetHex.attacking).map(function(hex) {
        return hex.stack.map(function(unit) {
            return getUnitInfo(unit).attack;
        }).reduce(sum);
    }).reduce(sum);

    DeclareAttack({
        attackerHex: sourceHex.id,
        targetHex: targetHex.id,
        value: attack + ':' + defence
    });
    return function() {

    };
}
