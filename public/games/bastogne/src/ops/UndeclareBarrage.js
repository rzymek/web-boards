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

UndeclareBarrage = function(data) {
    var targetHex = byId(data.targetHex);
    var counter = byId(data.counterId);

    counter.barrage.arrow.remove();
    counter.barrage.display.remove();
    delete targetHex.barrage.from[counter.id];
    var count = Object.keys(targetHex.barrage.from).length;
    if (count === 0) {
        targetHex.barrage.display.remove();
        delete targetHex.barrage;
    } else {
        setSpriteTexts(targetHex.barrage.display, count);
    }
    delete counter.barrage;
    return function() {
    };
};