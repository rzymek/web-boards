function getOddsText(odds) {
    return odds.getElementsByTagNameNS(SVGNS, 'text')[0];
}
ShowOdds= function(data) {
    var targetHex = byId(data.targetHex);

    if (!targetHex.odds) {
        targetHex.odds = sprites.target.cloneNode(true);
        copyTransformation(targetHex, targetHex.odds);
        byId('overlays').appendChild(targetHex.odds);
    }
    getOddsText(targetHex.odds).textContent = data.value;
    return function() {
        //TODO
    };
};


