var svgZoomAndPan;
(function (svgZoomAndPan) {
    var Point = (function () {
        function Point(x, y) {
            if (typeof x === "undefined") { x = 0; }
            if (typeof y === "undefined") { y = 0; }
            this.x = x;
            this.y = y;
        }
        return Point;
    })();
    var SvgZoomAndPan = (function () {
        function SvgZoomAndPan(root) {
            this.KEY_ZOOM_STEP = 1.3;
            this.minScale = 0.25;
            this.mouse = new Point();
            this.offset = new Point();
            this.mouseDown = false;
            this.panning = false;
            this.root = root;
            var viewbox = root.viewBox.baseVal;
            this.size = new Point(viewbox.width, viewbox.height);
            this.minScale = Math.min(document.body.clientWidth / viewbox.width, document.body.clientHeight / viewbox.height);
        }
        SvgZoomAndPan.prototype.updateMousePosition = function (e) {
            this.mouse.x = e.clientX;
            this.mouse.y = e.clientY;
            var viewbox = this.root.viewBox.baseVal;
            this.offset.x = viewbox.x;
            this.offset.y = viewbox.y;
        };
        SvgZoomAndPan.prototype.toUsertSpace = function (x, y) {
            var ctm = this.root.getScreenCTM();
            var p = this.root.createSVGPoint();
            p.x = x;
            p.y = y;
            return p.matrixTransform(ctm.inverse());
        };
        SvgZoomAndPan.prototype.attach = function () {
            var _this = this;
            this.root.onmousedown = function (e) {
                _this.updateMousePosition(e);
                _this.mouseDown = true;
            };
            this.root.onmouseup = function (e) {
                return _this.mouseDown = false;
            };
            this.root.onmousemove = function (e) {
                if (_this.mouseDown) {
                    _this.panning = true;
                    var x = _this.mouse.x;
                    var y = _this.mouse.y;
                    var start = _this.toUsertSpace(x, y);
                    var pos = _this.toUsertSpace(e.clientX, e.clientY);
                    var viewBox = _this.root.viewBox.baseVal;
                    viewBox.x = _this.offset.x + (start.x - pos.x);
                    viewBox.y = _this.offset.y + (start.y - pos.y);
                } else {
                    _this.panning = false;
                    _this.updateMousePosition(e);
                }
            };
        };
        return SvgZoomAndPan;
    })();

    function setup(root) {
        var zoomAndPan = new SvgZoomAndPan(root);
        zoomAndPan.attach();
    }
    svgZoomAndPan.setup = setup;
})(svgZoomAndPan || (svgZoomAndPan = {}));


svgZAP = svgZoomAndPan;