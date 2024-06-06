package gameActions;

import org.json.JSONObject;

import logic.Game;

public class SwapTileAction extends GameAction {

	SwapTileAction() {
		super("swap_tile");
	}

	@Override
	GameAction interpret(JSONObject jo) {

		if(this.type.equals(jo.getString("type")))
			return this;
		
		return null;
	}

	@Override
	public void executeAction(Game game) {
		game.swapTile();
	}

}
