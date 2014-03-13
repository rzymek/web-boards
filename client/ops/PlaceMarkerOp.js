PlaceMarkerOp = function(data) {
    var counter = byId(data.counterId);
    var bbox = counter.getBBox();

    var marker = document.createElementNS(SVGNS, 'image');
    marker.x.baseVal.value = -bbox.width/2;
    marker.y.baseVal.value = -bbox.height/2;
    marker.width.baseVal.value = bbox.width;
    marker.height.baseVal.value = bbox.height;
    marker.id = data._id;
    marker.href.baseVal = data.src;
    counter.appendChild(marker);
    
    return function() {
        marker.remove();
    };
};