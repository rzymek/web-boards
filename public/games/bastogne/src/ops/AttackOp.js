Attack = function(data) {
    console.log('attack', data.server, data.result);
    if (data.server) {
        return;
    }
    var hex = byId(data.target);
    var initialAttack = hex.attack;

    var undo = RemoveElementOp({
        elements: _.values(hex.attack.arrows).map(function(e) {
            return e.id;
        }).concat(hex.attack.odds.id)
    });
    delete hex.attack;

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

    //TODO: CRT
    return function() {
        undo();
        hex.attack = initialAttack;
        boom.remove();
    };
};