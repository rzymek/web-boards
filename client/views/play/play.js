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
    var layer = svg.getElementById('hexes');
    if (layer.childNodes.length > 0) {
        return svg; //already setup
    }
    var board = Session.get('gameInfo').board;
    var hex2hex = {
        x: board.grid.hexWidth,
        y: board.grid.hexSize
    };

    var A = 2 * board.grid.hexSize / Math.sqrt(3);
    var use;
    var yn = board.height / hex2hex.y;
    var xn = board.width / hex2hex.x;
    for (var y = 0; y < yn; y++) {
        for (var x = 0; x < xn; x++) {
            var hx = (board.grid.originX + x * hex2hex.x);
            var hy = (board.grid.originY + y * hex2hex.y);
            var hscale = A / 100;
            if (x % 2) {
                hy -= hex2hex.y / 2;
            }
            use = document.createElementNS(SVGNS, 'use');
            use.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", '#hex');
            var translate = svg.createSVGTransform();
            var scale = svg.createSVGTransform();

            translate.setTranslate(hx, hy);
            scale.setScale(hscale, hscale);

            use.transform.baseVal.appendItem(translate)
            use.transform.baseVal.appendItem(scale)

//            use.setAttribute('transform', 'translate(' + hx + ' ' + hy + ') scale(' + hscale + ')');
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

Template.play.board = function() {
    var gameInfo = Session.get('gameInfo');
    return {
        w: gameInfo.board.width,
        h: gameInfo.board.height
    };
};

Template.play.boardImg = function() {
    var info = Session.get('gameInfo');
    var table = getTable({fields: {game: 1}});
    if (info && table) {
        return '/games/' + table.game + '/images/' + info.board.image;
    } else {
        return '/img/loading.gif';
    }
};

Template.play.destroyed = function() {
    Session.set('board.ready', false);
    unbindKeys();
    unloadModule();
};

Deps.autorun(function() {
    if (!is('board.ready'))
        return;
    var override = Session.get('boardImgOverride');
    var svg = byId('svg');
    var img = _.toArray(svg.children).filter(function(e) {
        return e.tagName === 'image'
    })[0];
    if (override) {
        img.origHref = img.href.baseVal;
        img.href.baseVal = override;
    } else if (img.origHref) {
        img.href.baseVal = img.origHref;
        delete img.origHref;
    }
});

unbindKeys = function() {
    _.each(Meteor.Keybindings._bindings, function(obj) {
        Meteor.Keybindings.removeOne(obj.key);
    });
};

Template.play.rendered = function() {
    console.log('play rendered');

    var svg = byId('svg');
    if (svg.ready)
        return;

    if (!isTouchDevice()) {
        svgZoomAndPan(svg);
        $('#menu').addClass('touch');
    } else {
        $('#menu').addClass('mouse');
    }
    var gameInfo = Session.get('gameInfo');
    if (gameInfo.board.grid === null) {
        return;
    }
    setupGrid(svg);

    Deps.autorun(function() {
        console.log('keybindings - remove');
        unbindKeys();
        if (!Template.join.guest()) {
            console.log('keybindings - add');
            Meteor.Keybindings.add({
                '←': menu.Back,
                '→': menu.Fwd,
                'ctrl+z': menu.Undo,
                'ctrl+shift+z': function() {
                    alert('redo not implemented yet');
                },
                'ctrl+shift+s': function() {
                    Session.set('boardImgOverride',
                            Session.get('boardImgOverride') ? null : '/img/board-sfw.jpg');
                },
                'Q': function() {
                    byId('svg').zoom(+1);
                },
                'W': function() {
                    byId('svg').zoom(-1);
                }
            });
        }
    });

    selectById(null);
    Session.set('board.ready', true);
    svg.ready = true;
};
