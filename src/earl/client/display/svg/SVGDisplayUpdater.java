package earl.client.display.svg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vectomatic.dom.svg.impl.SVGElement;
import org.vectomatic.dom.svg.impl.SVGImageElement;

import com.google.gwt.dom.client.Element;

import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.display.DisplayChangeListener;

public class SVGDisplayUpdater implements DisplayChangeListener {
	private final SVGDisplay display;

	public SVGDisplayUpdater(SVGDisplay display) {
		this.display = display;
	}

	@Override
	public void counterMoved(Counter counter, Hex from, Hex to) {
		display.alignStack(to);
	}

	@Override
	public void counterChanged(Counter piece) {
		SVGImageElement c = (SVGImageElement) display.svg.getElementById(piece.getId());
		c.getHref().setBaseVal("units/"+piece.getState());
	}
	
	@Override
	public void alignStack(Hex position) {
		display.alignStack(position);
	}
	
	@Override
	public boolean areCountersOverlapping(Hex hex, List<Counter> stack) {
		SVGElement h = (SVGElement) display.svg.getElementById(hex.getId());
		List<SVGElement> counters = getStack(hex);
		return display.areCountersOverlapping(h, counters);
	}
	
	@Override
	public void showStackSelection(Hex hex) {
		SVGElement c = (SVGElement) display.svg.getElementById(hex.getId());
		Collection<SVGElement> counters = getStack(hex);
		display.showStackSelection(c, counters);
	}

	private List<SVGElement> getStack(Hex hex) {
		List<Counter> stack = hex.getStack();
		List<SVGElement> counters = new ArrayList<SVGElement>(stack.size());
		for (Counter counter : stack) {
			SVGElement e = (SVGElement) display.svg.getElementById(counter.getId());
			counters.add(e);
		}
		return counters;
	}
	
	@Override
	public void counterDeselected(Counter selectedPiece) {
		Element c = display.svg.getElementById(selectedPiece.getId());
		c.setAttribute("style", "opacity: 1");
	}

	@Override
	public void counterSelected(Counter selectedPiece) {
		SVGElement c = (SVGElement)display.svg.getElementById(selectedPiece.getId());
		c.setAttribute("style", "opacity: 0.8");
		display.bringToTop(c);
	}
}
