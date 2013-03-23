package webboards.tools.mapinfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GetMapInfo { 
	public static void main(String[] args) throws Exception {
		new GetMapInfo().generateMapInfo();
	}


	public void generateMapInfo() throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {
		StringBuilder out = new StringBuilder();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File("../webboards/src/main/svg/bastogne-orig.svg"));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("//g[@id='area']/path");
		NodeList result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		Pattern regex = Pattern.compile("fill:#(.{2})(.{2})(.{2})");
		for (int i = 0; i < result.getLength(); i++) {
			Element e = (Element) result.item(i);
			String id = e.getAttribute("id");
			String style = e.getAttribute("style");
			Matcher matcher = regex.matcher(style);
			if(matcher.find()){
				String r = matcher.group(1);
				String g = matcher.group(2);
				String b = matcher.group(3);
				boolean city = "ff".equals(r);
				boolean forest = "ff".equals(g);
				boolean rune = "ff".equals(b);
				if(city || forest || rune) {
					String s="";
					if(city)  s=append(s, "HexTraits.CITY");
					if(forest)s=append(s, "HexTraits.FOREST");
					if(rune)  s=append(s, "HexTraits.RUNE");
					String[] coords = id.split("[.]");
					int x = Integer.parseInt(coords[0]);
					int y = Integer.parseInt(coords[1]);
					out.append("		hexes["+x+"]["+y+"] = new SCSHex("+s+");\n");
				}
			}
		}
		System.out.println(out);
		String tmpl = FileUtils.readFileToString(new File("MapTraits.tmpl"));
		String src = tmpl.replace("${content}", out);
		FileWriter fout = new FileWriter("../webboards/src/main/java/webboards/client/games/scs/bastogne/MapTraits.java");
		fout.append(src);
		fout.close();
	}


	private String append(String s, String v) {
		if(s.isEmpty()){
			return v;
		}else{
			return s+", "+v;
		}
	}
}