var svg;
function earl_init(svgRoot) {
	svg = svgRoot;
	var hexes = svg.getElementById("area").getElementsByTagName("path");
	var hx = [];
	for(var i=0;i<hexes.length;i++) {
		var hex = hexes[i];
	  hex.onmouseover=onMouseOverHex;
	  hex.onmouseout=onMouseOutHex;
	  hex.onclick=hexClicked;
		hx[i] = hex;
	}
	hx.sort(compare);
	var lastx=getCenter(hx[0]).x;
	var xx=1;
	var yy=0;
	var map="";
	for(var i=0;i<hx.length;i++) {
		var hex = hx[i];
		var p = getCenter(hex);
		if(Math.abs(lastx - p.x) < 2) {
			++yy;
		}else{
			//new col
			++xx;
			if(xx == 50) {
				yy=17;
			}else if(xx > 50) {
				yy=(xx % 2 == 0 ? 18 : 19);
			}else if(xx % 2 == 0) {
				yy=0;
			}else{
				yy=1;
			}
		}
		var s=xx+"."+yy;
//		s+=" - "+i+" / "+Math.round(p.x)+" - "+Math.round(p.y);//DEBUG
		hex.setAttribute("newid", s);
		lastx = p.x;
		map+=hex.id+"="+s+"\n";
	}
	console.log("last is "+s);
	writeConsole(map);
}

function writeConsole(content) {
 top.consoleRef=window.open('','myconsole',
  'width=350,height=250'
   +',menubar=0'
   +',toolbar=1'
   +',status=0'
   +',scrollbars=1'
   +',resizable=1')
 top.consoleRef.document.writeln(
  '<html><head><title>Console</title></head>'
   +'<body bgcolor=white onLoad="self.focus()"><pre>'
   +content
   +'</pre></body></html>'
 )
 top.consoleRef.document.close()
}

function compare(a,b){
	var p1=getCenter(a);
	var p2=getCenter(b);
	if(Math.abs(p1.x-p2.x) < 2) {
		return p2.y-p1.y;
	}else{
		return p1.x-p2.x;
	}
}

function onMouseOverHex(e) {
	e.target.style.opacity=1;
}

function onMouseOutHex(e) {
	e.target.style.opacity=0.33;
}

function getCenter(e) {
	var bb = e.getBoundingClientRect();
	var p=svg.createSVGPoint();
	p.x = bb.left + bb.width/2;
	p.y = bb.top + bb.height/2;
	return p;
}

function hexClicked(e) {
	var bb = e.target.getBoundingClientRect();
	var p=getCenter(e.target);
	p.x = bb.left + bb.width/2;
	p.y = bb.top + bb.height/2;
	console.log(e.target.id+" -> "+e.target.getAttribute("newid"));
}
