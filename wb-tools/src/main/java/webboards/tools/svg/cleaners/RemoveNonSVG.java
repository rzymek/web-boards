package webboards.tools.svg.cleaners;

import javax.xml.stream.XMLStreamWriter;

import webboards.tools.svg.utils.StreamCopy;


public class RemoveNonSVG extends StreamCopy {

	public RemoveNonSVG(XMLStreamWriter delegate) {
		super(delegate);
	}

}
