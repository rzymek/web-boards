PlaceMarkerOp = function(data) {
    var counter = byId(data.counterId);
    var bbox = counter.getBBox();

    var marker = document.createElementNS(SVGNS, 'image');
    marker.width.baseVal.value = bbox.width;
    marker.height.baseVal.value = bbox.height;
    marker.id = data._id;
    marker.href.baseVal = data.src;
    counter.appendChild(marker);
    
    return function() {
        marker.remove();
    };
};