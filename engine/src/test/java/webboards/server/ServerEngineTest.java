package webboards.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import webboards.client.data.Game;
import webboards.client.data.GameInfo;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.games.scs.bastogne.scenarios.BattleForLongvilly;
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

	private Table table;

	private Player player1;

	@Before
	public void setup() {
		ObjectifyService.reset();
		ManagerServlet.registerEntities();
		table = new Table();
		table.game = new Bastogne();
		table.scenario = new BattleForLongvilly();
		table.id = 1L;
		player1 = new Player(table, "player1@test.com", BastogneSide.GE);
		player1.channelToken = "xyz";
		ObjectifyService.ofy().save().entities(table, player1);
	}

	@Test
	public void getStateOfRunningGame() {
		doReturn(player1.user).when(engine).getUser();
		GameInfo state = engine.getState(table.id);
		assertEquals(player1.side, state.side);
		assertEquals(player1.channelToken, state.channelToken);
		assertNull("joinAs", state.joinAs);
		assertEquals(table.game.getClass(), state.game.getClass());
		assertEquals(table.scenario.getClass(), state.scenario.getClass());
		assertNotNull("ops", state.ops);
	}

	@Test
	public void joinGame() {
		doReturn("player2@example.com").when(engine).getUser();
		GameInfo state = engine.getState(table.id);
		assertNull("state.side", state.side);
		assertNotEquals(player1.side, state.joinAs);
		// TODO: other assertions

		
		//assertEquals(player1.channelToken, state.channelToken);
		assertEquals(table.game.getClass(), state.game.getClass());
		assertEquals(table.scenario.getClass(), state.scenario.getClass());
		assertNotNull("ops", state.ops);
		assertNotEquals(player1.channelToken, state.channelToken);
		

	}
}
