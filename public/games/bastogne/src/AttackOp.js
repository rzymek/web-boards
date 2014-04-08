Attack = function(data) {
    console.log('attack', data.server, data.result);
    if (data.server) {
        return;
    }
    var hex = byId(data.target);
    var backup = {
        odds: hex.odds,
        arrows: hex.attackArrows
    };
    var undo = RemoveElementOp({
        elements: hex.attackArrows.concat(hex.odds.id)
    });
    delete hex.odds;
    delete hex.attackArrows;

    var boom = sprites.boom.cloneNode(true);
    boom.id = data._id;
    boom.getElementsByTagNameNS(SVGNS, 'text')[0].textContent = data.result.roll;
    boom.style.pointerEvents = 'auto';
    boom.onclick = function() {
        Operations.insert({
            op: 'RemoveElementOp',
            element: boom.id
        });
    };
    copyTransformation(hex, boom);
    byId('overlays').appendChild(boom);

    //TODO: roll the dice, display results
    return function() {
        undo();
        hex.odds = backup.odds;
        hex.attackArrows = backup.arrows;
        boom.remove();
    };
};