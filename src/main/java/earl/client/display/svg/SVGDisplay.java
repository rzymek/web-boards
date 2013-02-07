package earl.client.display.svg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.impl.SVGElement;
import org.vectomatic.dom.svg.impl.SVGImageElement;
import org.vectomatic.dom.svg.impl.SVGPathElement;
import org.vectomatic.dom.svg.impl.SVGRectElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;
import org.vectomatic.dom.svg.impl.SVGTSpanElement;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.dom.client.Text;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.data.Identifiable;
import earl.client.display.BasicDisplay;
import earl.client.display.Position;
import earl.client.ex.EarlException;
import earl.client.games.HexXY;
import earl.client.games.Ref;
import earl.client.games.scs.SCSCounter;
import earl.client.ops.Operation;
import earl.client.utils.Browser;

public class SVGDisplay extends BasicDisplay {
	protected final SVGSVGElement svg;
	private final SVGRectElement stackSelector;
	private Ref showingStackSelector = null;
	
	public SVGDisplay(SVGSVGElement svg) {
		this.svg = svg;
		SVGRectElement rect = (SVGRectElement) svg.getElementById("selection");
		rect.getStyle().setDisplay(Display.NONE);
		svg.getElementById("units").appendChild(rect);

		stackSelector = (SVGRectElement) svg.getElementById("stack-selector");
		stackSelector.getStyle().setVisibility(Visibility.HIDDEN);
		svg.getElementById("units").appendChild(stackSelector);
	}
	
	protected void hexClicked(final Board board, ClickEvent event) {
		hideStackSelection();
		String id = event.getRelativeElement().getId();
		HexXY position = HexXY.fromSVGId(id);
		Hex hex = board.get(position);
		Operation op = hex.onClicked(ctx, position);
		process(op);
	}

	protected void counterClicked(final Board board, ClickEvent event) {		
		String id = event.getRelativeElement().getId();
		SCSCounter counter = (SCSCounter) board.getCounter(id);
		if(ctx.selected == counter) {
			select(null);
			return;
		}
		SVGImageElement img = (SVGImageElement) svg.getElementById(counter.getId());
		String attribute = img.getAttribute("earl-stacks");
		if(attribute == null || attribute.isEmpty()) {
			hideStackSelection();
			select(counter);
			return;
		}
		Ref position = counter.getPosition();
		stackSelector.getX().getBaseVal().setValue(img.getX().getBaseVal().getValue() - 10);
		stackSelector.getY().getBaseVal().setValue(img.getY().getBaseVal().getValue() - 10);
		stackSelector.getStyle().setVisibility(Visibility.VISIBLE);
		List<SVGElement> gstack = getGraphicalStack(counter);		
		double size = Math.sqrt(gstack.size());
		double width = Math.ceil(size);
		double height = Math.floor(size+0.5);
		width = 10 + (int)width * img.getWidth().getBaseVal().getValue() + 10;
		height= 10 + (int)height * img.getHeight().getBaseVal().getValue() + 10;
		stackSelector.getWidth().getBaseVal().setValue((float) width);
		stackSelector.getHeight().getBaseVal().setValue((float) height);		
		bringToTop(stackSelector);
		alignStack(stackSelector, gstack);
		showingStackSelector = position;
		
		for (SVGElement e : gstack) {
			e.setAttribute("class","s");
		}
	}
	
	private List<SVGElement> getGraphicalStack(SCSCounter counter) {
		List<SVGElement> stack = new ArrayList<SVGElement>();
		SVGImageElement img = (SVGImageElement) svg.getElementById(counter.getId());
		for(;;) {
			stack.add(img);
			String next = img.getAttribute("earl-stacks");
			if(next == null || next.isEmpty())  {
				break;				
			}
			img = (SVGImageElement) svg.getElementById(next);
		}
		return stack;
	}

	@Override
	public void alignStack(Ref ref) {
		hideStackSelection();
		SVGElement h = getSVGElement(ref.getSVGId());
		Hex hex = ctx.board.get(ref);
		Collection<Counter> stack = hex.getStack();
		List<SVGElement> svgStack = getSVGElements(stack);
		alignStack(h, svgStack);
		select(ctx.selected); //update position of selection rect
	}

	private List<SVGElement> getSVGElements(Collection<Counter> stack) {
		List<SVGElement> result = new ArrayList<SVGElement>(stack.size());
		for (Counter counter : stack) {
			SVGElement svgCounter = getSVGElement(counter.getId());
			result.add(svgCounter);
		}
		return result;
	}

