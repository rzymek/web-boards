package earl.client;

import org.vectomatic.dom.svg.impl.SVGSVGElement;

import earl.client.display.handler.BasicDisplayHandler;
import earl.client.display.svg.SVGDisplay;
import earl.client.games.Bastogne;
import earl.client.games.BastogneSide;

public class EarlGUIContext {

	public SVGSVGElement svg;
	public Bastogne game;
	public BastogneSide side;
	public SVGDisplay display;
	public BasicDisplayHandler handler;

}
