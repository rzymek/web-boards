package webboards.server.tests;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Test;
import webboards.server.tests.ServerTestBase;

@SuppressWarnings("all")
public class ServerTest extends ServerTestBase {
  @Test
  public void test() {
    WebTarget _path = this.webTarget.path("resource");
    WebTarget _queryParam = _path.queryParam("greeting", "Hi World!");
    Builder _request = _queryParam.request(MediaType.TEXT_PLAIN_TYPE);
    final String result = _request.<String>get(String.class);
    InputOutput.<String>println(result);
  }
}
