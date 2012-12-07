package earl.tools.svg.cleaners;

import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopy;

public class RemoveNonSVG extends StreamCopy {

	public RemoveNonSVG(XMLStreamWriter delegate) {
		super(delegate);
	}

}
