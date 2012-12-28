package earl.tools.svg.cleaners;

import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopyAttrEndAware;

public class ApplyTransform extends StreamCopyAttrEndAware {
	private String tag = null;
	private String transform;
	private String d;

	public ApplyTransform(XMLStreamWriter delegate) {
		super(delegate);
	}

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		super.writeStartElement(localName);
		tag = localName;
		d = null;
		transform = null;
	}

	@Override
	protected void onStartElementEnd() throws XMLStreamException {
		if(transform != null && d != null) {				
			d = applyTranform(transform, d);
			super.writeAttribute("d", d);
			return;
		}else{
			if(transform != null) {
				super.writeAttribute("transform", transform);
			}
			if(d != null) {
				super.writeAttribute("d", d);
			}
		}
	}
	private static final Pattern SUPPORTED_TRANSFORM = Pattern.compile("matrix[(][^)]*[)]");
	private static final Pattern SUPPORTED_D = Pattern.compile("m ([0-9]|[,.\\- ])* z");

	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if("path".equals(tag)) {
			if("transform".equals(localName)) {
				if(SUPPORTED_TRANSFORM.matcher(value).matches()) {//only matrix supported
					transform = value;
					return;
				}
			}else if("d".equals(localName)) {
				if(SUPPORTED_D.matcher(value).matches()) {
					d = value;
					return;
				}
			}
		}
		super.writeAttribute(localName, value);
	}

	protected static String applyTranform(String transform, String d) {
		float[] m = getMatrix(transform);
		d = d.replaceAll("^m ", "").replaceAll(" z$", "");
		float ax1 = 0;
		float ay1 = 0;
		StringBuilder buf = new StringBuilder("m ");
		String[] points = d.split(" ");
		float rx2 = 0;
		float ry2 = 0;
		float last_ax2 = 0;
		float last_ay2 = 0;
		for (String point : points) {
			String[] split = point.split(",");
			float rx1 = Float.parseFloat(split[0]);
			float ry1 = Float.parseFloat(split[1]);
			ax1 += rx1;
			ay1 += ry1;
			float ax2 = m[0] * ax1 + m[2] * ay1 + m[4] * 1;
			float ay2 = m[1] * ax1 + m[3] * ay1 + m[5] * 1;
			
			rx2 = ax2 - last_ax2;
			ry2 = ay2 - last_ay2;
			last_ax2 = ax2;
			last_ay2 = ay2;
			buf.append(String.format("%f,%f ", rx2, ry2));
		}
		buf.append("z");
		return buf.toString();
	}
	
	private static float[] getMatrix(String t) {
		t = t.replace("matrix(", "").replace(")", "");
		String[] split = t.split(",");
		float[] m = new float[split.length];
		for (int i = 0; i < split.length; i++) {
			m[i] = Float.parseFloat(split[i]);
		}
		return m;
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		super.writeEndElement();
		tag = null;
	}
}
