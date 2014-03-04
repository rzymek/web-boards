AckOverlay = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    var overlays = hex.overlays;
    hex.overlays.forEach(function(it) {
        it.remove();
    });
    delete hex.overlays;
    return function() {
        var layer = svg.getElementById('overlays');
        overlays.forEach(function(it) {
            layer.appendChild(it);
        });
        hex.overlays = overlays;
    };
};