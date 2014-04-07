Attack = function(data) {
    if(data.server)
        return;
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

    //TODO: roll the dice, display results
    return function() {
        undo();
        hex.odds = backup.odds;
        hex.attackArrows = backup.arrows;
    };
};