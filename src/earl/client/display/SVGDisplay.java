package earl.client.display;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.data.Identifiable;
import earl.client.dom.SVGElement;
import earl.client.dom.SVGMatrix;
import earl.client.dom.SVGPoint;
import earl.client.dom.SVGRect;
import earl.client.utils.DOMUtils;

public class SVGDisplay implements Display {
	private final DisplayHandler handler;
	private Board board;

	public SVGDisplay() {
		initAreas();
		initPieces();
		handler = new DisplayHandler(new SVGDisplayUpdater(this));
	}

	public SVGElement getSVGElement(Identifiable c) {
		Element e = DOM.getElementById(c.getId());
		if (e == null) {
			throw new NullPointerException(c.getId() + " not found");
		}
		return (SVGElement) e;
	}

	@Override
	public void init(Board board) {
		this.board = board;//
		Collection<Hex> stacks = board.getStacks();
		for (Hex hex : stacks) {
			alignStack(hex);
		}
	}

	public void alignStack(Hex hex) {
		SVGElement h = getSVGElement(hex);
		Collection<Counter> stack = hex.getStack();
		Collection<SVGElement> svgStack = getSVGElements(stack);
		alignStack(h, svgStack);
	}

	public void alignStack(SVGElement hex, Collection<SVGElement> counters) {
		int offset = 0;
		for (SVGElement counter : counters) {
			SVGRect bbox = counter.getBBox();
			SVGMatrix matrix = hex.getTransformToElement(counter);
			SVGPoint to = hex.getCenter();
			to = to.matrixTransform(matrix);

			float x = to.getX() - bbox.getWidth() / 2.0f + offset;
			float y = to.getY() - bbox.getHeight() / 2.0f + offset;

			counter.setX(x);
			counter.setY(y);

			offset += 3;
		}
	}

	private Collection<SVGElement> getSVGElements(Collection<Counter> stack) {
		Collection<SVGElement> result = new ArrayList<SVGElement>(stack.size());
		for (Counter counter : stack) {
			SVGElement svgCounter = getSVGElement(counter);
			result.add(svgCounter);
		}
		return result;
	}

	protected void initAreas() {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String hexId = event.getRelativeElement().getId();
				Hex hex = board.getHex(hexId);
				handler.areaClicked(hex);
			}
		};
		SVGElement area = (SVGElement) DOM.getElementById("area");
		addClickHandler(area.getElementsByTagName("path"), clickHandler);
		addClickHandler(area.getElementsByTagName("g"), clickHandler);
	}

	protected void initPieces() {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String id = event.getRelativeElement().getId();
				Counter counter = board.getCounter(id);
				handler.pieceClicked(counter);
			}
		};
		addClickHandler(DOM.getElementById("units").getElementsByTagName("image"), clickHandler);
	}

	private void addClickHandler(NodeList<com.google.gwt.dom.client.Element> hexes, ClickHandler clickHandler) {
		for (int i = 0; i < hexes.getLength(); ++i) {
			com.google.gwt.dom.client.Element element = hexes.getItem(i);
			DOMUtils.addClickHandler(element, clickHandler);
		}
	}

}
