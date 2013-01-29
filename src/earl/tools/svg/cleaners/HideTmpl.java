package earl.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopyAttrEndAware;

public class HideTmpl extends StreamCopyAttrEndAware {
	private String id = null;
	private String style = null;

	public HideTmpl(XMLStreamWriter writer) {
		super(writer);
	}

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		super.writeStartElement(localName);
		id = null;
		style = null;
	}

	@Override
	protected void onStartElementEnd() throws XMLStreamException {
		if ("tmpl".equals(id)) {
			super.writeAttribute("id", id);
			super.writeAttribute("style", "display:none");
		} else {
			if (id != null) {
				super.writeAttribute("id", id);
			}
			if (style != null) {
				super.writeAttribute("style", style);
			}
		}
	}

	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if (localName.equals("id")) {
			id = value;
		} else if (localName.equals("style")) {
			style = value;
		} else {
			super.writeAttribute(localName, value);
		}
	}
}
