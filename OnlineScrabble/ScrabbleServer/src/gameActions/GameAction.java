package gameActions;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import logic.Game;

public abstract class GameAction {
	
	protected final String type;
	
	GameAction(String type) {
		this.type = type;
	}
	
	private static List<GameAction> AVAILABLE_ACTIONS = new ArrayList<GameAction>() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(new PassTurnAction());
			add(new PlayTurnAction());
			add(new SwapTileAction());
			add(new UpdateAction());
			add(new UserExitsAction());
			add(new WriteAWordAction());
			add(new RegisterAction());
		}
	};
	
	public static GameAction getAction(JSONObject jo) {
		
		GameAction action = null;

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
	
	abstract GameAction interpret(JSONObject jo);
	
	public abstract void executeAction(Game game) throws FileNotFoundException;
}
