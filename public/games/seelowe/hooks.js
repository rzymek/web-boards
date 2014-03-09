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
;