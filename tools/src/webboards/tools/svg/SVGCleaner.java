package webboards.tools.svg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import webboards.tools.svg.cleaners.AddLayers;
import webboards.tools.svg.cleaners.ApplyTransform;
import webboards.tools.svg.cleaners.FindCommonStyles;
import webboards.tools.svg.cleaners.FixHexIds;
import webboards.tools.svg.cleaners.HideTmpl;
import webboards.tools.svg.cleaners.NamespacesFix;
import webboards.tools.svg.cleaners.RelativeImagePath;
import webboards.tools.svg.cleaners.RemoveBoardHref;
import webboards.tools.svg.cleaners.RemoveHexColor;
import webboards.tools.svg.cleaners.RemoveIds;
import webboards.tools.svg.cleaners.SetSVGDimentions;

public class SVGCleaner {

	private static final String BASEDIR = "../webboards/";
	private static final String WEBDIR = BASEDIR + "src/main/webapp/";
	private static final String SRCDIR = BASEDIR + "src/main/svg/";

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		TransformerFactory factory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", SVGCleaner.class.getClassLoader());
		Transformer transformer = factory.newTransformer(new StreamSource("clean.xslt"));
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		File out = new File(WEBDIR + "bastogne/bastogne.svg");
		File in = new File(SRCDIR + "bastogne-orig.svg");

		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		Result run1Result = new StreamResult(buf);
		// Result run1Result = new StreamResult(new FileOutputStream(out));
		XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(run1Result);

		Map<String, Integer> count = new HashMap<String, Integer>();
		// called in reverse order

		// clean.xslt needs to be replaced with StreamCopy filters for this to
		// work because otherwise inkscape:label is already removed here
		// writer = new LayerNameToId(writer);
		writer = new HideTmpl(writer);
		writer = new FixHexIds(writer);
		writer = new FindCommonStyles(writer, count);
		writer = new NamespacesFix(writer);
		writer = new RemoveIds(writer);
		writer = new RemoveHexColor(writer);
		writer = new RelativeImagePath(writer);
		writer = new ApplyTransform(writer);
		writer = new AddLayers(writer);
		writer = new SetSVGDimentions(writer);
		writer = new RemoveBoardHref(writer);

		Source source = new StreamSource(in);
		Result result = new StAXResult(writer);
		transformer.transform(source, result);

		ByteArrayInputStream run2in = new ByteArrayInputStream(buf.toByteArray());
		Transformer run2 = factory.newTransformer();
		run2.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		run2.transform(new StreamSource(run2in), new StreamResult(out));

		prepareIndex(out);
		dump(count);
		System.out.println("Saved: " + (100 - (100 * out.length() / in.length())) + "% in " + (System.currentTimeMillis() - start) + "ms");
		String string = FileUtils.readFileToString(out);
		System.out.println(string.substring(0, Math.min(string.length(), 3000)));
	}

	private static void prepareIndex(File out) throws IOException {
		File index = new File(SRCDIR + "index.html");
		File indexOut = new File(WEBDIR+"bastogne/index.html");
		String indexTmpl = FileUtils.readFileToString(index);
		String svg = FileUtils.readFileToString(out);
		String indexSrc = indexTmpl.replace("${svg}", svg);
		FileUtils.writeStringToFile(indexOut, indexSrc);
	}

	private static void dump(Map<String, Integer> count) {
		List<String> out = new ArrayList<String>(count.size());
		for (Entry<String, Integer> e : count.entrySet()) {
			if (e.getValue() > 1) {
				out.add(e.getValue() + " = " + e.getKey());
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
