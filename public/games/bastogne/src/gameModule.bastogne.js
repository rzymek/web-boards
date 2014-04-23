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
    if(Session.get('config').indexOf('SM') >= 0)
        deps.push(showMovement());
    if(Session.get('config').indexOf('RM') >= 0)
        deps.push(showRoadMovement());

    return function() {
        console.log('unloading bastogne', original);
        deps.forEach(function(c) {
            c.stop();
        });
        MoveOp = original.MoveOp;
        moveTo = original.moveTo;
        delete CRT;
    };
};