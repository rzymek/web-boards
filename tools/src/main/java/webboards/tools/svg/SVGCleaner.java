package webboards.tools.svg;

import java.io.File;

public class SVGCleaner {
	private static final String BASEDIR = "../engine/";
	private static final String WEBDIR = BASEDIR + "src/main/webapp/";
	private static final String SRCDIR = BASEDIR + "src/main/svg/";

	public static void main(String[] args) throws Exception {
		SVGCleanerPlugin plugin = new SVGCleanerPlugin();
		plugin.in = new File(SRCDIR + "bastogne-orig.svg");
		plugin.out = new File(WEBDIR + "bastogne/bastogne.svg");
		plugin.htmlTemplate = new File(SRCDIR + "index.html");
		plugin.htmlOut = new File(WEBDIR + "bastogne/index.html");
		plugin.execute();
	}

}
