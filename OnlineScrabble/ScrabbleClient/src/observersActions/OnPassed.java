package observersActions;

import java.util.List;

import org.json.JSONObject;

import view.ScrabbleObserver;

public class OnPassed  extends OnObserverAction {

	private int numPlayers;
	private String currentPlayerName;
	private boolean gameInitiated;
	
	OnPassed() {
		super("passed");
	}
	
	@Override
	OnObserverAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.numPlayers = data.getInt("num_players");
			this.currentPlayerName = data.getString("current_player_name");
			this.gameInitiated = data.getBoolean("game_initiated");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {

		for(ScrabbleObserver o : observers)
			o.onPassed(numPlayers, currentPlayerName, gameInitiated);
	}

}
