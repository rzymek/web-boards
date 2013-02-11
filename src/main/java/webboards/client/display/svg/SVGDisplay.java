package webboards.client.display.svg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNode;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.impl.SVGElement;
import org.vectomatic.dom.svg.impl.SVGImageElement;
import org.vectomatic.dom.svg.impl.SVGPathElement;
import org.vectomatic.dom.svg.impl.SVGRectElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;
import org.vectomatic.dom.svg.impl.SVGTSpanElement;

import webboards.client.data.Board;
import webboards.client.data.CounterInfo;
import webboards.client.data.HexInfo;
import webboards.client.data.ref.CounterId;
import webboards.client.display.BasicDisplay;
import webboards.client.display.Color;
import webboards.client.display.SelectionHandler;
import webboards.client.display.VisualCoords;
import webboards.client.ex.EarlException;
import webboards.client.games.Hex;
import webboards.client.games.Position;
import webboards.client.games.scs.SCSSelectionHandler;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.utils.Browser;
import webboards.client.utils.Utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.dom.client.Text;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;

public class SVGDisplay extends BasicDisplay {
	private static final String STACKS = "wb-stacks";
	protected final SVGSVGElement svg;
	private final SVGRectElement stackSelector;
	private Position showingStackSelector = null;
	private final SelectionHandler handler;
	private List<CounterInfo> stackSelectorContents = Collections.emptyList();
	private Position stackSelectorPosition = null;

