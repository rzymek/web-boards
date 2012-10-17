/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package earl.manager.server;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.engine.client.Table;
import earl.manager.client.ManagerService;

public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {

	@Override
	public String startGame() {
		String username = getCurrentUser();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		String name = UUID.randomUUID().toString();
		Key key = KeyFactory.createKey("table", name);
		Entity table = new Entity(key);
		table.setProperty("created", new Date());
		table.setProperty("player1", username);
		ds.put(table);
		return table.getKey().getName();
	}

	protected String getCurrentUser() {
		Principal principal = getThreadLocalRequest().getUserPrincipal();
		if (principal == null) {
			throw new SecurityException("Not logged in.");
		}
		return principal.getName();
	}

	@Override
	public List<Table> getUserTables() {
		String username = getCurrentUser();
		Filter isPlayer1 = createFilter("1", "2", username);
		Filter isPlayer2 = createFilter("2", "1", username);
		Filter filter = CompositeFilterOperator.or(isPlayer1, isPlayer2);
		Query query = new Query("table").setFilter(filter);
		return getTables(query);
	}

	private Filter createFilter(String is, String isNot, String username) {
		Filter player = new FilterPredicate("player" + is, FilterOperator.EQUAL, username);
		Filter not = new FilterPredicate("player" + isNot, FilterOperator.NOT_EQUAL, null);
		return CompositeFilterOperator.and(player, not);
	}

	@Override
	public List<Table> getInvitations() {
		return getTables(new Query("table"));
	}

	protected List<Table> getTables(Query query) {
		String username = getCurrentUser();
		List<Table> result = new ArrayList<Table>();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Iterable<Entity> tables = ds.prepare(query).asIterable();
		for (Entity entity : tables) {
			Date started = (Date) entity.getProperty("created");
			String player1 = (String) entity.getProperty("player1");
			String player2 = (String) entity.getProperty("player2");
			Table table = new Table();
			if (player1 == null || username.equals(player1)) {
				table.opponent = player2;
			} else {
				table.opponent = player1;
			}
			table.id = entity.getKey().getName();
			table.started = started;
			result.add(table);
		}
		return result;
	}

}
