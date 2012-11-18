package earl.client.display.svg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.impl.SVGElement;
import org.vectomatic.dom.svg.impl.SVGImageElement;
import org.vectomatic.dom.svg.impl.SVGRectElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;

import earl.client.ClientEngine;
import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.data.Identifiable;
import earl.client.display.Dimention;
import earl.client.display.Display;
import earl.client.display.DisplayHandler;
import earl.client.display.GameChangeListener;
import earl.client.display.GameHandler;
import earl.client.games.bastogne.BastogneHandler;
import earl.client.utils.Browser;
import earl.client.utils.SVGUtils;

public class SVGDisplay implements Display {
	private final GameHandler handler;
	public Board board;
	final SVGSVGElement svg;
	private SVGRectElement selectionRect;
	private DisplayHandler displayHandler;

	public SVGDisplay(SVGSVGElement svg) {
		this.svg = svg;
		initAreas();
		displayHandler = new DisplayHandler(new SVGDisplayUpdater(this));
		handler = new BastogneHandler(displayHandler);
		selectionRect = (SVGRectElement) svg.getElementById("selectionRect");
		selectionRect.getStyle().setVisibility(Visibility.HIDDEN);
		svg.appendChild(selectionRect);
	}

	public SVGElement getSVGElement(Identifiable c) {
		SVGElement e = (SVGElement) svg.getElementById(c.getId());
		if (e == null) {
			throw new NullPointerException(c.getId() + " not found");
		}
		return e;
	}

	@Override
	public void init(Board board) {
		this.board = board;
		Collection<Hex> stacks = board.getStacks();
		for (Hex hex : stacks) {
			List<Counter> counters = hex.getStack();
			for (Counter counter : counters) {
				createCounter((Counter) counter);
			}
			alignStack(hex);
		}
	}

