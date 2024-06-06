package observersActions;

import java.util.List;

import org.json.JSONObject;

import view.ScrabbleObserver;

public class OnError extends OnObserverAction {

	private String message;
	private int currentTurn;
	
	OnError() {
		super("error");
	}

	@Override
	OnObserverAction interpret(JSONObject jo) {

		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.message = data.getString("message");
			this.currentTurn = data.getInt("current_turn");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {

		for(ScrabbleObserver o : observers)
			o.onError(this.message, currentTurn);
	}
	
	
}
