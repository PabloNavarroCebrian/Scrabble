package gameActions;

import org.json.JSONObject;

import logic.Game;

public class WriteAWordAction extends GameAction {

	private String word;
	private int posX;
	private int posY;
	private String direction;
	
	WriteAWordAction() {
		super("write_a_word");
	}

	@Override
	GameAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.word = data.getString("word");
			this.posX = data.getInt("pos_X");
			this.posY = data.getInt("pos_Y");
			this.direction = data.getString("direction");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(Game game) {
		
		game.writeAWord(word, posX, posY, direction);
	}
}
