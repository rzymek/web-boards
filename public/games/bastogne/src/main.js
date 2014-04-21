var isAttack = function(counter, targetHex) {
    var targetStackOwner = (targetHex.stack && targetHex.stack.map(function(it) {
        return ownerByCategory[it.category];
    })[0]);
    var counterOwner = ownerByCategory[counter.category];
    return (targetStackOwner !== undefined && counterOwner !== undefined && counterOwner !== targetStackOwner);
};
var isArty = function(counter) {
    return (getUnitInfo(counter).artyType !== undefined);
};
gameModule = function() {
    var original = {
        MoveOp: MoveOp,
        moveTo: moveTo
    };
    moveTo = function(counter, targetHex) {
        if (isAttack(counter, targetHex)) {
            if (isArty(counter)) {
                Operations.insert({
                    op: 'DeclareBarrage',
                    counterId: counter.id,
                    targetHex: targetHex.id
                });
            } else {
                Operations.insert({
                    op: 'DeclareAttack',
                    sourceHex: counter.position.id,
                    targetHex: targetHex.id
                });
            }
        } else {
            original.moveTo(counter, targetHex);
        }
    };
    function getAttackTarget(hex) {
        if (hex.attack) {
            return hex;
        } else if (hex.attackSource) {
            return hex.attackSource.target;
        } else {
            return null;
        }
    }
    function abandonAttack(hex) {
        if (!hex)
            return function() {
            };
        var undoData = hex.attack;
        _.values(hex.attack.arrows).forEach(function(el) {
            el.remove();
        });
        _.values(hex.attack.from).forEach(function(sourceHex) {
            delete sourceHex.attackSource;
        });
        hex.attack.odds.remove();
        delete hex.attack;
        return function() {
            hex.attack = undoData;
            var overlays = byId('overlays');
            _.values(hex.attack.arrows).forEach(function(el) {
                overlays.appendChild(el);
            });
            _.values(hex.attack.from).forEach(function(sourceHex) {
                sourceHex.attackSource = {
                    target: hex
                };
            });
            overlays.appendChild(hex.attack.odds);
        };
    }
    MoveOp = function(data) {
        var counter = byId(data.counter);
        if (counter.name.match(/ DG$/)) {
            return original.MoveOp(data);
        }
        var from = counter.position;
        var to = byId(data.to);
        var DGfrom = from.stack ? from.stack.filter(
                function(c) {
                    return c.name.match(/ DG$/);
                }) : [];
        var DGto = to.stack ? to.stack.filter(
                function(c) {
                    return c.name.match(/ DG$/);
                }) : [];

        var undo = [];
        undo.push(cancelBarrage(counter));
        undo.push(original.MoveOp(data));
        if (DGfrom.length > 0 && DGto.length === 0) {
            undo.push(PlaceOp({
                _id: (new Meteor.Collection.ObjectID()).toHexString(),
                name: DGfrom[0].name,
                category: DGfrom[0].category,
                hexid: data.to
            }));
        }
        if (from.stack && from.stack.length === DGfrom.length) {
            DGfrom.forEach(function(it) {
                undo.push(RemoveElementOp({element: it.id}));
            });
        }
        undo.push(abandonAttack(getAttackTarget(from)));
        undo.push(abandonAttack(getAttackTarget(to)));
        if ((from.stack || []).length === 0) {
            undo.push(abbortBarrage1(from.barrage));
        }
        return function() {
            undo.reverse().forEach(function(fn) {
                fn();
            });
        };
    };
    console.log('here');
    var showMovement = Deps.autorun(function() {
        return;//TODO:enable
        var selectedId = Session.get('selectedPiece');
        if (!selectedId) {
            $('#overlays').empty();
            clearHexMarks();
            return;
        }
        var counter = byId(getSelectedId());
        var side = ownerByCategory[counter.category];
        var cinfo = getUnitInfo(counter);

        console.log(counter);
        var startId = counter.position.id;
        var go = [startId];
        var costToGetTo = {};
        costToGetTo[startId] = cinfo.movement;

        while (true) {
//        Meteor.setInterval(function(c) {
            var beginId = go.pop();
            if (!beginId)
//                c.stop();
                break;
            var mps = costToGetTo[beginId];
//            setSpriteTexts(placeSprite(sprites.target, byId(begin)), mps, "!!!");
            getAdjacentIds(beginId).filter(function(adjId) {
                return !containsEnemy(side, adjId);
            }).forEach(function(adjId) {
                var otherRouteCost = costToGetTo[adjId];
                var hinfo = getHexInfo(adjId);
                var mpsAtAdj = mps;
                if (isEZOC(counter, adjId)) {
                    mpsAtAdj -= 2;
                } else {
                    var pathMovement = getPathMovementCost(beginId, adjId);
                    if (pathMovement) {
                        mpsAtAdj -= pathMovement;
                    }
                }
                mpsAtAdj -= hinfo.movement;
                if (mpsAtAdj >= 0) {
                    if (!otherRouteCost || otherRouteCost < mpsAtAdj) {
//                        setSpriteTexts(placeSprite(sprites.mps, byId(adjId)), mpsAtAdj);
                        costToGetTo[adjId] = mpsAtAdj;
                        go.push(adjId);
                    }
                }
            });
//        }, 100);
        }
        Object.keys(costToGetTo).forEach(function(hexId) {
            placeMPS(costToGetTo[hexId], hexId);
        });
    });
    function placeMPS(value, hexId) {
        var hex = byId(hexId);
        if (!hex) {
            console.error(hexId, 'not found');
        }
        var s = placeSprite(sprites.mps, hex);
        s.style.pointerEvents = 'none';
        setSpriteTexts(s, value || '');
    }
    Deps.autorun(function() {
//        return;//TODO: remove
        var selectedId = Session.get('selectedPiece');
        var layer = byId('overlays');
        if (!selectedId) {
            $(layer).empty();
            clearHexMarks();
            return;
        }
        var counter = byId(selectedId);
        var side = ownerByCategory[counter.category];
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

        var trimToEZOC = function(road) {
            var trimmed = upToEZOC(road.hexes);
            if (trimmed.stop) {
                road.hexes = trimmed.nodes;
                delete road.crossroad;
            }
            return road;
        };

        var step = (function() {
            var visited = {};// hex -> steps left
            var visit = [{
                    hex: counter.position.id,
                    stepsLeft: 3
                }];
            return function() {
                var travelerInfo = visit.pop();
                if (!travelerInfo)
                    return;
                var previous = visited[travelerInfo.hex];
                console.log('---------------------------------------\nstep', travelerInfo, previous);
                if (previous !== undefined && previous.stepsLeft >= travelerInfo.stepsLeft && previous.type === travelerInfo.type) {
                    return;
                }
                visited[travelerInfo.hex] = travelerInfo;
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

        Meteor.setInterval(step, 100);
//        Meteor.Keybindings.add({
//            'space': step
//        });
    });

    return function() {
        showMovement.stop();
        MoveOp = original.MoveOp;
        hexClicked = original.hexClicked;
        delete CRT;
    };
};