package webboards.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import webboards.tools.svg.utils.StreamCopyAttrEndAware;


public class RemoveBoardHref extends StreamCopyAttrEndAware {
	private static final String XLINK = "http://www.w3.org/1999/xlink";
	private String id = null;
	private String href = null;

	public RemoveBoardHref(XMLStreamWriter writer) {
		super(writer);
	}

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		super.writeStartElement(localName);
		id = null;
		href = null;
	}

	@Override
	protected void onStartElementEnd() throws XMLStreamException {
		if ("img".equals(id)) {
			super.writeAttribute("id", id);
			href = null;
		} else {
			if (id != null) {
				super.writeAttribute("id", id);
			}
			if (href != null) {
				super.writeAttribute("xlink", XLINK, "href", href);
			}
		}
	}

	@Override
	public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
		if(XLINK.equals(namespaceURI) && localName.equals("href")) {
			href = value;
		}else{
			super.writeAttribute(prefix, namespaceURI, localName, value);
		}
	}
	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if (localName.equals("id")) {
			id = value;
		} else {
			super.writeAttribute(localName, value);
		}
	}
}
