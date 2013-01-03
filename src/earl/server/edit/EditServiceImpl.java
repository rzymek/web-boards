package earl.server.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.client.display.svg.edit.EditService;

public class EditServiceImpl extends RemoteServiceServlet implements EditService {
	
	@Override
	public void save(Long id, String color, String src) throws Exception {
		System.out.println(src);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity entity = new Entity("edit", id);
		entity.setProperty("color", color);
		entity.setProperty("src", src);
		ds.put(entity);
	}
	
	@Override
	public List<Map<String, String>> load() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Iterable<Entity> results = ds.prepare(new Query("edit")).asIterable();
		for (Entity e : results) {
			HashMap<String, String> map = new HashMap<String, String>();
			for (Entry<String, Object> tmp : e.getProperties().entrySet()) {
				map.put(tmp.getKey(), (String)tmp.getValue());
			}
			map.put("id", String.valueOf(e.getKey().getId()));
			list.add(map);			
		}
		return list;
	}
}
