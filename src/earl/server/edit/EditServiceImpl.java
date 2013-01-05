package earl.server.edit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.client.display.svg.edit.EditService;

public class EditServiceImpl extends RemoteServiceServlet implements EditService {
	private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

	public void initialize() throws IOException {
		InputStream in = getServletContext().getResourceAsStream("/roads.txt");
		List<String> lines = IOUtils.readLines(in);
		for (String line : lines) {
			System.out.println(line);
			String[] data = line.split("\\t");
			Long id = Long.valueOf(data[0]);
			save(id, data[1], data[2]);
		}
	}

	@Override
	public void save(Long id, String color, String src) {
		System.out.println(src);
		Entity entity = new Entity("edit", id);
		entity.setProperty("color", color);
		entity.setProperty("src", src);
		ds.put(entity);
	}

	@Override
	public List<Map<String, String>> load() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Iterable<Entity> results = ds.prepare(new Query("edit")).asIterable();
		for (Entity e : results) {
			HashMap<String, String> map = new HashMap<String, String>();
			for (Entry<String, Object> tmp : e.getProperties().entrySet()) {
				map.put(tmp.getKey(), (String) tmp.getValue());
			}
			map.put("id", String.valueOf(e.getKey().getId()));
			list.add(map);
		}
		return list;
	}
	
	@Override
	public String dump() {
		StringBuilder buf = new StringBuilder();
		Iterable<Entity> results = ds.prepare(new Query("edit")).asIterable();
		for (Entity e : results) {
			String s = String.format("%d	%s	%s", e.getKey().getId(), e.getProperty("color"), e.getProperty("src"));
			System.out.println(s);
			buf.append(s).append("\n");
		}
		return buf.toString();
	}
}