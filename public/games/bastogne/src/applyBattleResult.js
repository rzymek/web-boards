var placeAutoRemoveBoom = function(target, arg1, arg2) {
    var pickStep = setSpriteTexts(placeSprite(sprites.boom, target), arg1, arg2);
    pickStep.id = 'autoremoveboom_'+target.id;
    pickStep.onclick = function() {
        Operations.insert({
            op: 'RemoveElementOp',
            element: pickStep.id
        });
    };
    return function() {
        pickStep.remove();
    };
};

var applyBattleResult = function(target, killSteps) {
    var counters = _.chain(target.stack).filter(function(it) {
        return !isDGMarker(it);
    }).value();
    var steps = counters.map(getStepsLeft).reduce(function(a, b) {
        return a + b;
    }, 0);
    if (steps <= killSteps) {
        var undo = counters.map(function(counter) {
            return RemovePieceOp({
                counterId: counter.id
            });
        });
        return function() {
            undo.forEach(function(f) {
                f();
            });
        };
    } else if (counters.length === 1) {
        var counter = _.first(counters);
        if (getStepsLeft(counter) > 1 && killSteps === 1) {
            return FlipOp({
                counterId: counter.id
            });
        }
    } else {
        return placeAutoRemoveBoom(target, 'pick', killSteps);
    }
    return function() {
    };
};