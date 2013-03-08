package webboards.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import webboards.client.games.scs.bastogne.ArtyType;

public class Units {
	public static void main(String[] args) throws Exception {
		new Units().run();
	}

	private void run() throws IOException {
		String baseDir = "../webboards/src/main/svg/bastogne/units/";
		File front = new File(baseDir, "front");
		File back = new File(baseDir, "back");
		assert front.canRead();
		assert back.canRead();
		Collection<File> backs = new TreeSet<File>(FileUtils.listFiles(back, TrueFileFilter.TRUE, null));
		IOFileFilter filter = new SuffixFileFilter(ImageIO.getReaderFileSuffixes());
		Collection<File> files = FileUtils.listFiles(front, filter, TrueFileFilter.TRUE);
		Pattern info = Pattern.compile("([0-9]+)(_([0-9gyo]+))?-([0-9]+)-([0-9]+)[+](.*)_f.([a-z]{3})");
		List<String> result = new ArrayList<String>();
		for (File file : files) {
			String name = file.getName();
			Matcher matcher = info.matcher(name);
			if (matcher.matches()) {
				String owner = file.getParentFile().getName();
				String attack = matcher.group(1);
				String rangeInfo = matcher.group(3);
				String defence = matcher.group(4);
				String movement = matcher.group(5);
				String unit = matcher.group(6);

				String range = rangeInfo == null ? null : rangeInfo.replaceAll("[gyo]", "");
				ArtyType type = getArtyType(rangeInfo);
				String ext = matcher.group(7);
				File b = new File(back, unit + "_b." + ext);
				String backName = b.exists() ? '"' + b.getName() + '"' : null;
				int steps = backs.remove(b) ? 2 : 1;
				String desc = unit.replaceAll("_", " ");
				unit = unit.replace("-", "_");
				if (backName == null && steps != 1) {
					throw new RuntimeException("inconsistency: " + name);
				}
				String s = String.format("%s_%s(%s,%s,%s,%s,%s,%s,%s,\"%s\",\"%s\",%s)", owner, unit, 
						owner.toUpperCase(), attack, range, type, defence, movement, steps, desc, name, backName);
				result.add(s);
				// System.out.println(owner+":"+attack+":"+range+":"+defence+":"+movement+":"+unit+":"+steps+":"+name);
			} else {
				throw new IOException(file.getName() + " does not match " + info);
			}
		}
		if (!backs.isEmpty()) {
			throw new IOException("Some backs not matched:" + backs.toString().replace(',', '\n'));
		}
		Collections.sort(result);
		for (int i = 0; i < result.size(); i++) {
			System.out.print(result.get(i));
			System.out.println(i == result.size() - 1 ? ";" : ",");
		}
	}

	private ArtyType getArtyType(String rangeInfo) {
		if(rangeInfo == null) {
			return null;
		}
		ArtyType type = null;
		if(rangeInfo.contains("g")) {
			type = ArtyType.GUNS_88;
		}else if(rangeInfo.contains("y")) {
			type = ArtyType.YELLOW;
		}else{
			type = ArtyType.OTHER;
		}
		return type;
	}
}
