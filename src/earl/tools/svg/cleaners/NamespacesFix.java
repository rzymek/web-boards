package earl.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopy;

public class NamespacesFix extends StreamCopy {
	private boolean nsOut = false;

	public NamespacesFix(XMLStreamWriter delegate) {
		super(delegate);
	}

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		delegate.writeStartElement(localName);
		if(!nsOut) {
			delegate.writeNamespace("", "http://www.w3.org/2000/svg");
			delegate.writeNamespace("xlink", "http://www.w3.org/1999/xlink");
			nsOut = true;
		}
	}
	@Override
	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
	}
}
