function getOddsText(odds) {
    return odds.getElementsByTagNameNS(SVGNS, 'text')[0];
}
function removeAttack(sourceHex, targetHex) {
    delete targetHex.attacking[sourceHex.id];
    var remove = toArray(byId('overlays').children).filter(function(element) {
        return element.from && element.to;
    }).filter(function(element) {
        return (element.from.id === sourceHex.id && element.to.id === targetHex.id);
    });
    remove.forEach(function(it) {
        it.remove();
    });
    if (targetHex.attacking.length === 0) {
        delete targetHex.attacking;
    }
}
function setAttack(sourceHex, targetHex) {
    targetHex.attacking[sourceHex.id] = sourceHex; 
    var arrow = sprites.attackArrow.cloneNode(true);
    arrow.from = sourceHex;
    arrow.to = targetHex;
    placeArrow(arrow, sourceHex, targetHex, 'overlays');
}
function setOdds(targetHex, value) {
    if (!targetHex.odds) {
        targetHex.odds = sprites.target.cloneNode(true);
        copyTransformation(targetHex, targetHex.odds);
        byId('overlays').appendChild(targetHex.odds);
    }
    getOddsText(targetHex.odds).textContent = value;
}
function removeOdds(targetHex) {
    if(targetHex.odds) {
        targetHex.odds.remove();
        delete targetHex.odds;
    }
}
JoinAttack = function(data) {
    function sum(a, b) {
        return (a || 0) + b;
    }

    var targetHex = byId(data.targetHex);
    var sourceHex = byId(data.sourceHex);
    if (!targetHex.attacking) {
        targetHex.attacking = {};
    }
    var undo;
    if (sourceHex.id in targetHex.attacking) {
        removeAttack(sourceHex, targetHex);
        undo = setAttack;
    } else {
        setAttack(sourceHex, targetHex);
        undo = removeAttack;
    }

    var defence = targetHex.stack.map(function(unit) {
        return getUnitInfo(unit).defence;
    }).reduce(sum, 0);

    var attack = _.values(targetHex.attacking).map(function(hex) {
        return hex.stack.map(function(unit) {
            return getUnitInfo(unit).attack;
        }).reduce(sum, 0); 
    }).reduce(sum, 0);
    var initialOdds = targetHex.odds ? getOddsText(targetHex.odds).textContent : null;
    if (attack > 0) {
        setOdds(targetHex, attack + ':' + defence);
    } else {
        removeOdds(targetHex);
    }
    return function() {
        if (initialOdds === null) {
            removeOdds(targetHex);
        } else {
            setOdds(targetHex, initialOdds);
        }        
        undo(sourceHex, targetHex);
    };
}
