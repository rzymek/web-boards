var showMovement = function() {
    return Deps.autorun(function() {
        var selectedId = Session.get('selectedPiece');
        if (!selectedId) {
            $('#traces').empty();
            clearHexMarks();
            return;
        }
        var counter = byId(getSelectedId());
        var side = getOwner(counter);
        var cinfo = getUnitInfo(counter);

        console.log(counter);
        var startId = counter.position.id;
        var go = [startId];
        var costToGetTo = {};
        costToGetTo[startId] = cinfo.movement;

        var step = function() {
            var beginId = go.pop();
            if (!beginId)
                return false;
            var mps = costToGetTo[beginId];
            //setSpriteTexts(placeSprite(sprites.target, byId(begin)), mps, "!!!");
            getAdjacentIds(beginId).filter(function(adjId) {
                return !containsEnemy(side, adjId);
            }).forEach(function(adjId) {
                var otherRouteCost = costToGetTo[adjId];
                var hinfo = getHexInfo(adjId);
                var mpsAtAdj = mps;
                if (isEZOC(counter, adjId)) {
                    mpsAtAdj -= (hinfo.movement + 2);
                } else {
                    //TODO: fix road traversal                    
                    var pathMovement = getPathMovementCost(beginId, adjId);
                    if (pathMovement) {
                        mpsAtAdj -= pathMovement;
                    } else {
                        mpsAtAdj -= hinfo.movement;
                    }
                }
                //TODO: river cross
                if (mpsAtAdj >= 0) {
                    if (!otherRouteCost || otherRouteCost < mpsAtAdj) {
                        //setSpriteTexts(placeSprite(sprites.mps, byId(adjId)), mpsAtAdj);
                        costToGetTo[adjId] = mpsAtAdj;
                        go.push(adjId);
                    }
                }
            });
            return true;
        };
        var show = function() {
            _.keys(costToGetTo).forEach(function(hexId) {
                placeMPS(costToGetTo[hexId], hexId, 'traces');
            });
        };
//        var c = Meteor.setInterval(function() {
//            if (!step()) {
//                Meteor.clearTimeout(c);
//            }
//            show();
//        }, 0);
//        while (step());show();
        while (step()) {
        }
        show();
    });
};