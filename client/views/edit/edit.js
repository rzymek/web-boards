Template.edit.board = function() {
    return Template.play.board();
};

Template.edit.boardImg = function() {
    var game = Session.get('editingGame');
    var info = Session.get('gameInfo');
    if (game && info) {
        return '/games/' + game + '/images/' + info.board.image;
    } else {
        return '/img/loading.gif';
    }
};

Template.edit.types = function() {
    return Object.keys(maps);
};
Template.edit.actions = function() {
    return Object.keys(actions);
};
Template.edit.events({
    'change #select': function(e) {
        selected = e.currentTarget.value;
    },
    'change #actions': function(e) {
        var action = e.currentTarget.value;
        actions[action]();
    }
});
Meteor.startup(function() {
    setupSvgWidth(Template.edit);
});
Edit.before.insert(function(userId, doc) {
    doc.game = Session.get('editingGame');
});

var actions = {
    '': function() {
    },
    'reset all': function() {
        Edit.find({}, {reactive: false}).forEach(function(it) {
            Edit.remove(it._id);
        });
    }
};
var maps = {
    road: {color: 'black'},
    trail: {color: 'yellow'},
    rail: {color: 'red'},
    stream: {color: 'blue'}
};

var selected = Object.keys(maps)[0];
var last = null;
function editHexClicked(e) {
    var svg = byId('svg');
    if (svg.panning)
        return;
    var hex = e.currentTarget;

    if (last) {
        last.style.fill = '';
        if (last === hex) {
            last = null;
            return;
        }
        var existing = Edit.findOne({
            type: selected,
            $or: [
                {from: last.id, to: hex.id},
                {from: hex.id, to: last.id}
            ]
        });
        console.log(existing);
        if (existing) {
            Edit.remove(existing._id);
        } else {
            Edit.insert({
                from: last.id,
                to: hex.id,
                type: selected
            });
        }
    }
    hex.style.fill = 'red';
    last = hex;
}

function tx() {
    return Object.keys(maps).indexOf(selected) * 3;
}
Edit.find().observe({
    added: function(data) {
        var trace = document.createElementNS(SVGNS, "path");
        trace.id = data._id;
        trace.setAttribute('stroke', maps[data.type].color);
        trace.style.opacity = "0.5";
        trace.setAttribute('stroke-width', '5px');
        var from = byId(data.from);
        var to = byId(data.to);
        trace.pathSegList.appendItem(trace.createSVGPathSegMovetoAbs(
                from.rx + tx(),
                from.ry + tx()
                ));
        trace.pathSegList.appendItem(trace.createSVGPathSegLinetoAbs(
                to.rx + tx(),
                to.ry + tx()
                ));
        svg.getElementById('data').appendChild(trace);
    },
    removed: function(data) {
        byId(data._id).remove();
    }
});

Deps.autorun(function() {
    Meteor.subscribe("edit", Session.get('editingGame'));
});

Template.edit.rendered = function() {
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
    setupGrid(svg, editHexClicked);
};