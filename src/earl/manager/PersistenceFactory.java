package earl.manager;

import com.google.appengine.api.datastore.DatastoreServiceFactory;

import earl.server.persistence.DataStorePersistence;

public class PersistenceFactory {
	public static Persistence get() {		
		return new DataStorePersistence(DatastoreServiceFactory.getDatastoreService());
	}

}
