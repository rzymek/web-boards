UndeclareAttack = function(data) {
    var undoData = [];
    data.targetHexes.map(function(id) {
        return byId(id);
    }).forEach(function(targetHex) {
        undoData.push([targetHex, targetHex.attack]);
        console.log('targetHex.attack', targetHex.attack);
        _.values(targetHex.attack.arrows).forEach(function(arrow) {
            arrow.remove();
        });
        targetHex.attack.odds.remove();
        delete targetHex.attack;
    });
    return function() {
        undoData.forEach(function(undo) {
            var targetHex = undo.targetHex;
            targetHex.attack = undo.attack;
            with (byId('overlays')) {
                _.values(targetHex.attack).forEach(function(arrow) {
                    appendChild(arrow);
                });
                appendChild(targetHex.odds);
            }
        });
    };
};