	private void createCounter(Counter counter) {
		Element tmpl = svg.getElementById("counter");
		SVGImageElement c = (SVGImageElement) tmpl.cloneNode(true);
		String id = counter.getId();
		com.google.gwt.user.client.Element existing = DOM.getElementById(id);
		if(existing!=null) {
			String msg = "Element with id="+id+" aleady exists:"+existing;
			Browser.console(msg);
			Browser.console(existing);
			throw new RuntimeException(msg);
		}
		c.setId(id);
		c.getHref().setBaseVal(counter.getState());
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String id = event.getRelativeElement().getId();
				ClientEngine.log("click " + id);
				Counter counter = board.getCounter(id);
				handler.pieceClicked(counter);
			}
		};
		SVGUtils.addClickHandler(c, clickHandler);
		svg.getElementById("units").appendChild(c);
	}

	public void alignStack(Hex hex) {
		hideStackSelection();

		SVGElement h = getSVGElement(hex);
		Collection<Counter> stack = hex.getStack();
		List<SVGElement> svgStack = getSVGElements(stack);
		alignStack(h, svgStack);
	}

	private void hideStackSelection() {
		selectionRect.getStyle().setVisibility(Visibility.HIDDEN);
	}

	public void alignStack(SVGElement area, List<SVGElement> counters) {
		OMSVGRect areaBBox = SVGUtils.getBBox(area);
		float areaWidth = areaBBox.getWidth();
		float areaHeight = areaBBox.getHeight();
		float spacing = 3;
		Dimention counterDim = getMaxCounterSize(counters);
		Dimention placing = getPlacing(counters, counterDim, areaBBox, spacing);
		float startx = (areaWidth - (placing.width * (counterDim.width + spacing))) / 2;
		float starty = (areaHeight - (placing.height * (counterDim.height + spacing))) / 2;
		startx += areaBBox.getX();
		starty += areaBBox.getY();
		float x = 0;
		float y = 0;
		int stackOffset = 0;
		for (SVGElement counter : counters) {
			float cx = startx + x + stackOffset;
			float cy = starty + y + stackOffset;
			SVGUtils.setXY(counter, cx, cy);
			bringToTop(counter);
			x += counterDim.width + spacing;
			if (x + counterDim.width + spacing > areaWidth) {
				x = 0;
				y += counterDim.height + spacing;
			}
			if (y + counterDim.height + spacing > areaHeight) {
				x = 0;
				y = 0;
				stackOffset += 5;
			}
		}
	}

	private Dimention getPlacing(List<SVGElement> counters, Dimention counterDim, OMSVGRect areaBBox, float spacing) {
		Dimention dim = new Dimention();
		float width = areaBBox.getWidth();
		dim.width = (int) (width / (counterDim.width + spacing));
		int maxSlots = (int) (areaBBox.getHeight() / (counterDim.height + spacing) + 0.5);
		int rows = (int) Math.ceil(counters.size() / dim.width);
		dim.height = Math.min(maxSlots, rows);
		return dim;
	}

	private Dimention getMaxCounterSize(List<SVGElement> counters) {
		Dimention max = new Dimention();
		for (SVGElement svgElement : counters) {
			OMSVGRect bBox = SVGUtils.getBBox(svgElement);
			max.width = Math.max(max.width, bBox.getWidth());
			max.height = Math.max(max.height, bBox.getHeight());
		}
		return max;
	}

	private void centerInHex(SVGElement hex, SVGElement counter, int offset) {
		OMSVGRect bbox = SVGUtils.getBBox(counter);
		OMSVGPoint to = SVGUtils.getCenter(hex);

		float x = to.getX() - bbox.getWidth() / 2.0f + offset;
		float y = to.getY() - bbox.getHeight() / 2.0f + offset;
		SVGUtils.setXY(counter, x, y);
		bringToTop(counter);
	}

	public void bringToTop(Element c) {
		c.getParentElement().appendChild(c);
	}

	private List<SVGElement> getSVGElements(Collection<Counter> stack) {
		List<SVGElement> result = new ArrayList<SVGElement>(stack.size());
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
				String id = event.getRelativeElement().getId();
				ClientEngine.log("click " + id);
				Hex hex = board.getHex(id);
				handler.areaClicked(hex);
			}
		};
		SVGElement area = (SVGElement) svg.getElementById("area");
		addClickHandler(area.getElementsByTagName("path"), clickHandler);
		addClickHandler(area.getElementsByTagName("g"), clickHandler);
		addClickHandler(area.getElementsByTagName("rect"), clickHandler);
	}

	private void addClickHandler(NodeList<Element> nodeList, ClickHandler clickHandler) {
		for (int i = 0; i < nodeList.getLength(); ++i) {
			SVGElement item = (SVGElement) nodeList.getItem(i);
			SVGUtils.addClickHandler(item, clickHandler);
		}
	}

	public void showStackSelection(SVGElement hex, Collection<SVGElement> counters) {
		Iterator<SVGElement> iterator = counters.iterator();
		if (!iterator.hasNext()) {
			return;
		}
		SVGImageElement first = (SVGImageElement) iterator.next();
		centerInHex(hex, first, 0);
		float x = SVGUtils.getX(first).getBaseVal().getValue();
		float y = SVGUtils.getY(first).getBaseVal().getValue();
		selectionRect.getX().getBaseVal().setValue(x - 5);
		selectionRect.getY().getBaseVal().setValue(y - 5);
		first.getParentElement().appendChild(selectionRect);
		bringToTop(selectionRect);
		OMSVGRect bbox = SVGUtils.getBBox(first);
		for (SVGElement c : counters) {
			SVGUtils.setXY(c, x, y);
			x += bbox.getWidth() + 5;
			bringToTop(c);
		}
		selectionRect.getWidth().getBaseVal().setValue(x - selectionRect.getX().getBaseVal().getValue());
		selectionRect.getHeight().getBaseVal().setValue(bbox.getHeight() + 10);
		selectionRect.getStyle().setVisibility(Visibility.VISIBLE);
	}

	public boolean areCountersOverlapping(SVGElement area, List<SVGElement> counters) {
		OMSVGRect areaBBox = SVGUtils.getBBox(area);
		float spacing = 3;
		Dimention counterDim = getMaxCounterSize(counters);
		Dimention placing = getPlacing(counters, counterDim, areaBBox, spacing);
		float slots = placing.width * placing.height;
		return (slots < counters.size());
	}
	
	public void addGameListener(GameChangeListener listener) {
		displayHandler.addGameListener(listener);
	};
	
	@Override
	public void toggleUnits() {
		Element e = svg.getElementById("units");
		Style style = e.getStyle();
		String current = style.getVisibility();		
		Visibility newValue = !Visibility.HIDDEN.getCssName().equalsIgnoreCase(current) ? Visibility.HIDDEN : Visibility.VISIBLE;
		style.setVisibility(newValue);
	}
}