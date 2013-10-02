/// <reference path="../private/lib.d.ts" />

module svgZoomAndPan{
    class Point{
        x:number;
        y:number;
        constructor(x:number=0, y:number=0){
            this.x=x;
            this.y=y;
        }
    }
    class SvgZoomAndPan {        
        KEY_ZOOM_STEP = 1.3;
        size: Point;
        minScale = 0.25;
        root: SVGSVGElement;
        mouse = new Point();
        offset = new Point();
		mouseDown = false;
		panning = false;
        
        constructor(root: SVGSVGElement) {
            this.root = root;
            var viewbox = root.viewBox.baseVal;
            this.size = new Point(viewbox.width, viewbox.height);
            this.minScale = Math.min(
    			document.body.clientWidth / viewbox.width, 
                document.body.clientHeight / viewbox.height);
        }
		
		updateMousePosition(e:MouseEvent):void {
    		this.mouse.x = e.clientX;
    		this.mouse.y = e.clientY;
    		var viewbox = this.root.viewBox.baseVal;
    		this.offset.x = viewbox.x;
    		this.offset.y = viewbox.y;
  		}
		toUsertSpace(x:number, y:number):SVGPoint {
    		var ctm = this.root.getScreenCTM();
    		var p = this.root.createSVGPoint();
    		p.x = x;
    		p.y = y;
    		return p.matrixTransform(ctm.inverse());
		}
		attach():void {
			this.root.onmousedown = (e:MouseEvent) => {
				this.updateMousePosition(e);
				this.mouseDown = true;
			}
			this.root.onmouseup = (e) => this.mouseDown = false;
			this.root.onmousemove = (e:MouseEvent) => {
				if (this.mouseDown) {
        			this.panning = true;
        			var x = this.mouse.x;
        			var y = this.mouse.y;
        			var start = this.toUsertSpace(x, y);
        			var pos = this.toUsertSpace(e.clientX, e.clientY);
        			var viewBox = this.root.viewBox.baseVal;
        			viewBox.x = this.offset.x + (start.x - pos.x);
        			viewBox.y = this.offset.y + (start.y - pos.y);
				} else {
        			this.panning = false;
        			this.updateMousePosition(e);
      			}				
			}
		}
	}
		  
    export function setup(root: SVGSVGElement) {
		var zoomAndPan = new SvgZoomAndPan(root);
		zoomAndPan.attach();        
    }
}


