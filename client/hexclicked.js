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
    var selectedId = Session.get('selectedPiece');
    if (selectedId === null) {
        //selecting a piece
        var stack = use.stack;
        if (stack === undefined || stack === null)
            return;
        if (stack.length > 1) {
            showStackSelector(use, stack);
        } else if (stack.length === 1) {
            Session.set('selectedPiece', stack[0].id);
        }
        return;
    }else{
        //clicked on a selected piece -> deselect
        var img = byId(selectedId);
        if(img.position && img.position.id === use.id){
            Session.set('selectedPiece', null);
            return;
        }        
    }

    if (selectedId) {
        var data = null;
        var img = byId(selectedId);
        if (isOnBoard(img)) {
            data = {op: 'MoveOp', counter: img.id, to: use.id};
        } else {         
            data = {
                op: 'PlaceOp',
                imageBase: '/games/' + Session.get('selectedGame') + '/images/',
                sides: img.getAttribute('sides').split('|'),
                size: getNatural(img),
                hexid: use.id
            };
        }
        Operations.insert(data);
    }
};
