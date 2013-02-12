package webboards.client.menu;

import org.vectomatic.dom.svg.impl.SVGSVGElement;

import webboards.client.ClientEngine;
import webboards.client.data.Game;
import webboards.client.data.GameCtx;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.games.scs.bastogne.BastogneSide;

public class EarlClienContext {
	public SVGSVGElement svg;
	public Bastogne game;
	public BastogneSide side;
	public GameCtx ctx;
	public ClientEngine engine;
	public Game initial;
}
