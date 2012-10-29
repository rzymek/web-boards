package earl.engine.client.utils;

import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGRect;

public class SVGUtils {

	public static OMSVGRect getBBox(OMSVGElement e) {
		if (e instanceof OMSVGGElement) {
			return ((OMSVGGElement) e).getBBox();
		} else if (e instanceof OMSVGPathElement) {
			return ((OMSVGPathElement) e).getBBox();
		} else if (e instanceof OMSVGImageElement) {
			return ((OMSVGImageElement) e).getBBox();
		} else {
			throw new RuntimeException("SVGUtils.getBBox: unsupported " + e.getClass().getName());
		}
	}

	public static OMSVGMatrix getTransformToElement(OMSVGElement e, OMSVGElement to) {
		if (e instanceof OMSVGGElement) {
			return ((OMSVGGElement) e).getTransformToElement(to);
		} else if (e instanceof OMSVGPathElement) {
			return ((OMSVGPathElement) e).getTransformToElement(to);
		} else if (e instanceof OMSVGImageElement) {
			return ((OMSVGImageElement) e).getTransformToElement(to);
		} else {
			throw new RuntimeException("SVGUtils.getBBox: unsupported " + e.getClass().getName());
		}
	}

}
