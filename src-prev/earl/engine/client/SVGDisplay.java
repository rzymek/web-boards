package earl.engine.client;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMNodeList;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import earl.client.data.Hex;
import earl.client.data.Counter;
import earl.engine.client.data.Board;
import earl.engine.client.handlers.HexHandler;

public class SVGDisplay extends Display {
	private final OMSVGSVGElement svg;
	public final HexHandler hexHandler = new HexHandler(this);

	private final Board board;

	public SVGDisplay(OMSVGSVGElement svg, Board board) {
		super();
		this.svg = svg;
		this.board = board;
		init();
	}

	protected void areaClicked(ClickEvent event) {
		OMSVGPathElement clickedArea = OMSVGDocument.convert(event.getRelativeElement());
		Hex area = getArea(clickedArea);
		super.areaClicked(area);
	}

	protected void pieceClicked(ClickEvent event) {
		OMSVGImageElement clickedPiece = OMSVGDocument.convert(event.getRelativeElement());
		Counter piece = getPiece(clickedPiece);
		super.pieceClicked(piece);
	}

	private Hex getArea(OMSVGPathElement clickedArea) {
		return null;
	}

	private Counter getPiece(OMSVGImageElement selectedPiece2) {
		return null;
	}

	private void init() {
		initAreas();
		initPieces();
	}

	protected void initAreas() {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				areaClicked(event);
			}
		};
		OMNodeList<OMElement> hexes = svg.getElementById("area").getElementsByTagName("path");
		for (int i = 0; i < hexes.getLength(); ++i) {
			OMElement element = hexes.getItem(i);
			OMSVGPathElement hex = (OMSVGPathElement) element;
			hex.addClickHandler(clickHandler);
		}

		hexes = svg.getElementById("area").getElementsByTagName("g");
		for (int i = 0; i < hexes.getLength(); ++i) {
			OMElement element = hexes.getItem(i);
			OMSVGGElement hex = (OMSVGGElement) element;
			hex.addClickHandler(clickHandler);
		}
	}

	protected void initPieces() {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pieceClicked(event);
			}
		};
		OMNodeList<OMElement> units = svg.getElementById("units").getElementsByTagName("image");
		for (int i = 0; i < units.getLength(); ++i) {
			OMElement element = units.getItem(i);
			if ("image".equals(element.getTagName())) {
				OMSVGImageElement unit = (OMSVGImageElement) element;
				unit.addClickHandler(clickHandler);
			}
		}
	}

}
