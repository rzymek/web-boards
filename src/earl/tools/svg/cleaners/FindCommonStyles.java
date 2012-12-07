package earl.tools.svg.cleaners;

import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopy;

public class FindCommonStyles extends StreamCopy {
	private final Map<String, Integer> count;
	public FindCommonStyles(XMLStreamWriter xsw, Map<String, Integer> count) {
		super(xsw);
		this.count = count;
	}
	
	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if("style".equals(localName)) {
			Integer c = count.get(value);
			if(c == null) {
				c = 0;				
			}
			c = c + 1;
			count.put(value, c);
		}
		super.writeAttribute(localName, value);
	}
}
