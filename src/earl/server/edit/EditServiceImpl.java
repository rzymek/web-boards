package earl.server.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.client.display.svg.edit.EditService;

public class EditServiceImpl extends RemoteServiceServlet implements EditService {
	private final List<Entity> results = new ArrayList<Entity>();

	public EditServiceImpl() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<Entity> list = ds.prepare(new Query("edit")).asList(FetchOptions.Builder.withDefaults());
		results.addAll(list);
	}
	
	@Override
	public void save(Long id, String color, String src) throws Exception {
		System.out.println(src);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity entity = new Entity("edit", id);
		entity.setProperty("color", color);
		entity.setProperty("src", src);
		ds.put(entity);
		results.add(entity);
	}
	
	@Override
	public List<Map<String, String>> load() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
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
