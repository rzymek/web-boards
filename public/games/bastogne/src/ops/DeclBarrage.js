var abbortBarrage1 = function(barrage) {
    if (!barrage)
        return function() {
        };
    var undoData = _.clone(barrage);
    clearBarrage(barrage);
    delete barrage.target;
    _.values(barrage.from).forEach(function(counter) {
        delete counter.barrage;
    });
    delete barrage.from;
    return function() {
        undoData.target.barrage = undoData;
        _.values(undoData.from).forEach(function(counter) {
            counter.barrage = undoData;
        });
        drawBarrage(undoData);
    };
};

var clearBarrage = function(barrage) {
    if (!barrage || !barrage.ui)
        return;
    barrage.ui.target.remove();
    barrage.ui.arrows.forEach(function(arrow) {
        arrow.remove();
    });
    barrage.ui.sourceMarkers.forEach(function(marker) {
        marker.remove();
    });
    delete barrage.ui;
};


var drawBarrage = function(barrage) {
    var count = Object.keys(barrage.from).length;
    if (count === 0) {
        return;
    }
    barrage.ui = {
        arrows: _.values(barrage.from).map(function(counter) {
            var arrow = sprites.attackArrow.cloneNode(true);
            return placeArrow(arrow, counter.position, barrage.target, 'overlays');
        })
    };
    barrage.ui.target = placeSprite(sprites.target, barrage.target);
    barrage.ui.target.style.pointerEvents = 'none';
    setSpriteTexts(barrage.ui.target, count);


    barrage.ui.sourceMarkers = _.values(barrage.from).map(function(counter) {
        var marker = sprites.declareBarrage.cloneNode(true);
        marker.transform.baseVal.clear();
        marker.style.pointerEvents = 'none';
        var info = getUnitInfo(counter);
        var dgRollModifier = getHexInfo(barrage.target.id).barrage;
        var killRoll = getKillRoll(info);
        var dgRoll = info.attack - dgRollModifier;
        setSpriteTexts(marker,
                '\u2264'/* <= */ + dgRoll,
                '\u2265'/* >= */ + killRoll);
        counter.appendChild(marker);
        return marker;
    });
};

var cancelBarrage = function(counter) {
    var barrage = counter.barrage;
    if (!barrage) {
        return function() {
        };
    }
    clearBarrage(barrage);
    delete barrage.from[counter.id];
    delete counter.barrage;
    if (Object.keys(barrage.from) === 0)
        delete barrage.target.barrage;
    else
        drawBarrage(barrage);
    return function() {
        declareBarrage(barrage.target, counter);
    };
};

var declareBarrage = function(targetHex, counter) {
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
        undo = cancelBarrage(counter);
    } else {
        undo = declareBarrage(targetHex, counter);
    }
    return undo;
};