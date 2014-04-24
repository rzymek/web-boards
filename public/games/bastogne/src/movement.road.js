var showRoadMovement = function() {
    return Deps.autorun(function() {
        var selectedId = Session.get('selectedPiece');
        var layer = byId('traces');
        if (!selectedId) {
            $(layer).empty();
            clearHexMarks();
            return;
        }
        var counter = byId(selectedId);
        var side = getOwner(counter);
        var upToEZOC = function(hexIds) {
            var res = [];
            for (var i = 0; i < hexIds.length; i++) {
                if (isEZOC(side, hexIds[i]) || containsEnemy(side, hexIds[i])) {
                    return {
                        nodes: res,
                        stop: true
                    };
                }
                res.push(hexIds[i]);
            }
            return {
                nodes: res,
                stop: false
            };
        };

        trimToEZOC = function(road) {
            var trimmed = upToEZOC(road.hexes);
            if (trimmed.stop) {
                road.hexes = trimmed.nodes;
                delete road.crossroad;
            }
            return road;
        };
        var visitedKey = function(travelerInfo) {
            return travelerInfo.hex + (travelerInfo.type || '');
        };
        (function() {
            var result = {};
            var visited = {};// hex -> steps left
            var visit = [{
                    hex: counter.position.id,
                    stepsLeft: 3
                }];
            function getStepLeftOnPrevVisit(travelerInfo) {
                var previous = visited[visitedKey(travelerInfo)];
                return (previous == null)
                        ? 0
                        : previous.stepsLeft;
            }
            function isStartHex(travelerInfo) {
                return visited[travelerInfo.hex];
            }
            function stop(travelerInfo) {
                return !travelerInfo
                        || getStepLeftOnPrevVisit(travelerInfo) >= travelerInfo.stepsLeft
                        || isStartHex(travelerInfo.hex);
            }
            step = function() {
                var travelerInfo = visit.pop();
                if (stop(travelerInfo))
                    return;
                visited[visitedKey(travelerInfo)] = travelerInfo;
                var neightours = getRadiating(travelerInfo.hex)
                        .map(trimToEZOC);
                _.chain(neightours).forEach(function(info) {
                    var penalty = (travelerInfo.type && info.type !== travelerInfo.type) ? 1 : 0;
                    var newStepsLeft = travelerInfo.stepsLeft - penalty;
                    if (newStepsLeft <= 0)
                        return;
//                    info.hexes.forEach(placeMPS.bind(_, newStepsLeft));
                    info.hexes.forEach(function(hexId) {
                        result[hexId] = Math.max(newStepsLeft, result[hexId] || 0);
                    });
                    if (!info.crossroad)
                        return;
                    visit.push({
                        hex: info.crossroad,
                        type: info.type,
                        stepsLeft: newStepsLeft
                    });
                });
                console.log('visit', visit.map(function(it) {
                    return byId(it.hex);
                }), '\n' + visit.map(function(it) {
                    return it.hex + ':' + it.stepsLeft;
                }).join('\n'));
            };
//            var loop = Meteor.setInterval(function() {
//                if (visit.length === 0)
//                    Meteor.clearInterval(loop);
//                step();
//            }, 10);
            while(visit.length > 0) {
                step();
            }
            _.keys(result).forEach(function(hexId){
                placeMPS(result[hexId], hexId);
            });
//            Meteor.Keybindings.add({
//                'space': step
//            });
        })();
    });
};