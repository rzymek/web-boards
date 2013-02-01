package earl.tools.svg.cleaners;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopy;

public class SetSVGDimentions extends StreamCopy {
	private int depth = 0;

	public SetSVGDimentions(XMLStreamWriter writer) {
		super(writer);
	}

	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if(depth == 1) {
			if("width".equals(localName) || "height".equals(localName)){
				super.writeAttribute(localName, "100%");
				return; 
			}
		}
		super.writeAttribute(localName, value);
	}
	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		super.writeStartElement(localName);
		if(depth < 5) {
			depth++;
		}
	}

}
