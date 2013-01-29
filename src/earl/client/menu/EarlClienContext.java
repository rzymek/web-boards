package earl.client.menu;

import org.vectomatic.dom.svg.impl.SVGSVGElement;

import earl.client.display.BasicDisplayHandler;
import earl.client.display.svg.SVGDisplay;
import earl.client.games.scs.bastogne.Bastogne;
import earl.client.games.scs.bastogne.BastogneSide;

public class EarlClienContext {
	public SVGSVGElement svg;
	public Bastogne game;
	public BastogneSide side;
	public SVGDisplay display;
	public BasicDisplayHandler handler;
}
