package earl.client.display;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import earl.client.data.Counter;
import earl.client.data.Hex;

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
		//TODO
	}
	
	@Override
	public void counterDeselected(Counter selectedPiece) {
		Element c = DOM.getElementById(selectedPiece.getId());
		c.setAttribute("style", "opacity: 1");
	}
	
	@Override
	public void counterSelected(Counter selectedPiece) {
		Element c = DOM.getElementById(selectedPiece.getId());
		c.setAttribute("style", "opacity: 0.8");
	}

}
