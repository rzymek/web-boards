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
        return Object.keys(maps);
    };
    Template.edit.actions = function() {
        return Object.keys(actions);
    };
    Template.edit.events({
        'change #select': function(e) {
            selected = e.currentTarget.value;
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
            $.get('/games/' + Session.get('editingGame') + '/hex-info.json').done(function(data) {
                data.forEach(function(it) {
                    Edit.insert(it);
                });
            });
        },
        'new path': function() {
            Session.set('edit.path', Edit.insert({
                type: selected,
                nodes: []
            }));
        },
        'undo': function() {
            Edit.update(Session.get('edit.path'), {
                $pop: {nodes: 1}
            });
        }
    };
    var maps = {
        road: {color: 'black'},
        trail: {color: 'brown'},
        rail: {color: 'gray'},
        stream: {color: 'blue'},
        interrupted: {color: 'red'}
    };

    var selected = Object.keys(maps)[0];
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
        var k = Object.keys(maps);
        return (k.indexOf(type)) * 2;
    }
    
    function toSVGPath(data) {
        var trace = document.createElementNS(SVGNS, "path");
        trace.id = data._id;
        trace.style.stroke = maps[data.type].color;
        data.nodes.forEach(function(hexId) {
            var hex = byId(hexId);
            var createSeg = trace.pathSegList.numberOfItems === 0
                    ? trace.createSVGPathSegMovetoAbs
                    : trace.createSVGPathSegLinetoAbs;
            var seg = createSeg.call(trace, hex.rx + tx(data.type), hex.ry + tx(data.type));
            trace.pathSegList.appendItem(seg);
        });
        return trace;
    }

    Deps.autorun(function() {
        var id = Session.get('edit.path');
        var layer = byId('all');
        $('#all').empty();
        Edit.find({_id: {$ne:id}}).map(toSVGPath).forEach(function(path){
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

    Template.play.destroyed = function() {
        Session.set('edit.ready', false);
    };
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
    };

}