package webboards.server.tests

import javax.ws.rs.core.MediaType
import org.junit.Test

class ServerTest extends ServerTestBase {
	
	@Test
	def void test() {
		val result = webTarget 
			.path('resource')
			.queryParam('greeting', 'Hi World!')
			.request(MediaType::TEXT_PLAIN_TYPE)			
			.get(typeof(String))
		println(result)
	}
}
