function getBarrageResult(counter, data) {
    console.log(data);
    var info = getUnitInfo(counter);
    var dice = data.result.dice;
    if(dice[0] <= data.dgRoll) {
        if(dice[1] >= getKillRoll(info)) {
            return 'DG+';
        }else{
            return 'DG';
        }
    }else{
        return '';
    }
}

Barrage = function(data) {
    if(data.server)
        return;
    var targetHex = byId(data.targetHex);
    var counter = byId(data.counterId);

    targetHex.barrage.arrow.remove();
    targetHex.barrage.target.remove();
    
    var dice = data.result.dice;
    var result = getBarrageResult(counter, data);
    var boom = sprites.boom.cloneNode(true);
    boom.id = data._id;
    var tspan = boom.getElementsByTagNameNS(SVGNS, 'tspan');
    tspan[1].textContent = result;
    tspan[0].textContent = dice[0] + (result === 'DG+' ? ','+dice[1] : '');
    copyTransformation(targetHex, boom);
    byId('overlays').appendChild(boom);
    boom.style.pointerEvents = 'auto';
    boom.onclick = function() {
        Operations.insert({
            op: 'RemoveElementOp',
            element: boom.id
        });
    };
    return function() {

    };
};