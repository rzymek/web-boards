package webboards.tools.svg.utils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class StreamCopyAttrEndAware extends StreamCopy {
	private boolean fireStartElementEnd = false;

	protected StreamCopyAttrEndAware(XMLStreamWriter delegate) {
		super(delegate);
	}
	
	private void onStartElement(String localName) throws XMLStreamException {
		fireStartElementEnd();
		fireStartElementEnd = true;	
	}

	private void fireStartElementEnd() throws XMLStreamException {
		if(fireStartElementEnd) {
			onStartElementEnd();
			fireStartElementEnd = false;
		}
	}

	protected abstract void onStartElementEnd() throws XMLStreamException;

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		onStartElement(localName);
		super.writeStartElement(localName);
	}

	@Override
	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		onStartElement(localName);
		super.writeStartElement(namespaceURI, localName);
	}

	@Override
	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		onStartElement(localName);
		super.writeStartElement(prefix, localName, namespaceURI);
	}
	

	@Override
	public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
		fireStartElementEnd();
		super.writeCharacters(text, start, len);
	}

	@Override
	public void writeCData(String data) throws XMLStreamException {
		fireStartElementEnd();
		super.writeCData(data);
	}

	@Override
	public void writeCharacters(String text) throws XMLStreamException {
		fireStartElementEnd();
		super.writeCharacters(text);
	}

	@Override
	public void writeComment(String data) throws XMLStreamException {
		fireStartElementEnd();
		super.writeComment(data);
	}

	@Override
	public void writeEmptyElement(String localName) throws XMLStreamException {
		fireStartElementEnd();
		super.writeEmptyElement(localName);
	}

	@Override
	public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
		fireStartElementEnd();
		super.writeEmptyElement(namespaceURI, localName);
	}

	@Override
	public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		fireStartElementEnd();
		super.writeEmptyElement(prefix, localName, namespaceURI);
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		fireStartElementEnd();
		super.writeEndElement();
	}
}
