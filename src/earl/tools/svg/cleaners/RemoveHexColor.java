package earl.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopy;

public class RemoveHexColor extends StreamCopy {
//	private static final String HEX_STYLE = "fill:#fffff;fill-opacity:0;stroke:#000000;stroke-opacity:1";
	
	private boolean inHex = false;
	private String style = null;

	public RemoveHexColor(XMLStreamWriter delegate) {
		super(delegate);
	}
	
	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if ("style".equals(localName)) {
			style = value;
		}else if ("id".equals(localName)) {
			super.writeAttribute("id", value);
			if (value.matches("[0-9]+[.][0-9]+")) {
				inHex = true;
				setHexStyle();
			}else{
				if (style != null) {
					super.writeAttribute("style", style);
				}
			}
		} else {
			if (inHex && "style".equals(localName)) {
				setHexStyle();
			}else{
				super.writeAttribute(localName, value);
			}
		}
	}

	protected void setHexStyle() throws XMLStreamException {
		super.writeAttribute("class","h");
//		super.writeAttribute("style", HEX_STYLE);
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		inHex = false;
		style = null;
		super.writeEndElement();
	}
}
