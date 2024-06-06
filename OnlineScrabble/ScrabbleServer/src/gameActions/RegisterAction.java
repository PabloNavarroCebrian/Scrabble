package gameActions;

import java.io.FileNotFoundException;

import org.json.JSONObject;

import logic.Game;

public class RegisterAction extends GameAction {
	
	private String name;
	
	RegisterAction() {
		super("register");
	}

	@Override
	GameAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.name = data.getString("name");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(Game game) throws FileNotFoundException {
		game.addNewHumanPlayer(name);
		game.register();
	}
}
