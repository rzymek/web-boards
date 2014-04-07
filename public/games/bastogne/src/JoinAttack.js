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
        delete targetHex.attackArrows[targetHex.attackArrows.indexOf(it.id)];
        it.remove();
    });
    if (targetHex.attacking.length === 0) {
        delete targetHex.attacking;
    }
}
function setAttack(sourceHex, targetHex) {
    targetHex.attacking[sourceHex.id] = sourceHex;
    var arrow = sprites.attackArrow.cloneNode(true);
    arrow.id = 'arrow_' + targetHex.id + "_" + sourceHex.id;
    arrow.from = sourceHex;
    arrow.to = targetHex;
    placeArrow(arrow, sourceHex, targetHex, 'overlays');
    if (!targetHex.attackArrows) {
        targetHex.attackArrows = [];
    }
    targetHex.attackArrows.push(arrow.id);
}
function setOdds(targetHex, value) {
    if (!targetHex.odds) {
        targetHex.odds = sprites.target.cloneNode(true);
        targetHex.odds.id = targetHex.id + "_odds";
        copyTransformation(targetHex, targetHex.odds);
        byId('overlays').appendChild(targetHex.odds);
    }
    getOddsText(targetHex.odds).textContent = value;
}
function removeOdds(targetHex) {
    if (targetHex.odds) {
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
    var targetHexInfo = getHexInfo(targetHex.id);

    var defence = targetHex.stack.map(function(unit) {
        return getUnitInfo(unit).defence;
    }).reduce(sum, 0) * targetHexInfo.defenceMod;

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
    if (targetHex.odds) {
        targetHex.odds.children[0].style.stroke = targetHexInfo.defenceMod > 1 ? 'black' : 'none';
    }

    targetHex.odds.style.pointerEvents = 'auto';
    targetHex.odds.onclick = function() {
        if (getSelectedId() !== null && isAttack(byId(getSelectedId()), targetHex)) {
            $(targetHex).click();
            return;
        }
        Operations.insert({
            op: 'Attack',
            target: targetHex.id,
            server: {
                roll: {
                    count: 2,
                    sides: 6
                }
            }
        });
    };

    return function() {
        if (initialOdds === null) {
            removeOdds(targetHex);
        } else {
            setOdds(targetHex, initialOdds);
        }
        undo(sourceHex, targetHex);
    };
};

