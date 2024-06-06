package observersActions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import view.ScrabbleObserver;

public abstract class OnObserverAction {
	
	protected final String type;
	
	OnObserverAction(String type) {
		this.type = type;
	}
	
	private static List<OnObserverAction> AVAILABLE_ACTIONS = new ArrayList<OnObserverAction>() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(new OnWordWritten());
			add(new OnPassed());
			add(new OnSwapped());
			add(new OnEnd());
			add(new OnError());
			add(new OnFirstTurnDecided());
			add(new OnMovementNeeded());
			add(new OnReset());
			add(new OnUpdate());
		}
	};
	
	public static OnObserverAction getAction(JSONObject jo) {
		
		OnObserverAction action = null;

		int i = 0;

		while (action == null && i < AVAILABLE_ACTIONS.size()) {
			action = AVAILABLE_ACTIONS.get(i).interpret(jo);
			++i;
		}

		if (action == null) {
			throw new IllegalArgumentException("El JSON no es válido.");
		}

		return action;
	}
	
	abstract OnObserverAction interpret(JSONObject jo);
	
	public abstract void executeAction(List<ScrabbleObserver> observers);
}
