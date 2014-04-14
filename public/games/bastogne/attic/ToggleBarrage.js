function abortBarrage(counter) {
    var targetHex = counter.barrageSource.target;
    targetHex.barrageTarget.arrows[counter.id].remove();
    delete targetHex.barrageTarget.arrows[counter.id];
    delete targetHex.barrageTarget.from[counter.id];
    delete counter.barrageSource;
}

function toggle(targetHex, counter) {
    if (counter.id in targetHex.barrageTarget.from) {
        abortBarrage(counter);
        return targetHex;
    } else {
        if (counter.barrageSource) {
            var prevTarget = counter.barrageSource.target;
            abortBarrage(counter);
        }
        counter.barrageSource = {
            target: targetHex
        };
        var arrow = sprites.attackArrow.cloneNode(true);
        arrow.id = 'barrage_' + targetHex.id + "_" + counter.id;
        placeArrow(arrow, counter.position, targetHex, 'overlays');

        targetHex.barrageTarget.from[counter.id] = counter;
        targetHex.barrageTarget.arrows[counter.id] = arrow;
        return prevTarget;
    }
}

ToggleBarrage = function(data) {
    var targetHex = byId(data.targetHex);
    var counter = byId(data.counterId);

    if (!targetHex.barrageTarget) {
        targetHex.barrageTarget = {
            from: {},
            arrows: {}
        };
    }
    var prev = toggle(targetHex, counter);
    return function() {
        if(prev)
            toggle(prev, counter);
        else
            abortBarrage(counter);
    };
};