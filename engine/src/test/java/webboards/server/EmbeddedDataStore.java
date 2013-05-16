package webboards.server;

import org.junit.rules.ExternalResource;

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

public class EmbeddedDataStore extends ExternalResource {
	private static LocalServiceTestHelper helper;

	@Override 
	protected void before() throws Throwable {
		LocalDatastoreServiceTestConfig cfg = new LocalDatastoreServiceTestConfig();
		LocalBlobstoreServiceTestConfig blobCfg = new LocalBlobstoreServiceTestConfig();
		LocalTaskQueueTestConfig queueCfg = new LocalTaskQueueTestConfig();
		LocalMemcacheServiceTestConfig cacheCfg = new LocalMemcacheServiceTestConfig();
		helper = new LocalServiceTestHelper(cfg, blobCfg, queueCfg, cacheCfg);
		helper.setUp();
	}

	@Override 
	protected void after() {
		helper.tearDown();
	}
}