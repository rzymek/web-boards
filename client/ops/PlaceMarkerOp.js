PlaceMarkerOp = function(data) {
    var counter = byId(data.counterId);
    var bbox = counter.getBBox();

    //toggle:
    //check if is placeing the same marker if so, remove it
    for (var i = 1; i < counter.children.length; i++) {
        var c = counter.children[i];
        if (c.marker) {
            if (c.href.baseVal === data.src) {
                //the same marker is already on this counter -> remove it
                c.remove();
                return function() {
                    //insertAfter: https://developer.mozilla.org/pl/docs/DOM/element.insertBefore
                    counter.insertBefore(c, counter.children[i - 1].nextSibling);
                };
            }
        }
    }
    
    //place marker
    var marker = document.createElementNS(SVGNS, 'image');
    marker.marker = true;
    marker.x.baseVal.value = -bbox.width / 2;
    marker.y.baseVal.value = -bbox.height / 2;
    marker.width.baseVal.value = bbox.width;
    marker.height.baseVal.value = bbox.height;
    marker.id = data._id;
    marker.href.baseVal = data.src;
    counter.appendChild(marker);

    return function() {
        marker.remove();
    };
};