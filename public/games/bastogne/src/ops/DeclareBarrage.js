function getKillRoll(info) {
    switch (info.artyType) {
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

    var arrow = sprites.attackArrow.cloneNode(true);
    arrow.id = 'barrage_' + targetHex.id + "_" + counter.id;
    placeArrow(arrow, counter.position, targetHex, 'overlays');

    var info = getUnitInfo(counter);
    var mod = getHexInfo(targetHex.id).barrage;
    var killRoll = getKillRoll(info);
    var dgRoll = info.attack - mod;
    
    var target = sprites.target.cloneNode(true);
    target.style.pointerEvents = 'auto';
    target.id = targetHex.id + '_barrage';
    target.onclick = function() {
        Operations.insert({
            op: 'Barrage',
            targetHex: targetHex.id,
            counterId: counter.id,
            dgRoll: dgRoll,
            server: {
                roll: {
                    count: 2,
                    sides: 6
                }
            }
        });
    };
    copyTransformation(targetHex, target);
    byId('overlays').appendChild(target);
    var tspan = target.getElementsByTagNameNS(SVGNS, 'tspan');

    tspan[1].textContent = '>=' + killRoll;
    tspan[0].textContent = '<=' + dgRoll;

    targetHex.barrage = {
        arrow: arrow,
        target: target
    };
    return function() {
        arrow.remove();
        target.remove();
    };
};