library webboards_core;

import 'dart:html';
import 'package:svg_zoom_and_pan/svg_zoom_and_pan.dart' as zoomAndPan;

void init() {
  var svg = query("svg");
  zoomAndPan.setupZoomAndPan(svg);
}