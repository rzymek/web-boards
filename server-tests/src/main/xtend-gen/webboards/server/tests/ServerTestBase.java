package webboards.server.tests;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Before;

@SuppressWarnings("all")
public abstract class ServerTestBase {
  protected WebTarget webTarget;
  
  @Before
  public void setupServerTestBase() {
    ClientConfig _clientConfig = new ClientConfig();
    ClientConfig clientConfig = _clientConfig;
    Client client = ClientBuilder.newClient(clientConfig);
    WebTarget _target = client.target("http://localhost:8888/json");
    this.webTarget = _target;
  }
}
