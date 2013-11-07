function isTouchDevice() {
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
}
if (!isTouchDevice()) {
    Template.play.svgWidth = '100%';
    Template.play.svgHeight = '100%';
} else {
    Template.play.svgWidth = function() {
        return S.gameInfo().board.width;
    };
    Template.play.svgHeight = function() {
        return S.gameInfo().board.height;
    };
}

function setupGrid(svg) {
    var svgns = "http://www.w3.org/2000/svg";
    var boardScale = 1;
    var layer = svg.getElementById('hexes');
    if (layer.childNodes.length > 0) {
        return svg; //already setup
    }
    var hex2hex = {y: 125.29 * boardScale, x: 108.5 * boardScale};
    var board = S.gameInfo().board;

    var A = 2 * 125.29 / Math.sqrt(3);
    var use;
    var yn = board.height / hex2hex.y;
    var xn = board.width / hex2hex.x;
    for (var y = 0; y < yn; y++) {
        for (var x = 0; x < xn; x++) {
            var hx = (80 * boardScale + x * hex2hex.x);
            var hy = (65 * boardScale + y * hex2hex.y);
            var hscale = A / 100 * boardScale;
            if (x % 2) {
                hy -= hex2hex.y / 2;
            }
            use = document.createElementNS(svgns, 'use');
            use.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", '#hex');
            use.setAttribute('transform', 'translate(' + hx + ' ' + hy + ') scale(' + hscale + ')');
//            use.setAttribute('transform', 'scale(' + hscale + ')');
//            use.x.baseVal.value = hx/hscale;
//            use.y.baseVal.value = hy/hscale;            
            
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
;

Template.play.board = function() {
    var gameInfo = Session.get('gameInfo');
    return {w: gameInfo.board.width, h: gameInfo.board.height};
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

Template.play.rendered = function() {
    var gameInfo = Session.get('gameInfo');
    if (gameInfo.board.grid === null) {
        return;
    }
    var svg = document.getElementById('svg');
    setupGrid(svg);
    if (!isTouchDevice()) {
        svgZoomAndPan(svg);
        var panel = $('#panel');
        panel.draggable({
            start: function() {
                panel.height(panel.height());
            }
        });
    } else {
        $('#panel').hide();
    }

    Meteor.Keybindings.removeAll();
    Meteor.Keybindings.add({ 
        '←': ctx.menu.Back,
        '→': ctx.menu.Fwd,
        'ctrl+z': ctx.menu.Undo,
        'ctrl+shift+z': function() {
            alert('redo not implemented yet');
        },
        'del': ctx.menu.Remove,
        'F': ctx.menu.Flip
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
        
        Meteor.subscribe('operations');
    });
};


