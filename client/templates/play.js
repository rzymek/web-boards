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

Template.play.board = function() {
    var gameInfo = S.gameInfo();
    return {w: gameInfo.board.width, h: gameInfo.board.height};
};
Template.play.boardImg = function() {
    if (S.selectedGame())
        return '/games/' + S.selectedGame() + '/' + S.gameInfo().board.image;
    else
        return null;
};
Template.play.status = function() {
    return Meteor.status();
};

Template.play.rendered = function() {
    if (S.gameInfo().board.grid === null) {
        return;
    }
    var svg = setupGrid();
    if (!isTouchDevice()) {
        svgZoomAndPan(svg);
        jsSetup();
    } else {
        document.getElementById('panel').style.display = 'none';
    }

    Meteor.subscribe('operations');
    ctx.menu = {
        'Undo': function() {
            Meteor.call('undo');
        },
        'Toggle units': function() {
            console.log('toggle');
        },
        'Back': function() {
            if (ctx.opBacktrack === null) {
                ctx.opBacktrack = Operations.find({}).count();
            }
            ctx.opBacktrack--;
            if (ctx.opBacktrack < 0) {
                ctx.opBacktrack = 0;
                return;
            }
            console.log("backing up", ctx.opBacktrack);
            //TODO: optimize
            var data = Operations.find({}).fetch()[ctx.opBacktrack];
            (new PlaceOperation(data)).undo();
        },
        'Fwd': function() {
            if (ctx.opBacktrack === null) {
                return;
            }
            if (ctx.opBacktrack >= Operations.find({}).count()) {
                ctx.opBacktrack = null;
                return;
            }
            var data = Operations.find({}).fetch()[ctx.opBacktrack];
            (new PlaceOperation(data)).run();
            ctx.opBacktrack++;
        }
    };
    S.setMenuItems(Object.keys(ctx.menu));

    Meteor.Keybindings.removeAll();
    Meteor.Keybindings.add({
        '←': ctx.menu.Back,
        '→': ctx.menu.Fwd,
        'ctrl+z': ctx.menu.Undo,
        'ctrl+shift+z': function() {
            alert('redo not implemented yet');
        }
    });
};


