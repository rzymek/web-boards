function getOddsText(odds) {
    return odds.getElementsByTagNameNS(SVGNS, 'text')[0];
}
DeclareAttack = function(data) {
    var arrow = sprites.attackArrow.cloneNode(true);
    var attacker = byId(data.attackerHex);
    var targetHex = byId(data.targetHex);
    placeArrow(arrow, attacker, targetHex, 'overlays');

    if (!targetHex.odds) {
        targetHex.odds = sprites.target.cloneNode(true);
        copyTransformation(targetHex, targetHex.odds);
        byId('overlays').appendChild(targetHex.odds);
    }
    getOddsText(targetHex.odds).textContent = data.value;
    arrow.from=attacker;
    arrow.to=targetHex;
    return function() {
        arrow.remove();
    };
};


