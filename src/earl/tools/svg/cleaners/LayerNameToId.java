package earl.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopyAttrEndAware;

public class LayerNameToId extends StreamCopyAttrEndAware {
	private int depth=0;
	private String currentElement;

	public LayerNameToId(XMLStreamWriter delegate) {
		super(delegate);
	}

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		// TODO Auto-generated method stub
		super.writeStartElement(localName);
		currentElement = localName;
		depth++;
	}
	@Override
	public void writeEndElement() throws XMLStreamException {
		// TODO Auto-generated method stub
		super.writeEndElement();
		depth--;
	}
	@Override
	protected void onStartElementEnd() throws XMLStreamException {
	}

	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		super.writeAttribute(localName, value);
	}

	@Override
	public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
			throws XMLStreamException {
		if("g".equals(currentElement) && depth == 1 && "label".equals(localName)) {
			System.out.println(localName);
		}
		super.writeAttribute(prefix, namespaceURI, localName, value);
	}
}
