if (Meteor.isClient) {

    Session.set('edit.ready', false);
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
    Template.edit.paths = function() {
        return Edit.find();
    }
    Template.edit.types = function() {
        return Object.keys(pathTypeColors);
    };
    Template.edit.actions = function() {
        return Object.keys(actions);
    };
    Template.edit.helpers({
        length: function(arr) {
            return arr.length;
        }
    });
    Template.edit.events({
        'change #select': function(e) {
            Session.set('edit.path.type', e.currentTarget.value);
        },
        'change #paths': function(e) {
            Session.set('edit.path', e.currentTarget.value);
        },
        'click #toolbar button': function(e) {
            var action = e.target.textContent;
            actions[action]();
        }
    });
    Meteor.startup(function() {
        setupSvgWidth(Template.edit);
    });
    Edit.before.insert(function(userId, doc) {
        doc.game = Session.get('editingGame');
        doc.timestamp = new Date();
    });
    var actions = {
        'reset all': function() {
            Edit.find({}, {reactive: false}).forEach(function(it) {
                Edit.remove(it._id);
            });
        },
        'import': function() {
            $.get('/games/' + Session.get('editingGame') + '/path-info.json' + requestSuffix()).done(function(data) {
                data.forEach(function(it) {
                    Edit.insert(it);
                });
            });
        },
        'delete path': function() {
            Edit.remove(Session.get('edit.path'));
        },
        'undo': function() {
            Edit.update(Session.get('edit.path'), {
                $pop: {nodes: 1}
            });
        },
        'new path': function() {
            Session.set('edit.path', Edit.insert({
                type: Session.get('edit.path.type'),
                nodes: []
            }));
        }
    };
    pathTypeColors = {
        road: {color: 'black'},
        trail: {color: 'brown'},
        rail: {color: 'gray'},
        stream: {color: 'blue'},
        interrupted: {color: 'red'}
    };
    Session.setDefault('edit.path.type', Object.keys(pathTypeColors)[0]);
    function editHexClicked(hex) {
        var svg = byId('svg');
        if (svg.panning)
            return;
        Edit.update(Session.get('edit.path'), {
            $push: {nodes: hex.id}
        });
    }
//minor translaction based on type
//when there are multiple paths on the same hex
    function tx(type) {
        var k = Object.keys(pathTypeColors);
        return (k.indexOf(type)) * 2;
    }

    nodesToSVGPath = function(hexIds, offset) {
        offset = offset || 0;
        var trace = document.createElementNS(SVGNS, "path");
        trace.style.fill = 'none';
        hexIds.forEach(function(hexId) {
            var hex = byId(hexId);
            var createSeg = trace.pathSegList.numberOfItems === 0
                    ? trace.createSVGPathSegMovetoAbs
                    : trace.createSVGPathSegLinetoAbs;
            var seg = createSeg.call(trace, hex.rx + offset, hex.ry + offset);
            trace.pathSegList.appendItem(seg);
        });
        return trace;
    };
    function toSVGPath(data) {
        var offset = tx(data.type);
        var trace = nodesToSVGPath(data.nodes, offset);
        trace.id = data._id;
        trace.style.stroke = pathTypeColors[data.type].color;
        return trace;
    }

    Deps.autorun(function() {
        var id = Session.get('edit.path');
        var layer = byId('all');
        $('#all').empty();
        Edit.find({_id: {$ne: id}}).map(toSVGPath).forEach(function(path) {
            layer.appendChild(path);
        });
    });
    Deps.autorun(function() {
        $('#data').empty();
        var id = Session.get('edit.path');
        if (!id)
            return;
        var data = Edit.findOne(id);
        if (!data) {
            Session.set('edit.path', null);
            return;
        }
        byId('data').appendChild(toSVGPath(data));
    });
    Deps.autorun(function() {
        var g = Session.get('editingGame');
        if (is('edit.ready') && g)
            Meteor.subscribe("edit", g, {
                onReady: function() {
                    var last = Edit.findOne({}, {sort: {timestamp: 1}});
                    if (last) {
                        Session.set('edit.path', last._id);
                    }
                }
            });
    });
    selectType = function (i) {
        Session.set('edit.path.type', Object.keys(pathTypeColors)[i]);
    }
    Template.edit.rendered = function() {
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
        Session.set('edit.ready', true);
        Meteor.Keybindings.add({
            'u': actions.undo,
            'n': actions['new path'],
            '1': selectType.bind(_, 0),
            '2': selectType.bind(_, 1),
            '3': selectType.bind(_, 2),
            '4': selectType.bind(_, 3),
            '5': selectType.bind(_, 4)
        });
    };
}