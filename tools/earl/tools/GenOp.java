package earl.tools;

import java.io.File;
import java.net.URL;

import earl.client.games.scs.ops.Move;

public class GenOp {
	public static void main(String[] args) {
		URL url = Move.class.getResource(".");
		File dir=  new File(url.getFile());
		String[] listFiles = dir.list();
		String tmpl ="if (%s.class.getName().equals(type)) {\n"+
			"	return new %s();\n" +
			"} else ";
		for (String string : listFiles) {
			String name = string.replace(".class","");
			String s = String.format(tmpl, name, name);
			System.out.print(s);
		}
		System.out.println("{");
		System.out.println("	throw new EarlException(\"Unknow operation type:\" + type);");
		System.out.println("}");
	}
}
