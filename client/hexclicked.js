pieceMenu = {
    'Flip': function(piece) {
        Operations.insert({
            op: 'FlipOp',
            counterId: piece.id,
        });
    },
    'Mark': function(piece) {
        console.log('mark', piece);
    }
};

function showPieceMenu(img) {
    var menuItem = byId('menuItem');
    var x = img.position.rx + img.width.baseVal.value / 2;
    var y = img.position.ry - img.height.baseVal.value / 2;
    menuItem.setAttribute('transform', 'translate(' + x + ' ' + y + ') scale('+(1/getBoardScaling())+')');
    menuItem.style.visibility = 'visible';
    byId('pieceMenuLayer').appendChild(menuItem);
}

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
    } else {
        var img = byId(selectedId);
        if (img.position && img.position.id === use.id) {
            //clicked on a selected piece -> deselect
            if (byId('menuItem').style.visibility === 'visible') {
                byId('menuItem').style.visibility = 'hidden';
                Session.set('selectedPiece', null);
            } else {
                showPieceMenu(img);
            }
            return;
        }
    }

    if (selectedId) {
        byId('menuItem').style.visibility = 'hidden';
        
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
