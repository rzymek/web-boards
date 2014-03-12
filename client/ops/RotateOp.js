getRotatation = function(svg, transformList) {
    return translateScaleRotate(svg, transformList).getItem(2);
};

RotateOp = function(data) {
    var svg = byId('svg');
    var counter = byId(data.counterId);
    var rotation = getRotatation(svg, counter.transform.baseVal);
    var initial = rotation.angle;
    rotation.setRotate(data.angle, 0, 0);
    return function() {
        rotation.setRotate(initial, 0, 0);
    };
};

