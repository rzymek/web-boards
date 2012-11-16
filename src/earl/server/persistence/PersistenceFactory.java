package earl.server.persistence;

import com.google.appengine.api.datastore.DatastoreServiceFactory;


public class PersistenceFactory {
	public static Persistence get() {		
		return new DataStorePersistence(DatastoreServiceFactory.getDatastoreService());
	}

}
