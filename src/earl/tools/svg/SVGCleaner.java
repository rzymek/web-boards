package earl.tools.svg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import earl.tools.svg.cleaners.FindCommonStyles;
import earl.tools.svg.cleaners.NamespacesFix;
import earl.tools.svg.cleaners.RelativeImagePath;
import earl.tools.svg.cleaners.RemoveHexColor;
import earl.tools.svg.cleaners.RemoveIds;

public class SVGCleaner {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		TransformerFactory factory = TransformerFactory.newInstance();		
		Transformer transformer = factory.newTransformer(new StreamSource("clean.xslt"));
		File out = new File("../earl/war/bastogne/bastogne.svg");
		File in = new File("../earl/sources/bastogne-orig.svg");

		ByteArrayOutputStream buf = new ByteArrayOutputStream(); 
		Result run1Result = new StreamResult(buf);
		XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(run1Result);
		
		Map<String, Integer> count = new HashMap<String, Integer>();
		writer = new FindCommonStyles(writer, count);		
		writer = new NamespacesFix(writer);
		writer = new RemoveIds(writer);
//		writer = new OmmitXMLDeclaration(writer);
		writer = new RemoveHexColor(writer);
		writer = new RelativeImagePath(writer);
		
		Source source = new StreamSource(in);
		Result result = new StAXResult(writer);
		transformer.transform(source, result);
		
		ByteArrayInputStream run2in = new ByteArrayInputStream(buf.toByteArray());
		Transformer run2 = factory.newTransformer();
		run2.transform(new StreamSource(run2in), new StreamResult(out));
		
		dump(count);
		System.out.println("Saved: "+(100 - (100 * out.length() / in.length())) + "% in " + (System.currentTimeMillis() - start) + "ms");		
		System.out.println(IOUtils.toString(new FileReader(out)).substring(0, 3000));		
	}

	private static void dump(Map<String, Integer> count) {
		List<String> out = new ArrayList<String>(count.size());
		for (Entry<String, Integer> e : count.entrySet()) {
			if(e.getValue() > 1) {
				out.add(e.getValue()+" = "+e.getKey());
			}
		}
		Collections.sort(out);
		for (String string : out) {
			System.out.println(string);
		}
		
	}

	@Test
	public void test() throws Exception {
		main(new String[0]);
	}
}
