package earl.tools;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GetMapInfo {
	public static void main(String[] args) throws Exception {
		StringBuilder out = new StringBuilder();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File("sources/bastogne/full/bastogne.svg"));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("//g[@id='area1']/path");
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
					if(city)s+="C";
					if(forest)s+="F";
					if(rune)s+="R";
					out.append(id+"="+s+"\n");
				}
			}
		}
		FileWriter fout = new FileWriter("src/bastogne-map.properties");
		fout.append(out);
		fout.close();
	}
}