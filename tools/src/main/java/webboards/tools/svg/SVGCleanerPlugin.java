package webboards.tools.svg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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

@Mojo(name = "svgclean", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class SVGCleanerPlugin extends AbstractMojo {
	private static final TransformerFactory factory = new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl();
	@Parameter(required = true)
	public File in;
	@Parameter(required = true)
	public File out;
	@Parameter
	public File htmlTemplate;
	@Parameter
	public File htmlOut;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			long start = System.currentTimeMillis();
			out.getParentFile().mkdirs();
			htmlOut.getParentFile().mkdirs();
			doExecute();
			getLog().info("Saved: " + (100 - (100 * out.length() / in.length())) + "% in " + (System.currentTimeMillis() - start) + "ms");
		} catch (MojoFailureException e) {
			throw e;
		} catch (Exception e) {
			throw new MojoExecutionException(e.toString(), e);
		}
	}

	private void doExecute() throws Exception {
		InputStream xslt = SVGCleanerPlugin.class.getResourceAsStream("/clean.xslt");
		Transformer transformer = factory.newTransformer(new StreamSource(xslt));
		xslt.close();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

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
//		writer = new SetSVGDimentions(writer);
		writer = new RemoveBoardHref(writer);

		Source source = new StreamSource(in);
		Result result = new StAXResult(writer);
		transformer.transform(source, result);

		ByteArrayInputStream run2in = new ByteArrayInputStream(buf.toByteArray());
		Transformer run2 = factory.newTransformer();
		run2.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		run2.transform(new StreamSource(run2in), new StreamResult(out));

		embedInHtml(out);

		dump(count);
		if (getLog().isDebugEnabled()) {
			String string = FileUtils.readFileToString(out);
			getLog().debug(string.substring(0, Math.min(string.length(), 3000)));
		}
	}

	private void embedInHtml(File svgResult) throws MojoFailureException, IOException {
		if (htmlTemplate == null) {
			if (htmlOut == null) {
				throw new MojoFailureException("htmlOut parameter is required if htmlTemplate is set");
			}
		}
		String indexTmpl = FileUtils.readFileToString(htmlTemplate);
		String svg = FileUtils.readFileToString(svgResult);
		String indexSrc = indexTmpl.replace("${svg}", svg);
		FileUtils.writeStringToFile(htmlOut, indexSrc);
	}

	private void dump(Map<String, Integer> count) {
		if (!getLog().isDebugEnabled()) {
			return;
		}
		List<String> out = new ArrayList<String>(count.size());
		for (Entry<String, Integer> e : count.entrySet()) {
			if (e.getValue() > 1) {
				out.add(e.getValue() + " = " + e.getKey());
			}
		}
		Collections.sort(out);
		for (String string : out) {
			getLog().debug(string);
		}
	}

}
