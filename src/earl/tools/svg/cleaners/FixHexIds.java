package earl.tools.svg.cleaners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopy;

public class FixHexIds extends StreamCopy {
	Pattern id = Pattern.compile("([0-9]+)[.]([0-9]+)");
	public FixHexIds(XMLStreamWriter delegate) {
		super(delegate);
	}

	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if (localName.equals("id")) {
			Matcher matcher = id.matcher(value);
			if(matcher.matches()) {
				int x = Integer.parseInt(matcher.group(1));
				int y = Integer.parseInt(matcher.group(2));
				value = String.format("h%02d%02d", x, y);
			}
		}
		super.writeAttribute(localName, value);
	}
}
