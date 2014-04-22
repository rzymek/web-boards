gameModule = function() {
    var original = {
        MoveOp: MoveOp,
        moveTo: moveTo
    };
    MoveOp = MoveOpStickyDG.bind(undefined, original);
    moveTo = function(counter, targetHex) {
        if (isAttack(counter, targetHex)) {
            if (isArty(counter)) {
                Operations.insert({
                    op: 'DeclareBarrage',
                    counterId: counter.id,
                    targetHex: targetHex.id
                });
            } else {
                Operations.insert({
                    op: 'DeclareAttack',
                    sourceHex: counter.position.id,
                    targetHex: targetHex.id
                });
            }
        } else {
            original.moveTo(counter, targetHex);
        }
    };

    var deps = [];
//    deps.push(showMovement());
//    deps.push(showRoadMovement());

    return function() {
        deps.forEach(function(c) {
            c.stop();
        });
        MoveOp = original.MoveOp;
        hexClicked = original.hexClicked;
        delete CRT;
    };
};