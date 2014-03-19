
showingPieceMenu = function() {
    return byId('pieceMenuLayer').childElementCount !== 0;
};

hidePieceMenu = function() {
    $('#pieceMenuLayer').empty();
};

showPieceMenu = function(counter) {
    var menuItem = sprites.menuItem;
    var layer = byId('pieceMenuLayer');
    var dy = 0;
    var bbox = counter.getBBox();
    for (var entry in pieceMenu) {
        var x = counter.position.rx + bbox.width / 2;
        var y = counter.position.ry - bbox.height / 2 + dy;
        var item = menuItem.cloneNode(true);
        item.removeAttribute('id');
        item.setAttribute('transform', 'translate(' + x + ' ' + y + ')');
        item.style.visibility = 'visible';

        $(item).find('tspan').text(entry);
        item.onclick = (function(name) {
            return function() {
                pieceMenu[name](counter);
            };
        })(entry);

        layer.appendChild(item);
        dy += item.getBBox().height;
    }
};

showMenu = function(hex) {
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
};
