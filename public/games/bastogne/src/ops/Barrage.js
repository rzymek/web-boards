function getBarrageResult(counter, targetHex, dice) {
    var roll = getArtyRollInfo(counter, targetHex, dice);
    if (dice[0] <= roll.dg) {
        if (dice[1] >= roll.kill) {
            return 'DG+';
        } else {
            return 'DG';
        }
    } else {
        return '';
    }
}

Barrage = function(data) {
    if (data.server)
        return;
    var targetHex = byId(data.targetHex);

    var barrage = targetHex.barrage;

    var counter = _.values(barrage.from)[0];
    var undo = cancelBarrage(counter);

    var dice = data.result.dice;
    var result = getBarrageResult(counter, targetHex, dice);
    var boom = sprites.boom.cloneNode(true);
    boom.id = data._id;
    console.log(result, dice);
    setSpriteTexts(boom,
            dice[0] + (result === 'DG+' ? ',' + dice[1] : ''),
            result
            );
    copyTransformation(targetHex, boom);
    byId('overlays').appendChild(boom);
    boom.style.pointerEvents = 'auto';
    boom.onclick = function() {
        Operations.insert({
            op: 'RemoveElementOp',
            element: boom.id
        });
        if (result.startsWith('DG')) {
            Operations.insert({
                op: 'PlaceOp',
                category: 'Misc Counters',
                name: 'US DG',
                hexid: targetHex.id
            });
        }
    };
    return function() {
        undo();
        boom.remove();
    };
};