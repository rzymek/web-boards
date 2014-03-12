console.warn('seelove hook');
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
    return function() {       
        for(key in pieceMenu) {
            if(key.indexOf('Rotate ') === 0) {
                delete pieceMenu[key];
            }
        }
    };
};


