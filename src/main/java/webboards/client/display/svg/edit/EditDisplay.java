package webboards.client.display.svg.edit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.impl.SVGElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import webboards.client.data.Board;
import webboards.client.display.VisualCoords;
import webboards.client.display.svg.SVGDisplay;
import webboards.client.games.Hex;
import webboards.client.games.Position;
import webboards.client.utils.AbstractCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class EditDisplay extends SVGDisplay implements MouseMoveHandler, KeyPressHandler, MouseDownHandler {
	private String color = "black";
	private long id = 1;
	private Element source = null;
	private final Stack<Element> path = new Stack<Element>();
	private final OMSVGDocument doc;

	public EditDisplay(SVGSVGElement svg) {
		super(svg);
		doc = OMElement.convert(svg.getOwnerDocument());
		RootPanel.get().addDomHandler(this, KeyPressEvent.getType());
		loadAll();
	}

	@Override
	protected void initAreas(Board board) {
		SVGElement area = (SVGElement) svg.getElementById("area");
		NodeList<Element> nodeList = area.getElementsByTagName("path");
		for (int i = 0; i < nodeList.getLength(); ++i) {
			SVGElement item = (SVGElement) nodeList.getItem(i);
			OMElement node = OMElement.convert(item);
			((HasMouseMoveHandlers) node).addMouseMoveHandler(this);
			((HasMouseDownHandlers) node).addMouseDownHandler(this);
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (event.isControlKeyDown()) {
			Element newSource = event.getRelativeElement();
			if (source != newSource && source != null) {
				Hex from = Hex.fromSVGId(source.getId());
				Hex to = Hex.fromSVGId(newSource.getId());
				drawLine(from, to);
				if (path.isEmpty()) {
					path.add(source);
				}
				path.add(newSource);
			}
			setCurrent(newSource);
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (event.isControlKeyDown()) {
			if(path.isEmpty()) {
				String id = event.getRelativeElement().getId();
				mark(Arrays.asList(Hex.fromSVGId(id)));
			}else{
				nextSegment();
			}
		}
	}

	@Override
	public void drawLine(Position from, Position to) {
		drawLine(getCenter(from), getCenter(to), to.getSVGId());
	}

	public void drawLine(VisualCoords start, VisualCoords end, String id) {
		OMSVGLineElement line = doc.createSVGLineElement(start.x, start.y, end.x, end.y);
		line.setId("editline-" + id);
		line.getStyle().setSVGProperty("stroke-width", "5");
		line.getStyle().setSVGProperty("stroke", color);
		line.getStyle().setSVGProperty("opacity", "0.75");
		svg.getElementById("markers").appendChild(line.getElement());
	}

	public void setCurrent(Element newSource) {
		source = newSource;
		mark(Arrays.asList(Hex.fromSVGId(source.getId())));
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		char c = event.getCharCode();
		if (c == 'h' || c == '?') {
			Window.alert("Help:\n" + 
					"q - Undo\n" + 
					"l - load all\n" + 
					"c - clear/reset\n" + 
					"n - save current and start next segment\n" + 
					"t - set color" + "o - open by id\n" + 
					"s - save current\n" +
					"i - init from file");
		} else if (c == 'q') {
			removeLine(path.pop().getId());
			setCurrent(path.peek());
		} else if (c == 'l') {
			loadAll();
		} else if (c == 'c') {
			clearMarkers("markers");
		} else if (c == 'd') {
			status("Dump...");
			EditServiceAsync service = GWT.create(EditService.class);
			service.dump(new AbstractCallback<String>(){
				@Override
				public void onSuccess(String result) {
					status("Dumped");
					show(result);
				}
			});
		} else if (c == 'i') {
			EditServiceAsync service = GWT.create(EditService.class);
			service.initialize(new AbstractCallback<Void>(){
				@Override
				public void onSuccess(Void result) {
					status("Initialized");
				}
			});
		} else if (c == 'n') {
			nextSegment();
		} else if (c == 't') {
			String newName = Window.prompt("Line color", color);
			if (newName != null) {
				color = newName;
				svg.getElementById("editline-" + id).getStyle().setProperty("stroke", color);
			}
		} else if (c == 's') {
			save();
		}
	}

	private void loadAll() {
		status("Load all...");
		clearMarkers("markers");
		EditServiceAsync service = GWT.create(EditService.class);
		service.load(new AbstractCallback<List<Map<String, String>>>() {
			@Override
			public void onSuccess(List<Map<String, String>> results) {
				for (Map<String, String> map : results) {
					color = map.get("color");
					id = Math.max(Long.parseLong(map.get("id")), id);					
					draw(map.get("src"));
				}
				++id;
				status("Loaded all - "+id);
			}
		});
	}

	private void nextSegment() {
		if (path.isEmpty()) {
			return;
		}
		save();
		reset();
		id++;
		status("Saved. New id=" + id);
		mark(new ArrayList<Position>());
	}

	private void reset() {
		source = null;
		path.clear();
	}

	private void draw(String result) {
		reset();
		String[] ids = result.split(" ");
		Hex prev = null;
		for (String id : ids) {
			Hex hex = Hex.fromSVGId(id);
			if (prev != null) {
				drawLine(prev, hex);
			}
			prev = hex;
		}
	}

	private void save() {
		status("Save...");
		EditServiceAsync service = GWT.create(EditService.class);
		StringBuilder src = new StringBuilder();
		for (Element h : path) {
			src.append(h.getId()).append(" ");
		}
		service.save(id, color, src.toString(), new AbstractCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				status("Saved " + id + " in " + color);
			}
		});
	}

	private void status(String string) {
		Window.setTitle(string);
	}

	public void removeLine(String id) {
		svg.getElementById("editline-" + id).removeFromParent();
	}

	@Override
	public void clearMarkers(String layer) {
		super.clearMarkers(layer);
		reset();
	}
	public static native void show(String s) /*-{
		window.open('', 'src', 'width=600,height=600').document.writeln('<pre>'+s+'</pre>');
	}-*/;
}
