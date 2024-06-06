package gameActions;

import org.json.JSONObject;

import logic.Game;

public class UpdateAction extends GameAction {

	UpdateAction() {
		super("update");
	}

	@Override
	GameAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type")))
			return this;
		
		return null;
	}

	@Override
	public void executeAction(Game game) {
		game.update();
	}

}
