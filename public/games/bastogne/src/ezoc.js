var isEnemyOnList = function(counterOwner, otherCounters) {
    return otherCounters.map(function(counter){
        return getOwner(counter);
    }).filter(function(owner){
        return owner && owner !== counterOwner;
    }).length > 0;
};

containsEnemy = function(counterOwner, hexId) {    
    return isEnemyOnList(counterOwner, byId(hexId).stack || []);
};

isEZOC = function(counter, hexId) {
    var counterOwner = _.isString(counter) ? counter : getOwner(counter);
    var adjCounters = _.chain(getAdjacent(hexId)).map(function(hex){
        return hex.stack || [];
    }).flatten().value();
    return isEnemyOnList(counterOwner, adjCounters);
};
