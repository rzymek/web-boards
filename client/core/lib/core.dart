library webboards_core;

import 'dart:html';
import 'dart:svg' as svg;
import 'package:svg_zoom_and_pan/svg_zoom_and_pan.dart' as zoomAndPan;

void init() {
  svg.SvgSvgElement svg = query("svg");
  zoomAndPan.setupZoomAndPan(svg);
  svg.attributes["width"] = "100%";
  svg.attributes["height"] = "100%";
}