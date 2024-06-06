package observersActions;

import java.util.List;

import org.json.JSONObject;

import view.ScrabbleObserver;

public class OnEnd extends OnObserverAction {

	private String message;
	
	OnEnd() {
		super("end");
	}

	@Override
	OnObserverAction interpret(JSONObject jo) {

		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.message = data.getString("message");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {

		for(ScrabbleObserver o : observers)
			o.onEnd(this.message);
	}
}
