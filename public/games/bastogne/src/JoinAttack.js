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
        toArray(byId('overlays').children).filter(function(element) {
            return element.from && element.to;
        }).forEach(function(element) {
            if (element.from.id === sourceHex.id && element.to.id === targetHex.id) {
                element.remove();
            }
        });
    } else {
        targetHex.attacking[sourceHex.id] = sourceHex;
        var arrow = sprites.attackArrow.cloneNode(true);
        arrow.from = sourceHex;
        arrow.to = targetHex;
        placeArrow(arrow, sourceHex, targetHex, 'overlays');
    }

    var defence = targetHex.stack.map(function(unit) {
        return getUnitInfo(unit).defence;
    }).reduce(sum, 0);

    var attack = _.values(targetHex.attacking).map(function(hex) {
        return hex.stack.map(function(unit) {
            return getUnitInfo(unit).attack;
        }).reduce(sum, 0);
    }).reduce(sum, 0);
    if (attack > 0) {
        ShowOdds({
            targetHex: targetHex.id,
            value: attack + ':' + defence
        });
    }else{
        targetHex.odds.remove();
        delete targetHex.odds;
    }
    return function() {

    };
}
