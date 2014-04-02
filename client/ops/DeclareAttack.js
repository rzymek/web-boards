function getOddsText(odds) {
    return odds.getElementsByTagNameNS(SVGNS, 'text')[0];
}
DeclareAttack = function(data) {
    var arrow = sprites.attackArrow.cloneNode(true);
    var attacker = byId(data.attackerHex);
    var targetHex = byId(data.targetHex);
    placeArrow(arrow, attacker, targetHex);

    if (!targetHex.odds) {
        targetHex.odds = sprites.target.cloneNode(true);
        copyTransformation(targetHex, targetHex.odds);
        byId('traces').appendChild(targetHex.odds);
    }
    getOddsText(targetHex.odds).textContent = data.value;

    return function() {
        arrow.remove();
    };
};


