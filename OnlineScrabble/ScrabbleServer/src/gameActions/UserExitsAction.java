package gameActions;

import org.json.JSONObject;

import logic.Game;

public class UserExitsAction extends GameAction {

	UserExitsAction() {
		super("user_exits");
	}

	@Override
	GameAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type")))
			return this;
		
		return null;
	}

	@Override
	public void executeAction(Game game) {
		game.userExits();
	}

}
