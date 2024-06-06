package observersActions;

import java.util.List;

import org.json.JSONObject;

import view.ScrabbleObserver;

public class OnMovementNeeded extends OnObserverAction {
	
	private int currentTurn;

	OnMovementNeeded() {
		super("movement_needed");
	}

	@Override
	OnObserverAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.currentTurn = data.getInt("current_turn");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {

		for(ScrabbleObserver o : observers)
			o.onMovementNeeded(currentTurn);
	}

}
