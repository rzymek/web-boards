package earl.client.menu;

import org.vectomatic.dom.svg.impl.SVGSVGElement;

import earl.client.ClientEngine;
import earl.client.data.GameCtx;
import earl.client.games.scs.bastogne.Bastogne;
import earl.client.games.scs.bastogne.BastogneSide;

public class EarlClienContext {
	public SVGSVGElement svg;
	public Bastogne game;
	public BastogneSide side;
	public GameCtx ctx;
	public ClientEngine engine;
}
