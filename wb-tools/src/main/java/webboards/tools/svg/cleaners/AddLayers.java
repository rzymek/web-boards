package webboards.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import webboards.tools.svg.utils.StreamCopy;


public class AddLayers extends StreamCopy {
	private int depth = 0;

	public AddLayers(XMLStreamWriter writer) {
		super(writer);
	}

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		super.writeStartElement(localName);
		depth++;
	}

	@Override
	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		super.writeStartElement(namespaceURI, localName);
		depth++;
	}

	@Override
	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		super.writeStartElement(prefix, localName, namespaceURI);
		depth++;
	}
	
	@Override
	public void writeEndElement() throws XMLStreamException {
		depth--;
		if(depth == 0) {
			writeLayer("traces");
			writeLayer("units");
			writeLayer("markers");
		}
		super.writeEndElement();
	}

	public void writeLayer(String id) throws XMLStreamException {
		super.writeStartElement("g");
		super.writeAttribute("id", id);
		super.writeEndElement();
	}
}
