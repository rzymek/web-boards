package earl.tools.svg.cleaners;

import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import earl.tools.svg.utils.StreamCopy;

public class ApplyTransform extends StreamCopy {
	private final boolean nsOut = false;
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
	
	/**<pre>
	 * Result:	 
	 * m 28.814604,1974.8076 -16.322154,-27.5907 15.733122,-27.9307 32.055276,-0.3401 16.322153,27.5907 -15.733122,27.9307 z
	 * 
	 * Source:
	 * m 3915.8045,1067.101 -71.8434,0 -35.9217,-62.2182 35.9217,-62.21817 71.8434,0 35.9217,62.21817 z
	 * matrix(0.44391556,0,0,0.44475269,-1676.4337,1498.8474)
	 */	
	public static void main(String[] args) {
//		double[] m = { 0.44391556, 0, 0, 0.44475269, -1676.4337, 1498.8474 };
		double[] m ={0.9999023,-0.01397816,0.01397816,0.9999023,-50.347011,-49.648104};
//		double[] p = { 3915.8045, 1067.101 };
//		double[] p = {-71.8434,0};
//		double[] p = {28.814604,1974.8076};
		double[] p = {50,50};
		double x = p[0];
		double y = p[1];
		
		double x1 = m[0] * x + m[2] * y + m[4] * 1;
		double y1 = m[1] * x + m[3] * y + m[5] * 1;
//		System.out.println(x1+","+y1);
		
		String t = "matrix(0.44391556,0,0,0.44475269,-1676.4337,1498.8474)";
		String d = "m 3915.8045,1067.101 -71.8434,0 -35.9217,-62.2182 35.9217,-62.21817 71.8434,0 35.9217,62.21817 z";
		d = applyTranform(t, d);
		System.out.println(d);
	}
	private static final Pattern NUMBER=Pattern.compile("[0-9]+([.][0-9]+)?");

	@Override
	public void writeAttribute(String localName, String value) throws XMLStreamException {
		if("path".equals(tag)) {
			if("transform".equals(localName)) {
				if(value.contains("matrix")) {//only matrix supported
					transform = value;
				}
			}else if("d".equals(localName)) {
				d = value;
			}else{
				super.writeAttribute(localName, value);
				return;
			}
			if(transform != null && d != null) {				
				d = applyTranform(transform, d);
				super.writeAttribute("d", d);
				return;
			}
			if("transform".equals(localName) || "d".equals(localName)){
				return;
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
		float lx2 = 0;
		float ly2 = 0;
		for (String point : points) {
			String[] split = point.split(",");
			float rx1 = Float.parseFloat(split[0]);
			float ry1 = Float.parseFloat(split[1]);
			ax1 += rx1;
			ay1 += ry1;
			float ax2 = m[0] * ax1 + m[2] * ay1 + m[4] * 1;
			float ay2 = m[1] * ax1 + m[3] * ay1 + m[5] * 1;
			
			rx2 = ax2 - lx2;
			ry2 = ay2 - ly2;
			lx2 = ax2;
			ly2 = ay2;
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
