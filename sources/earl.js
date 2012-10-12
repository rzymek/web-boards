var svg;
function earl_init(svgRoot) {
	svg = svgRoot;
	// window.addEventListener('mousewheel', handleMouseWheel, false);
	// svg.onmouseup = svgMouseUp;
	// svg.onmousedown = svgMouseDown;
	// svg.onmousemove = svgMouseMove;
	var hexes = svg.getElementById("area").getElementsByTagName("path");
	for ( var i in hexes) {
		var hex = hexes[i];
		hex.onmouseover = onMouseOverHex;
		hex.onmouseout = onMouseOutHex;
		hex.onclick = hexClicked;
	}
	var units = svg.getElementById("units").childNodes;
	for ( var i in units) {
		var unit = units[i];
		if (unit.tagName == undefined) {
			continue;
		}
		unit.onclick = unitClicked;
		unit.onmouseover = unitMouseOver;
		unit.onmouseout = unitMouseOut;
	}
}
function deselectUnit() {
	if (selectedUnit == null) {
		return;
	}
	selectedUnit.style.outline = "";
	selectedUnit.style.opacity = 1;
	selectedUnit = null;
}
function handleMouseWheel(evt) {
	if (evt.preventDefault)
		evt.preventDefault();

	if (evt.wheelDelta)
		delta = evt.wheelDelta / 3600; // Chrome/Safari
	else
		delta = evt.detail / -90; // Mozilla

	var z = 1 + delta * 5; // Zoom factor: 0.9/1.1
	if (0.1 <= z && z <= 10) {
		svg.currentScale *= z;
	}

}

function svgMouseMove(evt) {
	var p = getEventPoint(evt).matrixTransform(stateTf);
	svg.currentTranslate.x = p.x;
	svg.currentTranslate.y = p.y;
}

function svgMouseUp(e) {
}
var stateTf, stateOrigin;
function svgMouseDown(evt) {
	if (evt.preventDefault)
		evt.preventDefault();
	var g = svg;
	stateTf = g.getCTM().inverse();
	stateOrigin = getEventPoint(evt).matrixTransform(stateTf);
}

var selectedUnit = null;
function unitClicked(e) {
	var last = selectedUnit;
	deselectUnit();
	if (last == e.target) {
		return;
	}
	selectedUnit = e.target;
	selectedUnit.style.outline = "rgba(255,0,0,0.5) solid thick"
	selectedUnit.parentNode.appendChild(selectedUnit);
	selectedUnit.style.opacity = 0.8;
	console.log(selectedUnit);
}

function unitMouseOver(e) {
	e.target.style.strokeWidth = 10;
}
function unitMouseOut(e) {
	if (e.target != selectedUnit) {
		e.target.style.strokeWidth = "";
	}
}
function onMouseOverHex(e) {
	e.target.style.opacity = 1;
}

function onMouseOutHex(e) {
	e.target.style.opacity = 0.33;
}

function getCenter(e) {
	var bbox = e.getBBox();
	var p = svg.createSVGPoint();
	p.x = bbox.x + bbox.width / 2;
	p.y = bbox.y + bbox.height / 2;
	return p;
}
function getEventPoint(evt) {
	var p = svg.createSVGPoint();

	p.x = evt.clientX;
	p.y = evt.clientY;

	return p;
}

/**
 * Sets the current transform matrix of an element.
 */
function setCTM(element, matrix) {
	var s = "matrix(" + matrix.a + "," + matrix.b + "," + matrix.c + ","
			+ matrix.d + "," + matrix.e + "," + matrix.f + ")";
	element.setAttribute("transform", s);
}

function drawMove(unit, hex) {
	var to = getCenter(hex);
	var m = hex.getTransformToElement(unit);
	to = to.matrixTransform(m);

	var from = getCenter(unit);
	var shape = document.createElementNS("http://www.w3.org/2000/svg", "line");
	shape.setAttribute("x1", from.x);
	shape.setAttribute("y1", from.y);
	shape.setAttribute("x2", to.x);
	shape.setAttribute("y2", to.y);
	shape.setAttribute("style", "stroke:rgba(255,0,0,0.5);stroke-width:15");
	svg.getElementById("area").appendChild(shape);
}
function moveToHex(unit, hex) {
	var bbox = unit.getBBox();
	var to = getCenter(hex);
	var m = hex.getTransformToElement(unit);
	to = to.matrixTransform(m);

	unit.x.baseVal.value = to.x - bbox.width / 2;
	unit.y.baseVal.value = to.y - bbox.height / 2;
}

function hexClicked(e) {
	if (selectedUnit == null) {
		return;
	}
	drawMove(selectedUnit, e.target);
	moveToHex(selectedUnit, e.target);
	parent.menu.gwtexpEarl(selectedUnit.id, e.target.id);
	deselectUnit();
}
