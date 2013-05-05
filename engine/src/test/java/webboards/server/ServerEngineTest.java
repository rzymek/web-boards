package webboards.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import webboards.client.data.GameInfo;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.server.entity.Player;
import webboards.server.entity.Table;
import webboards.server.servlet.ManagerServlet;

import com.googlecode.objectify.ObjectifyService;

@RunWith(MockitoJUnitRunner.class)
public class ServerEngineTest {
	@Spy
	@InjectMocks
	ServerEngineImpl engine = new ServerEngineImpl();

	@Rule
	public EmbeddedDataStore store = new EmbeddedDataStore();

	@Before
	public void register() {
		ManagerServlet.registerEntities();
	}

	@Test
	public void getState() {
		Table table = new Table();
		table.game = new Bastogne();
		table.id = 432L;
		ObjectifyService.ofy().save().entity(table);
		Player p1 = new Player(table, "user@test.com", BastogneSide.US);
		doReturn(p1.user).when(engine).getUser();
		GameInfo state = engine.getState(table.id);
		assertEquals(Bastogne.class, state.game.getClass());
		//TODO: other assertions	
		
		//assertEquals(table.game, state.game);
		//assertEquals(loadOps(table), state.ops);
		assertEquals(BastogneSide.US, state.side);
		//assertEquals(table.scenario, state.scenario); //czym to jest ?
		
		
	}
}
