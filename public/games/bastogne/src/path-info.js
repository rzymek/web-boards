
$.get('/games/bastogne/path-info.json' + requestSuffix()).done(function(pathInfo) {
    var COST = {
        'road':0.5,
        'rail':1,
        'trail':1
    };
    getPathMovementCost = function(fromId, toId) {
        var value = _.chain(pathInfo).filter(function(path){
            var fromIdx = path.nodes.indexOf(fromId);
            if(fromIdx !== -1) {
                return (toId === path.nodes[fromIdx+1] || toId === path.nodes[fromIdx-1]);
            }
        }).map(function(path){
            return COST[path.type];
        }).min().value();
        return value === Number.POSITIVE_INFINITY ? undefined : value;
    };
});
