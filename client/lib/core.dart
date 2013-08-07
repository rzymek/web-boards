library webboards_core;

import 'dart:html';
import 'package:intl/intl.dart';
import 'dart:svg' as svg; 
import 'package:svg_zoom_and_pan/svg_zoom_and_pan.dart' as zoomAndPan;
import 'package:logging/logging.dart';

final Logger log = new Logger("web-boards.core");

void initLogging() {
  log.onRecord.listen((LogRecord e) {
    var fmt = new DateFormat("H:m:s");
    print("${fmt.format(e.time)} [${e.level}] ${e.message}");
  });  
}

void init() {
  initLogging();
  log.info("web-boards init...");
  svg.SvgSvgElement svg = query("svg");
  zoomAndPan.setupZoomAndPan(svg);
  svg.attributes["width"] = "100%";
  svg.attributes["height"] = "99%"; 
  connect();
}

void connect() {
  var url = "";
}