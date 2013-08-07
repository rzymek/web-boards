library webboards_client;

import 'dart:html';
import 'package:intl/intl.dart';
import 'dart:svg' as svg; 
import 'package:svg_zoom_and_pan/svg_zoom_and_pan.dart' as zoomAndPan;
import 'package:logging/logging.dart';
import 'dart:json' as json;

part 'src/workarounds.dart'; 

final Logger log = new Logger("web-boards.core");
svg.SvgSvgElement _svg; 

void init() {
  try {
    _initLogging();
    log.info("\n\n\nweb-boards init ...");
    _svg = query("svg");
    zoomAndPan.setupZoomAndPan(_svg);
    _svg.attributes["width"] = "100%";
    _svg.attributes["height"] = "99%"; 
    _connect();
  }catch(e) {
    _handleEx(e);
  }
}

void _initLogging() {
  log.onRecord.listen((LogRecord e) {
    var fmt = new DateFormat("H:m:s");
    print("${fmt.format(e.time)} [${e.level}] ${e.message}");
  });  
  hierarchicalLoggingEnabled = true;
  log.level = Level.FINE;
}

void _handleEx(e){ 
  log.severe("Error:",e);
  window.alert(e.toString());  
}


int _tableId() {
  var params = getUriParams(window.location.search);
  try {
    return int.parse(params['id']);
  }catch(e) {
    log.severe('id param missing', e);
    throw 'Invalid URL: expected id parameter';
  }  
}

void _connect() {
  var url = "/game/${_tableId()}";
  var request = HttpRequest.getString(url).then((String resp) {
    log.fine("units: ${resp}");
    Map units = json.parse(resp);
    var unitsLayer = _svg.getElementById('units');
    units.forEach((v,k) {
      log.fine("creating counter ${v} at ${k}");
      var template = _svg.getElementById('counter');
      svg.ImageElement c = template.clone(true);
      c.href.baseVal = v;
      c.x.baseVal.value = 10;
      c.y.baseVal.value = 10;
      unitsLayer.append(c);
    });
  }).catchError(_handleEx);
}