	public SVGDisplay(SVGSVGElement svg, BastogneSide side) {
		super(side);
		this.svg = svg;
		SVGRectElement rect = (SVGRectElement) svg.getElementById("selection");
		rect.getStyle().setDisplay(Display.NONE);
		svg.getElementById("units").appendChild(rect);

		stackSelector = (SVGRectElement) svg.getElementById("stack-selector");
		stackSelector.getStyle().setVisibility(Visibility.HIDDEN);
		OMSVGRectElement omstackSelector = OMNode.convert(stackSelector);
		omstackSelector.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				handler.onClicked(stackSelectorContents, stackSelectorPosition);
			}
		});
		svg.getElementById("units").appendChild(stackSelector);
		
		//TODO: move elsewhere:
		handler = new SCSSelectionHandler(ctx);
	}

	protected void hexClicked(final Board board, ClickEvent event) {
		hideStackSelection();
		String id = event.getRelativeElement().getId();
		Hex position = Hex.fromSVGId(id);
		handler.onClicked(position);
	}

	protected void counterClicked(final Board board, ClickEvent event) {
		SVGImageElement clicked = (SVGImageElement) event.getRelativeElement();
		String id = clicked.getId();
		CounterInfo counter = board.getCounter(id);
		List<SVGElement> stack = getStacksWith(counter);
		if(stack == null) {
			handler.onClicked(counter);
		}else{
			handler.onClicked(getCounterInfo(stack), counter.getPosition());
		}
	}

	@Override
	public void showStackSelector(List<CounterInfo> stack, Position position) {
		if(stackSelectorContents == stack) {
			return;
		}
		final int margin = 10;
		CounterInfo openAt = stack.get(stack.size() - 1);
		SVGImageElement img = (SVGImageElement) svg.getElementById(openAt.ref().toString());
		//Show stack selection box:
		stackSelector.getX().getBaseVal().setValue(img.getX().getBaseVal().getValue() - margin);
		stackSelector.getY().getBaseVal().setValue(img.getY().getBaseVal().getValue() - margin);
		stackSelector.getStyle().setVisibility(Visibility.VISIBLE);
		List<SVGElement> gstack = getSVGElements(stack);
		Collections.reverse(gstack);
		double size = Math.sqrt(gstack.size());
		double width = Math.ceil(size);
		double height = Math.floor(size + 0.5);
		Dimention maxCounterSize = getMaxCounterSize(gstack);
		width = margin + (int) width * maxCounterSize.width + margin;
		height = margin + (int) height * maxCounterSize.height + margin;
		stackSelector.getWidth().getBaseVal().setValue((float) width);
		stackSelector.getHeight().getBaseVal().setValue((float) height);
		bringToTop(stackSelector);
		alignStack(stackSelector, gstack);
		showingStackSelector = position;
		getSVGElement(position.getSVGId()).removeAttribute(STACKS);
		updateSelectionRect();
		stackSelectorContents = stack;
		stackSelectorPosition = position;
	}

	private List<SVGElement> getStacksWith(CounterInfo counter) {
		List<List<SVGElement>> stacks = getAllStacks(counter.getPosition());
		for (List<SVGElement> list : stacks) {
			Browser.console("Stack: "+list.size());
			for (SVGElement svgElement : list) {
				Browser.console(svgElement);	
			}
		}
		String id = counter.ref().toString();
		for (List<SVGElement> stack : stacks) {
			if(SVGUtils.findById(stack, id) != null) {
				return stack;
			}
		}
		return null;
	}

	private List<List<SVGElement>> getAllStacks(Position position) {
		SVGElement area = getSVGElement(position.getSVGId());
		String stackRoots = area.getAttribute(STACKS);
		if (Utils.isEmpty(stackRoots)) {
			return Collections.emptyList();
		}
		List<CounterInfo> pieces = ctx.board.getInfo(position).getPieces();
		List<SVGElement> all = getSVGElements(pieces);
		String[] roots = stackRoots.split(" ");
		List<List<SVGElement>> result = new ArrayList<List<SVGElement>>();
		for (String next : roots) {
			List<SVGElement> stack = new ArrayList<SVGElement>();
			for(;;) {
				SVGElement e = SVGUtils.findById(all, next);
				stack.add(e);
				next = e.getAttribute(STACKS);
				if(Utils.isEmpty(next)) {
					break;
				}
			}
			result.add(stack);
		}
		return result;
	}

	@Override
	public void alignStack(Position ref) {
		hideStackSelection();
		SVGElement h = getSVGElement(ref.getSVGId());
		HexInfo hex = ctx.board.getInfo(ref);
		Collection<CounterInfo> stack = hex.getPieces();
		List<SVGElement> svgStack = getSVGElements(stack);
		alignStack(h, svgStack);
		updateSelectionRect();
	}

	private List<CounterInfo> getCounterInfo(Collection<SVGElement> stack) {
		List<CounterInfo> result = new ArrayList<CounterInfo>(stack.size());
		for (SVGElement e : stack) {
			CounterInfo info = ctx.board.getInfo(new CounterId(e.getId()));
			result.add(info);
		}
		return result;
	}

	private List<SVGElement> getSVGElements(Collection<CounterInfo> stack) {
		List<SVGElement> result = new ArrayList<SVGElement>(stack.size());
		for (CounterInfo counter : stack) {
			SVGElement svgCounter = getSVGElement(counter.ref().toString());
			result.add(svgCounter);
		}
		return result;
	}

	private void hideStackSelection() {
		stackSelector.getStyle().setVisibility(Visibility.HIDDEN);
		Position hex = showingStackSelector;
		showingStackSelector = null;
		if (hex != null) {
			alignStack(hex);
		}
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
		int layer = 0;
		int countersOnLayer=0;
		List<String> stackRoots = new ArrayList<String>();
		for (int i = 0; i < counters.size(); i++) {
			SVGElement counter = counters.get(i);
			float cx = startx + x + stackOffset;
			float cy = starty + y + stackOffset;
			SVGUtils.setXY(counter, cx, cy);
			if (layer > 0) {
				String id = counter.getId();
				String stacksWith = counters.get(i - countersOnLayer).getId();
				stackRoots.remove(stacksWith);
				stackRoots.add(id);
				counter.setAttribute(STACKS, stacksWith);
			} else {
				counter.removeAttribute(STACKS);
			}
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
				layer++;
				if(layer == 1) {
					countersOnLayer = i + 1;
				}
			}
		}
		area.setAttribute(STACKS, Utils.toString(stackRoots, " "));
	}

	public void bringToTop(Element c) {
		c.getParentElement().appendChild(c);
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

	@Override
	protected void initAreas(final Board board) {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hexClicked(board, event);
			}
		};
		SVGElement area = (SVGElement) svg.getElementById("area");
		addClickHandler(area.getElementsByTagName("path"), clickHandler);
		addClickHandler(area.getElementsByTagName("g"), clickHandler);
		addClickHandler(area.getElementsByTagName("rect"), clickHandler);
	}

	@Override
	public void createCounter(CounterInfo counter, final Board board) {
		Element tmpl = svg.getElementById("counter");
		SVGImageElement c = (SVGImageElement) tmpl.cloneNode(true);
		String id = counter.ref().toString();
		com.google.gwt.user.client.Element existing = DOM.getElementById(id);
		if (existing != null) {
			String msg = "Element with id=" + id + " aleady exists:" + existing;
			Browser.console(msg);
			Browser.console(existing);
			throw new RuntimeException(msg);
		}
		c.setId(id);
		c.getHref().setBaseVal(counter.getState());
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				counterClicked(board, event);
			}
		};
		OMSVGImageElement img = OMElement.convert(c);
		img.addClickHandler(clickHandler);
		svg.getElementById("units").appendChild(c);
	}

	private void addClickHandler(NodeList<Element> nodeList, ClickHandler clickHandler) {
		for (int i = 0; i < nodeList.getLength(); ++i) {
			SVGElement item = (SVGElement) nodeList.getItem(i);
			SVGUtils.addClickHandler(item, clickHandler);
		}
	}

	@Override
	public void select(CounterInfo counter) {
		hideStackSelection();
		handler.onSelect(counter);
		if(!handler.canSelect(counter)) {
			counter = null;
		}
		CounterInfo last = ctx.selected;
		ctx.selected = counter;
		if(last != null && counter == null) {
			//deselect
			alignStack(last.getPosition()); //will call updateSelectionRect
		}else{
			updateSelectionRect();
		}
	}

	private void updateSelectionRect() {
		SVGRectElement rect = (SVGRectElement) svg.getElementById("selection");
		if (ctx.selected == null) {
			rect.getStyle().setVisibility(Visibility.HIDDEN);
		} else {
			SVGImageElement c = (SVGImageElement) svg.getElementById(ctx.selected.ref().toString());
			rect.getStyle().setVisibility(Visibility.VISIBLE);
			rect.getX().getBaseVal().setValue(c.getX().getBaseVal().getValue());
			rect.getY().getBaseVal().setValue(c.getY().getBaseVal().getValue());
			rect.getStyle().setDisplay(Display.BLOCK);
			bringToTop(c);
			c.getParentElement().insertBefore(rect, c);
		}
	}

	private SVGElement getSVGElement(String id) {
		SVGElement e = (SVGElement) svg.getElementById(id);
		if (e == null) {
			throw new EarlException("svg element [" + id + "] not found");
		}
		return e;
	}

	@Override
	public VisualCoords getCenter(Position to) {
		return getCenter(to.getSVGId());
	}

	private VisualCoords getCenter(String id) {
		SVGElement element = getSVGElement(id);
		OMSVGPoint center = SVGUtils.getCenter(element);
		return new VisualCoords((int) center.getX(), (int) center.getY());
	}

	private static final String ARROW_ID_PREFIX = "attackArrow";

	@Override
	public void drawLine(Position from, Position to) {
		drawLine(getCenter(from), getCenter(to));
	}

	public void drawLine(VisualCoords start, VisualCoords end) {
		SVGPathElement arrow = (SVGPathElement) svg.getElementById("trace");
		arrow.getStyle().setProperty("pointerEvents", "none");
		arrow = (SVGPathElement) arrow.cloneNode(true);
		OMSVGPathSegList seg = arrow.getPathSegList();
		seg.replaceItem(arrow.createSVGPathSegMovetoAbs(start.x, start.y), 0);
		seg.replaceItem(arrow.createSVGPathSegLinetoAbs(end.x, end.y), 1);
		shortenArrow(arrow, seg);
		svg.getElementById("traces").appendChild(arrow);
	}

	@Override
	public void drawArrow(Position from, Position to, String id, Color color) {
		drawArrow(getCenter(from.getSVGId()), getCenter(to.getSVGId()), id, color);
	}

	public void drawArrow(VisualCoords start, VisualCoords end, String id, Color color) {
		id = ARROW_ID_PREFIX + "_" + id;
		SVGPathElement arrow = (SVGPathElement) svg.getElementById(id);
		if (arrow == null) {
			arrow = (SVGPathElement) svg.getElementById(ARROW_ID_PREFIX);
			arrow.getStyle().setProperty("pointerEvents", "none");
			arrow = (SVGPathElement) arrow.cloneNode(true);
			arrow.setId(id);
		}
		OMSVGPathSegList seg = arrow.getPathSegList();
		seg.replaceItem(arrow.createSVGPathSegMovetoAbs(start.x, start.y), 0);
		seg.replaceItem(arrow.createSVGPathSegLinetoAbs(end.x, end.y), 1);
		shortenArrow(arrow, seg);
		if(color != null) {
			arrow.getStyle().setProperty("stroke", color.toString());
		}
		Browser.console(arrow);
		Browser.console(arrow.getAttribute("d"));
		svg.getElementById("markers").appendChild(arrow);
	}

	@Override
	public void clearArrow(Position from) {
		String id = getArrowId(from);
		removeElement(id);
	}

	private void removeElement(String id) {
		Element e = svg.getElementById(id);
		if (e != null) {
			e.removeFromParent();
		}
	}

	private String getArrowId(Position from) {
		return ARROW_ID_PREFIX + "_" + from.getSVGId();
	}

	protected void clearMarkers(String layerId) {
		Element markers = svg.getElementById(layerId);
		while (markers.hasChildNodes()) {
			markers.removeChild(markers.getLastChild());
		}
	}

	@Override
	public void drawOds(VisualCoords center, String text, String id) {		
		drawFromTemplate(center, "target", text, "ods"+id);
	}

	private void drawFromTemplate(VisualCoords center, String templateId, String text, final String id) {
		SVGElement target = (SVGElement) svg.getElementById(id);
		if (target == null) {
			target = (SVGElement) svg.getElementById(templateId);
			target = (SVGElement) target.cloneNode(true);
			target.setId(id);
			svg.getElementById("markers").appendChild(target);
		}
		target.getStyle().setProperty("pointerEvents", "none");
		target.getStyle().setVisibility(Visibility.VISIBLE);
		SVGTSpanElement tspan = (SVGTSpanElement) target.getElementsByTagName("tspan").getItem(0);
		Text item = (Text) tspan.getChildNodes().getItem(0);
		item.setNodeValue(text);
		target.setAttribute("transform", "translate(" + center.x + ", " + center.y + ")");
		bringToTop(target);
	}

	@Override
	public void clearOds(String id) {
		SVGElement target = (SVGElement) svg.getElementById("ods"+id);
		if (target != null) {
			target.removeFromParent();
		}
	}

	/**
	 * Shortens arrow point by half the hex size (bbox height)
	 */
	protected void shortenArrow(SVGPathElement arrow, OMSVGPathSegList seg) {
		float w = 30;//SVGUtils.getBBox(end).getHeight()/2;
		float totalLength = arrow.getTotalLength();
		OMSVGPoint p = arrow.getPointAtLength(totalLength - w);
		seg.replaceItem(arrow.createSVGPathSegLinetoAbs(p.getX(), p.getY()), 1);
	}

	@Override
	public void update(CounterId counter, String state) {
		SVGImageElement img = (SVGImageElement) getSVGElement(counter.toString());
		img.getHref().setBaseVal(state);
	}

	@Override
	public void mark(Collection<? extends Position> hexes) {
		StringBuilder buf = new StringBuilder();
		for (Position hex : hexes) {
			if (buf.length() != 0) {
				buf.append(",");
			}
			buf.append("#").append(hex.getSVGId());
		}
		buf.append("{fill:#0000ff;fill-opacity:0.1}");
		svg.getElementById("dyncss").setInnerText(buf.toString());
	}

	@Override
	public void clearMarks() {
		svg.getElementById("dyncss").setInnerText("");
	}

	@Override
	public void showResults(VisualCoords center, String result) {
		final String id = "boom@" + center.x + "_" + center.y;
		drawFromTemplate(center, "boom", result, id);
	}

	@Override
	public void clearResults(VisualCoords center) {
		final String id = "boom@" + center.x + "_" + center.y;
		removeElement(id);
	}

	@Override
	public void clearTraces() {
		clearMarkers("traces");
	}
}
