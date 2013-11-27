if (!isTouchDevice()) {
    Template.play.svgWidth = '100%';
    Template.play.svgHeight = '100%';
} else {
    Template.play.svgWidth = function() {
        return Template.play.board().w;
    };
    Template.play.svgHeight = function() {
        return Template.play.board().h;
    };
}

function setupGrid(svg) {
    var svgns = "http://www.w3.org/2000/svg";
    var boardScale = 1;
    var layer = svg.getElementById('hexes');
    if (layer.childNodes.length > 0) {
        return svg; //already setup
    }
    var board = Session.get('gameInfo').board;
    var scaling = getBoardScaling();
    var hex2hex = {
        x: board.grid.hexWidth * boardScale,
        y: board.grid.hexSize * boardScale
    };

    var A = 2 * board.grid.hexSize / Math.sqrt(3);
    var use;
    var yn = board.height / hex2hex.y;
    var xn = board.width / hex2hex.x;
    for (var y = 0; y < yn; y++) {
        for (var x = 0; x < xn; x++) {
            var hx = (board.grid.originX * boardScale + x * hex2hex.x);
            var hy = (board.grid.originY * boardScale + y * hex2hex.y);
            var hscale = A / 100 * boardScale;
            if (x % 2) {
                hy -= hex2hex.y / 2;
            }
            hx /= scaling;
            hy /= scaling;
            hscale /= scaling;
            use = document.createElementNS(svgns, 'use');
            use.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", '#hex');
            use.setAttribute('transform', 'translate(' + hx + ' ' + hy + ') scale(' + hscale + ')');
            use.setAttribute('class', 'hex');
            use.id = 'h' + x + '_' + y;
            use.onclick = hexClicked;
            use.rx = hx;
            use.ry = hy;
            layer.appendChild(use);
        }
    }
    return svg;
}

Deps.autorun(function() {
    console.log('board scaling: ' + getBoardScaling());
});


Template.play.board = function() {
    var gameInfo = Session.get('gameInfo');
    var scaling = getBoardScaling();
    return {
        w: gameInfo.board.width / scaling,
        h: gameInfo.board.height / scaling
    };
};
Template.play.boardImg = function() {
    var selected = Session.get('selectedGame');
    var info = Session.get('gameInfo');
    if (selected && info.board.grid)
        return '/games/' + selected + '/images/' + info.board.image;
    else
        return '/img/loading.gif';
};
Template.status.status = function() {
    return Meteor.status();
};

sprites = null;
Template.play.rendered = function() {
    var gameInfo = Session.get('gameInfo');
    if (gameInfo.board.grid === null) {
        return;
    }
    var svg = document.getElementById('svg');
    setupGrid(svg);
    if (!isTouchDevice()) {
        svgZoomAndPan(svg);
        $('#menu').addClass('touch');
    } else {
        $('#menu').addClass('mouse');
    }

    Meteor.Keybindings.removeAll();
    Meteor.Keybindings.add({
        '←': menu.Back,
        '→': menu.Fwd,
        'ctrl+z': menu.Undo,
        'ctrl+shift+z': function() {
            alert('redo not implemented yet');
        },
        'del': menu.Remove,
        'F': menu.Flip
    });

    $.get('/sprites.svg', function(data) {
        var dest = svg.getElementsByTagName('defs')[0];
        var defs = data.getElementsByTagName('defs')[0].childNodes;
        for (var i = 0; i < defs.length; i++) {
            if (defs[i].nodeType !== Element.ELEMENT_NODE)
                continue;
            dest.appendChild(defs[i]);
        }
        var tmpl = data.getElementById('tmpl');
        tmpl.style.display = 'none';
        svg.appendChild(tmpl);

        sprites = {
            selection: svg.getElementById('selection')
        };

        Meteor.subscribe('operations');
    });
};


