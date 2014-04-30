
$.get('/games/bastogne/path-info.json' + requestSuffix()).done(function(pathSegments) {
    var COST = {
        'road': 0.5,
        'rail': 1,
        'trail': 1
    };
    reverseSeg = function(seg) {
        return {
            nodes: seg.nodes.reverse(),
            next: seg.prevRev,
            nextRev: seg.prev,
            prev: seg.nextRev,
            prevRev: seg.next
        };
    };
    var crossroads = {};
    pathSegments.forEach(function(segInfo) {
        var first = _.first(segInfo.nodes);
        var last = _.last(segInfo.nodes);
        var edge = {
            hexes: segInfo.nodes,
            type: segInfo.type,
            ends: [first, last]
        };
        function addCrossroadsPath(crossroadHex, edge) {
            var crossroad = crossroads[crossroadHex];
            if (!crossroad)
                crossroads[crossroadHex] = [edge];
            else
                crossroads[crossroadHex].push(edge);
        }
        addCrossroadsPath(first, edge);
        addCrossroadsPath(last, edge);
    });

    getRadiating = function(crossroadHex) {
        return (crossroads[crossroadHex] || []).map(function(edge) {
            return {
                hexes: edge.hexes[0] === crossroadHex ? edge.hexes : edge.hexes.reverse(),
                type: edge.type,
                crossroad: _.chain(edge.ends).without(crossroadHex).first().value()
            };
        });
    };

    getPathMovementCost = function(fromId, toId) {
        var value = _.chain(pathSegments).filter(function(path) {
            var fromIdx = path.nodes.indexOf(fromId);
            if (fromIdx !== -1) {
                return (toId === path.nodes[fromIdx + 1] || toId === path.nodes[fromIdx - 1]);
            }else{
                return false;
            }
        }).map(function(path) {
            return COST[path.type];
        }).min().value();
        return value === Number.POSITIVE_INFINITY ? undefined : value;
    };
    window.pathInfo = pathSegments;
    getPath = function(hex) {
        var hexId = 'id' in hex ? hex.id : hex;
        _.chain(pathSegments).filter(function(path) {
            return path.nodes.indexOf(hexId) >= 0;
        });
    };
});
