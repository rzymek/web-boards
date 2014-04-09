function getToRoll(counter) {
    switch (getUnitInfo(counter).artyType) {
        case ArtyType.GUNS_88:
            return 5;
        case ArtyType.OTHER:
            return 6;
        case ArtyType.YELLOW:
            return 4;
    }
}

DeclareBarrage = function(data) {
    var targetHex = byId(data.targetHex);
    var counter = byId(data.counterId);

    console.log('barrage');

    var arrow = sprites.attackArrow.cloneNode(true);
    arrow.id = 'barrage_' + targetHex.id + "_" + counter.id;
    placeArrow(arrow, counter.position, targetHex, 'overlays');

    var toRoll = sprites.target.cloneNode(true);
    toRoll.id = targetHex.id + '_barrage';
    copyTransformation(targetHex, toRoll);
    byId('overlays').appendChild(toRoll);
    var tspan = toRoll.getElementsByTagNameNS(SVGNS, 'tspan');

    var mod = getHexInfo(targetHex.id).barrage;
    tspan[0].textContent = getToRoll(counter) + mod;
    tspan[1].textContent = mod;

    return function() {
        //TODO
    };
};