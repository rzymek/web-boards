library webboards_core;

import 'dart:html';
import 'package:intl/intl.dart';
import 'dart:svg' as svg; 
import 'package:svg_zoom_and_pan/svg_zoom_and_pan.dart' as zoomAndPan;
import 'package:logging/logging.dart';
import 'dart:json' as json;

final Logger log = new Logger("web-boards.core");
svg.SvgSvgElement root; 

void initLogging() {
  log.onRecord.listen((LogRecord e) {
    var fmt = new DateFormat("H:m:s");
    print("${fmt.format(e.time)} [${e.level}] ${e.message}");
  });  
}


void init() {
  initLogging();
  log.info("web-boards init...");
  root = query("svg");
  zoomAndPan.setupZoomAndPan(root);
  root.attributes["width"] = "100%";
  root.attributes["height"] = "99%"; 
  connect();
}

void connect() {
  var url = "units.json";
  var request = HttpRequest.getString(url).then((String resp) {
    Map units = json.parse(resp);    
    units.forEach((v,k) {
      var template = root.getElementById('counter');
      svg.SvgElement c = template.clone(true);
      c.href.baseVal = v;
      c.x.baseVal = 10;
      c.y.baseVal = 10;
    });
  });
}