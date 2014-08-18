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

var getAttackTarget = function(hex) {
    if (hex.attack) {
        return hex;
    } else if (hex.attackSource) {
        return hex.attackSource.target;
    } else {
        return null;
    }
};

var abandonAttack = function(hex) {
    if (!hex || !hex.attack) {
        return function() {
        };
    }
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
};