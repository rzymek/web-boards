gameModule = function() {
    console.warn('seelove gameModule()');
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

    return function() {
        for (key in pieceMenu) {
            if (key.indexOf('Rotate ') === 0) {
                delete pieceMenu[key];
            }
        }
        delete pieceMenu['High'];
        delete pieceMenu['Low'];
        delete pieceMenu['Very Low'];
        delete Special.jamm;
    };
};


