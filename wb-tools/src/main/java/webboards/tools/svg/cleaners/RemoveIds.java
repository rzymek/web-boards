package webboards.tools.svg.cleaners;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import webboards.tools.svg.utils.StreamCopy;


public class RemoveIds extends StreamCopy {

	public RemoveIds(XMLStreamWriter delegate) {
		super(delegate);
	}

	private final Set<String> ids = new TreeSet<String>();

	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if (localName.equals("id") && value.matches("[a-z]+[0-9]+")) {
			ids.add(value);
			return;
		}
		super.writeAttribute(localName, value);
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		super.writeEndDocument();
		System.out.println("Removed id ("+ids.size()+"): "+ids);
	}
}
