/*

var updateTargetDisplay = function(targetHex) {
    var initialCount = Object.keys(targetHex.barrage.from);
    var count = Object.keys(targetHex.barrage.from).length;
    console.log('updateTargetDisplay', count, _.values(targetHex.barrage.from));
    if (count > 0) {
        setSpriteTexts(targetHex.barrage.display, count);
        return function() {
            setSpriteTexts(targetHex.barrage.display, initialCount);
        };
    } else {
        var initial = targetHex.barrage;
        targetHex.barrage.from.remove();
        delete targetHex.barrage;
        return function() {
            targetHex.barrage = initial;
            byId('overlays').appendChild(targetHex.barrage.from);
        };
    }
};

DeclareBarrage = function(data) {
    var targetHex = byId(data.targetHex);
    var counter = byId(data.counterId);

    if (!targetHex.barrageTarget) {
        targetHex.barrage = {
            from: {},
            display: null
        };
    }
    counter.barrage = {
        arrow: null,
        display: null
    };
    counter.barrage.arrow = sprites.attackArrow.cloneNode(true);
    counter.barrage.arrow.id = 'barrage_' + targetHex.id + "_" + counter.id;
    placeArrow(counter.barrage.arrow, counter.position, targetHex, 'overlays');

    var info = getUnitInfo(counter);
    var dgRollModifier = getHexInfo(targetHex.id).barrage;
    var killRoll = getKillRoll(info);
    var dgRoll = info.attack - dgRollModifier;
    counter.barrage.display = placeSprite(sprites.declareBarrage, counter.position);
    counter.barrage.display.style.pointerEvents = 'none';
    setSpriteTexts(counter.barrage.display,
//            '\u2264'/* <=  + dgRoll,
//            '\u2265'/* >=  + killRoll);    
    
    targetHex.barrage.display = placeSprite(sprites.target, targetHex);
    targetHex.barrage.from[counter.id] = counter;
    updateTargetDisplay(targetHex);
    targetHex.barrage.display.id = targetHex.id + '_barrage';
    targetHex.barrage.display.onclick = function() {
        if (getSelectedId() !== null && isArty(byId(getSelectedId()))) {
            $(targetHex).click();
            return;
        }
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


    return function() {
    };
};
        */