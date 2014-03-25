(function() {
    function unload() {
        for (key in pieceMenu) {
            if (key.indexOf('Rotate ') === 0) {
                delete pieceMenu[key];
            }
        }

        delete pieceMenu['High'];
        delete pieceMenu['Low'];
        delete pieceMenu['Very Low'];
        delete Special.jamm;

        MoveOp = origMoveOp;
    }


    function setupMenu() {
        var rotations = '↑↗↘↓↙↖';
        for (var i = 0; i < rotations.length; i++) {
            (function(i) {
                pieceMenu['Rotate ' + rotations.charAt(i)] = function(piece) {
                    console.log('rotation:', i * 60);
                    Operations.insert({
                        op: 'RotateOp',
                        counterId: piece.id,
                        angle: i * 60
                    });
                };
            })(i);
        }
        pieceMenu['High'] = function(piece) {
            Operations.insert({
                op: 'AltitudeOp',
                counterId: piece.id,
                scale: 1
            });
        };
        pieceMenu['Low'] = function(piece) {
            Operations.insert({
                op: 'AltitudeOp',
                counterId: piece.id,
                scale: 0.75
            });
        };
        pieceMenu['Very Low'] = function(piece) {
            Operations.insert({
                op: 'AltitudeOp',
                counterId: piece.id,
                scale: 0.5
            });
        };
    }

    var origMoveOp = MoveOp;
    function setupAutoRotate() {
        MoveOp = function(data) {
            var h1 = byId(data.counter).position;
            var h2 = byId(data.to);
            var dy = h2.ry - h1.ry;
            var dx = h2.rx - h1.rx;
            var degrees = Math.atan2(dy, dx) * 180 / Math.PI + 90;
            var round = Math.round(degrees / 60) * 60;
            var undo = [];
            undo.push(origMoveOp(data));
            undo.push(RotateOp({
                counterId: data.counter,
                angle: round 
            }));
            return function() {
                undo.forEach(function(fn) {
                    fn();
                });
            }
        };
    }

    gameModule = function() {
        setupMenu();
        setupAutoRotate();
        Special.jamm = {
            src: '/games/seelowe/markers/jamm.svg',
            action: function(hex, counter) {
                if (!counter)
                    return;
                Operations.insert({
                    op: 'PlaceMarkerOp',
                    src: '/games/seelowe/markers/jamm.svg',
                    counterId: counter.id
                });
            }
        };
        AltitudeOp = function(data) {
            var svg = byId('svg');
            var counter = byId(data.counterId);
            var tx = ensureTransformListSize(svg, counter.transform.baseVal, 5);
            var initial = counter.altitude || 1;
            tx.getItem(4).setScale(data.scale, data.scale);
            counter.altitude = data.scale;
            return function() {
                tx.getItem(4).setScale(initial, initial);
            };
        };

        return unload;
    };
})();