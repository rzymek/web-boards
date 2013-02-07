package earl.client.display.svg;

import java.util.List;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGAnimatedLength;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.impl.SVGElement;
import org.vectomatic.dom.svg.impl.SVGGElement;
import org.vectomatic.dom.svg.impl.SVGImageElement;
import org.vectomatic.dom.svg.impl.SVGPathElement;
import org.vectomatic.dom.svg.impl.SVGRectElement;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class SVGUtils {
	public static OMSVGRect getBBox(SVGElement e) {
		String transform = e.getAttribute("transform");
		if("".equals(transform)) {
			return getRawBBox(e);
		}
		SVGElement owner = (SVGElement) e.getParentElement();
		SVGGElement g = (SVGGElement) createSVGElement("g");
		Node c = e.cloneNode(true);
		g.appendChild(c);
		owner.appendChild(g);
		OMSVGRect bBox = g.getBBox();
		g.removeChild(c);
		owner.removeChild(g);
		return bBox;
	}

	public static SVGElement createSVGElement(String tag) {
		return (SVGElement) createElementNS("http://www.w3.org/2000/svg", tag);
	}

	public static OMSVGRect getRawBBox(SVGElement e) {
		String tag = e.getTagName();
		if ("path".equals(tag)) {
			return ((SVGPathElement) e).getBBox();
		} else if ("image".equals(tag)) {
			return ((SVGImageElement) e).getBBox();
		} else if ("g".equals(tag)) {
			return ((SVGGElement) e).getBBox();
		} else if ("rect".equals(tag)) {
			return ((SVGRectElement) e).getBBox();
		} else {
			throw new RuntimeException("SVGUtils.getBBox: unsupported " + e.getClass().getName() + " " + tag);
		}
	}

	public static OMSVGPoint getCenter(SVGElement hex) {
		OMSVGRect bbox = getBBox(hex);
		OMSVGPoint point = hex.getOwnerSVGElement().createSVGPoint();
		point.setX(bbox.getCenterX());
		point.setY(bbox.getCenterY());
		return point;
	}

	public static void setXY(SVGElement e, float x, float y) {
		getX(e).getBaseVal().setValue(x);
		getY(e).getBaseVal().setValue(y);
	}

	public static OMSVGAnimatedLength getX(SVGElement e) {
		if (e instanceof SVGImageElement) {
			return ((SVGImageElement) e).getX();
		} else {
			throw new RuntimeException("SVGUtils.getX: unsupported " + e.getClass().getName());
		}
	}

	public static OMSVGAnimatedLength getY(SVGElement e) {
		if (e instanceof SVGImageElement) {
			return ((SVGImageElement) e).getY();
		} else {
			throw new RuntimeException("SVGUtils.getY: unsupported " + e.getClass().getName());
		}
	}

//	public static void addClickHandler(SVGElement e, ClickHandler clickHandler) {
//		String tag = e.getTagName();
//		if ("path".equals(tag)) {
//			OMElement.<OMSVGPathElement> convert(e).addClickHandler(clickHandler);
//		} else if ("image".equals(tag)) {
//			OMElement.<OMSVGImageElement> convert(e).addClickHandler(clickHandler);
//		} else if ("g".equals(tag)) {
//			OMElement.<OMSVGGElement> convert(e).addClickHandler(clickHandler);
//		} else {
//			throw new RuntimeException("SVGUtils.getBBox: unsupported " + e.getClass().getName() + " " + tag);
//		}
//	}

	public static void addClickHandler(SVGElement e, ClickHandler clickHandler) {
		OMElement node = OMElement.convert(e);
		HasClickHandlers hch = (HasClickHandlers) node;
		hch.addClickHandler(clickHandler);
	}

	public static native Element createElementNS(final String ns, final String name)/*-{
	    return document.createElementNS(ns, name);  
	}-*/;

	public static SVGElement findById(List<SVGElement> stack, String id) {
		for (SVGElement e : stack) {
			if(id.equals(e.getId())){
				return e;
			}
		}
		return null;
	}
}
