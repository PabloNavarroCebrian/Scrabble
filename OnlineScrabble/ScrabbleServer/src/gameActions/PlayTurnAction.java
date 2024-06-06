package gameActions;

import java.io.FileNotFoundException;

import org.json.JSONObject;

import logic.Game;

public class PlayTurnAction extends GameAction {

	PlayTurnAction() {
		super("play_turn");
	}

	@Override
	GameAction interpret(JSONObject jo) {

		if(this.type.equals(jo.getString("type")))
			return this;
		
		return null;
	}

	@Override
	public void executeAction(Game game) throws FileNotFoundException {
		game.playTurn();
	}

}
