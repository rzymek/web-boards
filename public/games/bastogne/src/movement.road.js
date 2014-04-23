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
        var visitedKey = function(travelerInfo){
            return travelerInfo.hex + (travelerInfo.type || '');
        };
        var step = (function() {
            var visited = {};// hex -> steps left
            var visit = [{
                    hex: counter.position.id,
                    stepsLeft: 3
                }];
            return function() {
                var travelerInfo = visit.pop();
                if (!travelerInfo) {
                    console.warn('done');
                    return;
                }
                var previous = visited[visitedKey(travelerInfo)];
                console.log('---------------------------------------\nstep', travelerInfo, previous);
                if (previous !== undefined && previous.stepsLeft >= travelerInfo.stepsLeft) {
                    return;
                }
                if(visited[travelerInfo.hex]) { //start hex
                    return;
                }
                visited[visitedKey(travelerInfo)] = travelerInfo;
                var neightours = getRadiating(travelerInfo.hex)
                        .map(trimToEZOC);
                _.chain(neightours).forEach(function(info) {
                    console.log(info);
                    var penalty = (travelerInfo.type && info.type !== travelerInfo.type) ? 1 : 0;
                    var newStepsLeft = travelerInfo.stepsLeft - penalty;
                    if (newStepsLeft <= 0)
                        return;
                    info.hexes.forEach(placeMPS.bind(_, newStepsLeft));
                    if (!info.crossroad)
                        return;
                    visit.push({
                        hex: info.crossroad,
                        type: info.type,
                        stepsLeft: newStepsLeft
                    });
                });
                console.log('been there', visited);
                console.log('visit', visit, _.pluck(visit, 'hex').map(byId));
            };
        })();
        Meteor.Keybindings.add({
            'space': step
        });
        step();
//        var roadMovement = Meteor.setInterval(step, 100);
    });
};