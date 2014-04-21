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
        if (getSelectedId() !== null) {
            $(barrage.target).click();
            return;
        }
        var dg = result.match(/^DG/);
        var stepLoss = (result === 'DG+');
        if (dg) {
            Operations.insert({
                op: 'ApplyBarrageResult',
                targetHex: targetHex.id,
                stepLoss: stepLoss,
                element: boom.id
            });
        } else {
            Operations.insert({
                op: 'RemoveElementOp',
                element: boom.id
            });
        }
    };
    return function() {
        undo();
        boom.remove();
    };
};