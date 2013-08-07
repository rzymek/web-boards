package webboards.server.tests

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.WebTarget
import org.glassfish.jersey.client.ClientConfig
import org.junit.Before

abstract class ServerTestBase {
	protected WebTarget webTarget

	@Before
	def void setupServerTestBase() {
		var clientConfig = new ClientConfig()
		var client = ClientBuilder::newClient(clientConfig)
		webTarget = client.target('http://localhost:8888/json')
	}
}
