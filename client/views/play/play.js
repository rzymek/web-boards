setupSvgWidth = function(tmpl) {
    if (!isTouchDevice()) {
        tmpl.svgWidth = '100%';
        tmpl.svgHeight = '100%';
    } else {
        tmpl.svgWidth = function() {
            return tmpl.board().w;
        };
        tmpl.svgHeight = function() {
            return tmpl.board().h;
        };
    }
};

setupGrid = function(svg, hexClicked) {
    var layer = byId('hexes');
    layer.onclick = function(event) {                
        var hex = event.target.correspondingUseElement || event.target;        
        hexClicked(hex);
    };
    var board = Session.get('gameInfo').board;
    var hex2hex = {
        x: board.grid.hexWidth,
        y: board.grid.hexSize
    };

    var A = 2 * board.grid.hexSize / Math.sqrt(3);
    var hex;
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
            hex = document.createElementNS(SVGNS, 'use');
            hex.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", '#hex');
            var translate = svg.createSVGTransform();
            var scale = svg.createSVGTransform();

            translate.setTranslate(hx, hy);
            scale.setScale(hscale, hscale);

            hex.transform.baseVal.appendItem(translate)
            hex.transform.baseVal.appendItem(scale)

            hex.setAttribute('class', 'hex');
            hex.id = 'h' + x + '_' + y;
            hex.rx = hx;
            hex.ry = hy;
            layer.appendChild(hex);
        }
    }
    return svg;
}

Template.play.board = function() {
    var gameInfo = Session.get('gameInfo');
    if (!gameInfo)
        return;
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
        return e.tagName === 'image';
    })[0];
    if (override) { 
        img.origHref = img.href.baseVal;
        img.href.baseVal = override;
    } else if (img.origHref) {
        img.href.baseVal = img.origHref;
        delete img.origHref;
    }
});

Meteor.startup(function() {
    setupSvgWidth(Template.play);
});

Template.play.rendered = function() {
    if (!isTouchDevice()) {
        var svg = byId('svg');
        svgZoomAndPan(svg);
        $('#menu').addClass('touch');
    } else {
        $('#menu').addClass('mouse');
    }
    
    Deps.autorun(function(c) {
        var gameInfo = Session.get('gameInfo');
        if (gameInfo == null)
            return;
        var svg = byId('svg');
        setupGrid(svg, hexClicked);
        c.stop();
        Session.set('board.ready', true);
    });

    unbindKeys();
    if (!Template.join.guest()) {
        bindKeys();
    }

    selectById(null);
};


