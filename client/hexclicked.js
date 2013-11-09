hexClicked = function(e) {
    var selector = byId('stackSelector');
    selector.style.visibility = 'hidden';
    if (selector.atHex) {
        selector.stack.forEach(function(it) {
            it.style.pointerEvents = '';
            it.onclick = undefined;
        });
        alignStack(selector.atHex);
    }

    var use = e.currentTarget;
    if (ctx.selected === null) {
        var stack = use.stack;
        if (stack === undefined || stack === null)
            return;
        if (stack.length > 1) {
            showStackSelector(use, stack);
        } else if (stack.length === 1) {
            Session.set('selectedPiece', stack[0].id);
        }
        return;
    }
    var s = Session.get('selectedPiece');
    if(s !== null) {        
        if(document.getElementById(s).position.id === use.id){
            Session.set('selectedPiece', null);
            return;
        }
    }

    if (ctx.selected) {
        var data = null;
        if (isOnBoard(ctx.selected.img)) {
            data = {op: 'MoveOp', counter: ctx.selected.img.id, to: use.id};
        } else {
            data = {
                op: 'PlaceOp',
                imageBase: '/games/' + Session.get('selectedGame') + '/images/',
                sides: ctx.selected.img.getAttribute('sides').split('|'),
                hexid: use.id
            };
        }
        Operations.insert(data);
    }
};
