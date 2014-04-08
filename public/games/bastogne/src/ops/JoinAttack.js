function getOddsText(odds) {
    return odds.getElementsByTagNameNS(SVGNS, 'text')[0];
}
function removeAttack(sourceHex, targetHex) {
    targetHex.attack.arrows[sourceHex.id].remove();

    delete targetHex.attack.from[sourceHex.id];
    delete targetHex.attack.arrows[sourceHex.id];
}

function setAttack(sourceHex, targetHex) {
    var arrow = sprites.attackArrow.cloneNode(true);
    arrow.id = 'arrow_' + targetHex.id + "_" + sourceHex.id;
    arrow.from = sourceHex;
    arrow.to = targetHex;
    placeArrow(arrow, sourceHex, targetHex, 'overlays');

    targetHex.attack.from[sourceHex.id] = sourceHex;
    targetHex.attack.arrows[sourceHex.id] = arrow;
}

function setOdds(targetHex, value) {
    if (!targetHex.attack.odds) {
        var odds = sprites.target.cloneNode(true);
        odds.id = targetHex.id + "_odds";
        copyTransformation(targetHex, odds);
        byId('overlays').appendChild(odds);
        targetHex.attack.odds = odds;
    }
    targetHex.attack.oddsValue = value;
    getOddsText(targetHex.attack.odds).textContent = value;

    targetHex.attack.odds.style.pointerEvents = 'auto';
    targetHex.attack.odds.onclick = function() {
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

}

function removeOdds(targetHex) {
    targetHex.attack.odds.remove();
    delete targetHex.attack;
}

JoinAttack = function(data) {
    function sum(a, b) {
        return (a || 0) + b;
    }
    function init(targetHex) {
        if (!targetHex.attack) {
            targetHex.attack = {
                arrows: {}, //source hex id -> arrow element
                from: {}, //source hex id -> hex element
                odds: null, //element
                oddsValue: ''
            };
        }
    }

    var targetHex = byId(data.targetHex);
    var sourceHex = byId(data.sourceHex);
    init(targetHex);

    var undo;
    if (sourceHex.id in targetHex.attack.from) {
        removeAttack(sourceHex, targetHex);
        undo = setAttack;
    } else {
        setAttack(sourceHex, targetHex);
        undo = removeAttack;
    }

    var defenceMod = getHexInfo(targetHex.id).defenceMod;
    var defence = targetHex.stack.map(function(unit) {
        return getUnitInfo(unit).defence;
    }).reduce(sum, 0) * defenceMod;
    var attack = _.values(targetHex.attack.from).map(function(hex) {
        return hex.stack.map(function(unit) {
            return getUnitInfo(unit).attack;
        }).reduce(sum, 0);
    }).reduce(sum, 0);

    var initialOdds = targetHex.attack.oddsValue;
    if (Object.keys(targetHex.attack.from).length > 0) {
        setOdds(targetHex, attack + ':' + defence);
        targetHex.attack.odds.children[0].style.stroke = defenceMod > 1 ? 'black' : 'none';
    } else {
        removeOdds(targetHex);
    }

    return function() {
        init(targetHex);
        undo(sourceHex, targetHex);
        if (initialOdds === '') {
            removeOdds(targetHex);
        } else {
            setOdds(targetHex, initialOdds);
        }
    };
};

