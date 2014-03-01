
function showingPieceMenu() {
    return byId('pieceMenuLayer').childElementCount !== 0;
}

function hidePieceMenu() {
    $('#pieceMenuLayer').empty();
}

function showPieceMenu(img) {
    var menuItem = sprites.menuItem;
    var layer = byId('pieceMenuLayer');
    var dy = 0;
    for (var entry in pieceMenu) {
        var x = img.position.rx + img.width.baseVal.value / 2;
        var y = img.position.ry - img.height.baseVal.value / 2 + dy;
        var item = menuItem.cloneNode(true);
        item.removeAttribute('id');
        item.setAttribute('transform', 'translate(' + x + ' ' + y + ')');
        item.style.visibility = 'visible';

        $(item).find('tspan').text(entry);
        item.onclick = (function(name) {
            return function() {
                pieceMenu[name](img);
            };
        })(entry);

        layer.appendChild(item);
        dy += item.getBBox().height;
    }
}

function showMenu(hex) {
    var menuItem = sprites.menuItem;
    var layer = byId('pieceMenuLayer');
    var dy = 0;
    for (var entry in menu) {
        var x = hex.rx;
        var y = hex.ry + dy;
        var item = menuItem.cloneNode(true);
        item.removeAttribute('id');
        item.setAttribute('transform', 'translate(' + x + ' ' + y + ')');
        item.style.visibility = 'visible';

        $(item).find('tspan').text(entry);
        item.onclick = (function(name) {
            return function() {
                menu[name]();
            };
        })(entry);

        layer.appendChild(item);
        dy += item.getBBox().height;
    }
}

hexClicked = function(e) {
    if(byId('svg').panning)
        return;
    var selector = sprites.stackSelector;
    selector.style.visibility = 'hidden';
    
    if (selector.atHex) {
        selector.stack.forEach(function(it) {
            it.style.pointerEvents = '';
            it.onclick = undefined;
        });
        alignStack(selector.atHex);
    }

    var use = e.currentTarget;
    var selectedId = getSelectedId();
    if (selectedId === null) {
        if(showingPieceMenu()) {
            hidePieceMenu();
            return;
        }
        //selecting a piece
        var stack = use.stack;
        if (stack === undefined || stack === null || stack.length === 0) {
            showMenu(use);
            return;
        }
        if (stack.length > 1) {
            showStackSelector(use, stack);
        } else if (stack.length === 1) {
            selectById(stack[0].id);
            showPieceMenu(byId(stack[0].id));
        }
        return;
    } else {
        var img = byId(selectedId);
        if (img.position && img.position.id === use.id) {
            //clicked on a selected piece -> deselect
            if (showingPieceMenu()) {
                hidePieceMenu();
                selectById(null);
            } else {
                hidePieceMenu();
                showPieceMenu(img);
            }
            return;
        }
    }

    if (selectedId) {
        hidePieceMenu();

        var data = null;
        var img = byId(selectedId);
        if (isOnBoard(img)) {
            data = {op: 'MoveOp', counter: img.id, to: use.id};
        } else {
            if(img.id === 'special-d6') {
                data = {
                    op: 'RollOp',
                    size: getNatural(img),
                    hexid: use.id,
                    server: {
                        roll: '1d6'
                    }
                };
            }else{ 
                data = {
                    op: 'PlaceOp',
                    imageBase: '/games/' + Session.get('selectedGame') + '/images/',
                    sides: img.getAttribute('sides').split('|'),
                    size: getNatural(img),
                    hexid: use.id
                };
            }
        }
        Operations.insert(data);
    }
};
