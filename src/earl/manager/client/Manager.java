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
package earl.manager.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Manager implements EntryPoint {
	private final ManagerServiceAsync manager = ManagerService.Util.getInstance();
	private ListBox invitations;
	private ListBox started;
	
	@Override
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();

		VerticalPanel verticalPanel = new VerticalPanel();
		rootPanel.add(verticalPanel, 10, 10);
		verticalPanel.setSize("413px", "224px");

		Button startButton = new Button("Start new game");
		startButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startNewGame();
			}
		});
		verticalPanel.add(startButton);
		startButton.setSize("148px", "24px");

		Label lblYourGames = new Label("Continue your game:");
		verticalPanel.add(lblYourGames);

		started = new ListBox();
		verticalPanel.add(started);
		started.setWidth("100%");
		started.setVisibleItemCount(5);
		
		Button continueButton = new Button("Continue");
		continueButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				continueGame();
			}
		});
		verticalPanel.add(continueButton);

		Label lblInvitations = new Label("Join a new game:");
		verticalPanel.add(lblInvitations);
		
				invitations = new ListBox();
				verticalPanel.add(invitations);
				invitations.setWidth("100%");
				invitations.setVisibleItemCount(5);
		
		Button joinButton = new Button("Join");
		joinButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				joinGame();
			}
		});
		joinButton.setEnabled(true);
		verticalPanel.add(joinButton);
		
		update();
		Window.alert("manager loaded.");
	}

	protected void joinGame() {
		// TODO Auto-generated method stub
		
	}

	protected void continueGame() {
		int idx = started.getSelectedIndex();
		String text = started.getItemText(idx);
	}

	protected void update() {
		manager.getUserTables(new ListUpdateCallback(started));
		manager.getInvitations(new ListUpdateCallback(invitations));
	}

	public void startNewGame() {
		manager.startGame(new ManagerCallback<String>(){
			@Override
			public void onSuccess(String result) {				
				Window.alert("Game started: "+result);
				UrlBuilder url = Window.Location.createUrlBuilder();
				url.setPath("/");
				url.setParameter("table", result);
				Window.Location.assign(url.buildString());
			}			
		});
	}
}
