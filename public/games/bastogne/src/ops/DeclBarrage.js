var clearBarrage = function(barrage) {
    if (!barrage || !barrage.ui)
        return;
    console.log('clearBarrage', barrage.ui);
    barrage.ui.target.remove();
    barrage.ui.arrows.forEach(function(arrow) {
        arrow.remove();
    });
    delete barrage.ui;
};
var drawBarrage = function(barrage) {
    barrage.ui = {
        arrows: _.values(barrage.from).map(function(counter) {
            var arrow = sprites.attackArrow.cloneNode(true);
            return placeArrow(arrow, counter.position, barrage.target, 'overlays');
        }) 
    };
    barrage.ui.target = placeSprite(sprites.target, barrage.target);
    barrage.ui.target.style.pointerEvents = 'none';
};

var cancelBarrage = function(targetHex, counter) {
    console.log('cancelBarrage', targetHex, counter);
    var barrage = targetHex.barrage;
    clearBarrage(barrage);
    delete barrage.from[counter.id];
    delete counter.barrage;
    if (Object.keys(barrage.from) === 0)
        delete targetHex.barrage;
    else
        drawBarrage(barrage);
    return function() {
        declareBarrage(targetHex, counter);
    };
};

var declareBarrage = function(targetHex, counter) {
    console.log('declareBarrage', targetHex, counter);
    var barrage = targetHex.barrage;
    clearBarrage(barrage);
    if (counter.barrage) {
        var undo = cancelBarrage(counter.barrage.target, counter);
    }
    barrage.from[counter.id] = counter;
    counter.barrage = barrage;
    targetHex.barrage = barrage;
    drawBarrage(barrage);
    return function() {
        if (undo)
            undo();
        cancelBarrage(targetHex, counter);
    };
};

DeclBarrage = function(data) {
    var targetHex = byId(data.targetHex);
    var counter = byId(data.counterId);

    var barrage = targetHex.barrage;
    if (!barrage) {
        barrage = {
            target: targetHex,
            from: {}
        };
        targetHex.barrage = barrage;
    }
    var undo;
    if (counter.id in barrage.from) {
        undo = cancelBarrage(targetHex, counter);
    } else {
        undo = declareBarrage(targetHex, counter);
    }
    return undo;
};