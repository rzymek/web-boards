function svgZoomAndPan(root) {
    function updateMousePosition(e) {
        mouse.x = e.clientX;
        mouse.y = e.clientY;
        viewbox = root.viewBox.baseVal;
        offset.x = viewbox.x;
        offset.y = viewbox.y;
    }

    function toUsertSpace(x, y) {
        var ctm = root.getScreenCTM();
        var p = root.createSVGPoint();
        p.x = x;
        p.y = y;
        return p.matrixTransform(ctm.inverse());
    }

    function updateZoom() {
        if (scale < minScale) {
            scale = minScale;
        }
        var x = mouse.x;
        var y = mouse.y;
        var before = toUsertSpace(x, y);
        var viewbox = root.viewBox.baseVal;
        viewbox.width = size.x / scale;
        viewbox.height = size.y / scale;
        var after = toUsertSpace(x, y);
        var dx = before.x - after.x;
        var dy = before.y - after.y;
        viewbox.x = viewbox.x + dx;
        viewbox.y = viewbox.y + dy;
    }

    function svgZoom(delta) {
        if (delta < 0) {
            scale /= KEY_ZOOM_STEP;
        } else {
            scale *= KEY_ZOOM_STEP;
        }
        updateZoom();
    }

    var KEY_ZOOM_STEP = 1.3;
    var minScale = 0.1;
    var scale = 1.0;
    var mouse = {x: 0, y: 0};
    var offset = {x: 0, y: 0};
    var mouseDown = false;
    root.panning = false;
    var viewbox = root.viewBox.baseVal;
    var size = {x: viewbox.width, y: viewbox.height};

    if (root.svgZoomAndPan) {
        return svgZoom;
    } else {
        root.svgZoomAndPan = true;
    }
    $(root).mousedown(function(e) {
        updateMousePosition(e);
        mouseDown = true;
    });
    $(root).mouseup(function() {
        mouseDown = false;
    });
    $(root).mousemove(function(e) {
        if (mouseDown) {
            root.panning = true;
            var x = mouse.x;
            var y = mouse.y;
            var start = toUsertSpace(x, y);
            var pos = toUsertSpace(e.clientX, e.clientY);
            var viewBox = root.viewBox.baseVal;
            viewBox.x = offset.x + (start.x - pos.x);
            viewBox.y = offset.y + (start.y - pos.y);
        } else {
            root.panning = false;
            updateMousePosition(e);
        }
    });
    $(root).click(function(e) {
        root.panning = false;
    });
    //require: jquery-mousewheel
    $(root).mousewheel(function(e, delta) {
        svgZoom(delta);
        e.preventDefault();
    });
    root.zoom = svgZoom;
}


