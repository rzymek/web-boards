getRotatation = function(svg, transformList) {
    var tranform;
    for (var i = 0; i < transformList.numberOfItems; i++) {
        tranform = transformList.getItem(i);
        if (tranform.angle !== 0) {
            return tranform;
        }
    }
    tranform = svg.createSVGTransform();
    transformList.appendItem(tranform);
    return tranform;
}

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

