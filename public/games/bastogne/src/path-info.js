
$.get('/games/bastogne/path-info.json' + requestSuffix()).done(function(pathSegments) {
    var COST = {
        'road': 0.5,
        'rail': 1,
        'trail': 1
    };

    function getEnds(segInfo) {
        return [_.first(segInfo.nodes), _.last(segInfo.nodes)];
    }
    
    pathSegments.forEach(function(segInfo) {
        var first = _.first(segInfo.nodes);
        var last = _.first(segInfo.nodes);
        function notThis(otherSeg){
            return segInfo !== otherSeg;
        }
        segInfo.prev = pathSegments.filter(function(segInfo){
            return _.last(segInfo.nodes) === first;
        }).filter(notThis);
        segInfo.prevRev = pathSegments.filter(function(segInfo){
            return _.first(segInfo.nodes) === first;
        }).filter(notThis);
        segInfo.next = pathSegments.filter(function(segInfo){
            return _.first(segInfo.nodes) === last;
        }).filter(notThis);
        segInfo.nextRev = pathSegments.filter(function(segInfo){
            return _.last(segInfo.nodes) === last;
        }).filter(notThis);
    });
    
    getPathMovementCost = function(fromId, toId) {
        var value = _.chain(pathSegments).filter(function(path) {
            var fromIdx = path.nodes.indexOf(fromId);
            if (fromIdx !== -1) {
                return (toId === path.nodes[fromIdx + 1] || toId === path.nodes[fromIdx - 1]);
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
    }
});