	private void hideStackSelection() {
		stackSelector.getStyle().setVisibility(Visibility.HIDDEN);
		Ref hex = showingStackSelector;
		showingStackSelector = null;
		if(hex != null) {
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
		for (int i = 0; i < counters.size(); i++) {
			SVGElement counter = counters.get(i);
			float cx = startx + x + stackOffset;
			float cy = starty + y + stackOffset;
			SVGUtils.setXY(counter, cx, cy);
			if(layer > 0) {
				String stacksWith = counters.get(i - i/layer).getId();
				counter.setAttribute("class","s");
				counter.setAttribute("earl-stacks", stacksWith);
			}else{				
				counter.setAttribute("class","c");
				counter.removeAttribute("earl-stacks");
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
			}
		}
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
	protected void createCounter(Counter counter, final Board board) {
		Element tmpl = svg.getElementById("counter");
		SVGImageElement c = (SVGImageElement) tmpl.cloneNode(true);
		String id = counter.getId();
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
		SVGUtils.addClickHandler(c, clickHandler);
		svg.getElementById("units").appendChild(c);
	}

	private void addClickHandler(NodeList<Element> nodeList, ClickHandler clickHandler) {
		for (int i = 0; i < nodeList.getLength(); ++i) {
			SVGElement item = (SVGElement) nodeList.getItem(i);
			SVGUtils.addClickHandler(item, clickHandler);
		}
	}

	@Override
	public void select(Counter counter) {
		ctx.selected = counter;
		SVGRectElement rect = (SVGRectElement) svg.getElementById("selection");
		if (counter == null) {
			rect.getStyle().setVisibility(Visibility.HIDDEN);
		} else {
			SVGImageElement c = (SVGImageElement) svg.getElementById(counter.getId());
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
	public Position getCenter(Ref to) {
		return getCenter(to.getSVGId());
	}

	private Position getCenter(String id) {
		SVGElement element = getSVGElement(id);
		OMSVGPoint center = SVGUtils.getCenter(element);
		return new Position((int) center.getX(), (int) center.getY());
	}

	private static final String ARROW_ID_PREFIX = "attackArrow";
	
	@Override
	public void drawLine(Ref from, Ref to) {
		drawLine(getCenter(from), getCenter(to));
	}

	public void drawLine(Position start, Position end) {
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
	public void drawArrow(Ref from, Ref to, String id) {
		drawArrow(getCenter(from.getSVGId()), getCenter(to.getSVGId()), id);
	}
	
	@Override
	public void drawArrow(Position start, Position end, String id) {
		id = ARROW_ID_PREFIX+"_"+id;
		SVGPathElement arrow = (SVGPathElement) svg.getElementById(id);
		if(arrow == null) {
			arrow = (SVGPathElement) svg.getElementById(ARROW_ID_PREFIX);
			arrow.getStyle().setProperty("pointerEvents", "none");
			arrow = (SVGPathElement) arrow.cloneNode(true);
			arrow.setId(id);
		}
		OMSVGPathSegList seg = arrow.getPathSegList();
		seg.replaceItem(arrow.createSVGPathSegMovetoAbs(start.x, start.y), 0);
		seg.replaceItem(arrow.createSVGPathSegLinetoAbs(end.x, end.y), 1);
		shortenArrow(arrow, seg);		
		Browser.console(arrow);
		Browser.console(arrow.getAttribute("d"));
		svg.getElementById("markers").appendChild(arrow);
	}

	@Override
	public void clearArrow(Ref from) {
		String id = getArrowId(from);
		removeElement(id);		
	}

	private void removeElement(String id) {
		Element e = svg.getElementById(id);
		if(e != null) {
			e.removeFromParent();
		}
	}

	private String getArrowId(Ref from) {
		return ARROW_ID_PREFIX + "_" + from.getSVGId();
	}
	
	protected void clearMarkers(String layerId) {
		Element markers = svg.getElementById(layerId);
		while(markers.hasChildNodes()) {
			markers.removeChild(markers.getLastChild());
		}
	}
	
	@Override
	public void drawOds(Position center, int[] odds) {
		String text = odds[0]+":"+odds[1];
		final String id = "tg@"+center.x+"_"+center.y;
		drawFromTemplate(center, "target", text, id);
	}

	private void drawFromTemplate(Position center, String templateId, String text, final String id) {
		SVGElement target = (SVGElement) svg.getElementById(id);
		if(target == null) {
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
		target.setAttribute("transform", "translate("+center.x+", "+center.y+")");
		bringToTop(target);
	}

	@Override
	public void clearOds(Position center) {
		final String id = "tg@"+center.x+"_"+center.y;
		SVGElement target = (SVGElement) svg.getElementById(id);
		if(target != null) {
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
	public void update(Identifiable counter, String state) {
		SVGImageElement img = (SVGImageElement) getSVGElement(counter.getId());
		img.getHref().setBaseVal(state);
	}

	@Override
	public void mark(Collection<? extends Ref> hexes) {
		StringBuilder buf = new StringBuilder();
		for (Ref hex : hexes) {
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
	public void showResults(Position center, String result) {
		final String id = "boom@"+center.x+"_"+center.y;
		drawFromTemplate(center, "boom", result, id);
	}
	@Override
	public void clearResults(Position center) {
		final String id = "boom@"+center.x+"_"+center.y;
		removeElement(id);
	}

	public void clearTraces() {
		clearMarkers("traces");		
	}
	
	public static void main(String[] args) {
		System.out.println(4 / 1);
	}
}
