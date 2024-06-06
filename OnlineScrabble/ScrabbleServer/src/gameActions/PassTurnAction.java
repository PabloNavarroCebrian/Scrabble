package gameActions;

import org.json.JSONObject;

import logic.Game;

public class PassTurnAction extends GameAction {

	PassTurnAction() {
		super("pass_turn");
	}

	
	@Override
	GameAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type")))
			return this;
		
		return null;
	}

	@Override
	public void executeAction(Game game) {

		game.passTurn();		
	}
}
