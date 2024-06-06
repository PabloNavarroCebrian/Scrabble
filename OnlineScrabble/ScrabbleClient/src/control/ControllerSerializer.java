package control;

import org.json.JSONObject;

public class ControllerSerializer {

	public static JSONObject serializeWriteAWord(String word, int posX, int posY, String direction) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "write_a_word");
		
		JSONObject data = new JSONObject();
		
		data.put("word", word);
		data.put("pos_X", posX);
		data.put("pos_Y", posY);
		data.put("direction", direction);
		
		jo.put("data", data);
		
		return jo;
	}

	public static JSONObject serializePassTurn() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "pass_turn");
		
		return jo;
	}

	public static JSONObject serializeSwapTile() {

		JSONObject jo = new JSONObject();
		
		jo.put("type", "swap_tile");
		
		return jo;
	}

	public static JSONObject serializeUserExits() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "user_exits");
		
		return jo;
	}

	public static JSONObject serializeUpdate() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "update");
		
		return jo;
	}
	
	public static JSONObject serializeRegister(String name) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "register");
		
		JSONObject data = new JSONObject();
		
		data.put("name", name);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static JSONObject serializePlayTurn() {

		JSONObject jo = new JSONObject();
		
		jo.put("type", "play_turn");
		
		return jo;
	}
}
