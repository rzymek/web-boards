package webboards.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import webboards.tools.svg.utils.StreamCopy;


public class OmmitXMLDeclaration extends StreamCopy {
	public OmmitXMLDeclaration(XMLStreamWriter delegate) {
		super(delegate);
	}

	@Override
	public void writeStartDocument() throws XMLStreamException {
	}
	@Override
	public void writeStartDocument(String encoding, String version) throws XMLStreamException {
	}
	@Override
	public void writeStartDocument(String version) throws XMLStreamException {
	}
}
