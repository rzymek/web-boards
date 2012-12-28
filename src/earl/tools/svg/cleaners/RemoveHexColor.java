package earl.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopyAttrEndAware;

public class RemoveHexColor extends StreamCopyAttrEndAware {
//	private static final String HEX_STYLE = "fill:#fffff;fill-opacity:0;stroke:#000000;stroke-opacity:1";
	
	private String style = null;
	private String id = null;

	public RemoveHexColor(XMLStreamWriter delegate) {
		super(delegate);
	}
	
	@Override
	protected void onStartElementEnd() throws XMLStreamException {
		if(style != null) {
			if(id != null) {
				super.writeAttribute("class","h");
				//super.writeAttribute("style", HEX_STYLE);
			}else{
				super.writeAttribute("style", style);
			}
		}
		style = null;
		id = null;
	}
	
	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if ("style".equals(localName)) {
			style = value;
			return;
		}else if ("id".equals(localName)) {
			if (value.matches("[0-9]+[.][0-9]+")) {
				id = value;
			}else{
				id = null;
			}
		}
		super.writeAttribute(localName, value);
	}
}
