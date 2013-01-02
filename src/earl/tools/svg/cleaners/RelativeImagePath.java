package earl.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopy;

public class RelativeImagePath extends StreamCopy {
	public RelativeImagePath(XMLStreamWriter delegate) {
		super(delegate);
	}
	
	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		value = makeRelative(localName, value);
		super.writeAttribute(localName, value);		
	}
	@Override
	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		value = makeRelative(localName, value);
		super.writeAttribute(namespaceURI, localName, value);
	}
	@Override
	public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
			throws XMLStreamException {
		value = makeRelative(localName, value);
		super.writeAttribute(prefix, namespaceURI, localName, value);
	}
	
	private String makeRelative(String localName, String value) {
		if("href".equals(localName)) {
			final String dir = "/war/bastogne/";
			int idx = value.indexOf(dir);
			if(idx == -1){
				return value;
			}
			value = value.substring(idx+dir.length());
			System.out.println("img:"+value);
		}
		return value;
	}
}
