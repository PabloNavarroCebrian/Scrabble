package logic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import containers.Board;
import containers.GamePlayers;

public class GameSerializer {
	
	public static final JSONObject serializeWordWritten (String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn, Board board, boolean gameInitiated) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "word_written");
		
		JSONObject data = new JSONObject();
		data.put("word", word);
		data.put("pos_X", posX);
		data.put("pos_Y", posY);
		data.put("direction", direction);
		data.put("points", points);
		data.put("extra_points", extraPoints);
		data.put("num_players", numPlayers);
		data.put("game_players", gamePlayers.report());
		data.put("current_turn", currentTurn);
		data.put("game_board", board.report());
		data.put("game_initiated", gameInitiated);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializePassed(int numPlayers, String currentPlayerName, Object newParam, boolean gameInitiated) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "passed");
		
		JSONObject data = new JSONObject();
		data.put("num_players", numPlayers);
		data.put("current_player_name", currentPlayerName);
		data.put("game_initiated", gameInitiated);

		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializeSwapped(int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "swapped");
		
		JSONObject data = new JSONObject();
		
		data.put("num_players", numPlayers);
		data.put("game_players", gamePlayers.report());
		data.put("current_turn", currentTurn);
		data.put("game_initiated", gameInitiated);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializeEnd(String message) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "end");
		
		JSONObject data = new JSONObject();
		
		data.put("message", message);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializeError(String message, int currentTurn) {

		JSONObject jo = new JSONObject();
		
		jo.put("type", "error");
		
		JSONObject data = new JSONObject();
		
		data.put("message", message);
		data.put("current_turn", currentTurn);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializeFirstTurnDecided(List<String> lettersObtained, GamePlayers gamePlayers, int numPlayers, int currentTurn, boolean gameInitiated) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "first_turn_decided");
		
		JSONObject data = new JSONObject();
		
		JSONArray lettersJA = new JSONArray();
		for (int i = 0; i < lettersObtained.size(); ++i)
			lettersJA.put(lettersObtained.get(i));
		
		data.put("letters_obtained", lettersJA);
		data.put("game_players", gamePlayers.report());
		data.put("num_players", numPlayers);
		data.put("current_turn", currentTurn);
		data.put("game_initiated", gameInitiated);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializeOnMovementNeeded(int currentTurn) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "movement_needed");
		
		JSONObject data = new JSONObject();
		
		data.put("current_turn", currentTurn);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializeRegister(Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "register");
		
		JSONObject data = new JSONObject();
		
		if(board != null)
			data.put("game_board", board.report());

		data.put("num_players", numPlayers);
		
		if(gamePlayers != null)
			data.put("game_players", gamePlayers.report());
		
		data.put("current_turn", currentTurn);
		data.put("game_initiated", gameInitiated);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializeReset(Board board, int numPlayers, String currentPlayerName, int remainingTiles, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "reset");
		
		JSONObject data = new JSONObject();
		
		data.put("game_board", board.report());
		data.put("num_players", numPlayers);
		if(currentPlayerName != null)
			data.put("current_player_name", currentPlayerName);
		data.put("remaining_tiles", remainingTiles);
		if(gamePlayers != null)
			data.put("game_players", gamePlayers.report());
		data.put("current_turn", currentTurn);
		data.put("game_initiated", gameInitiated);
		
		jo.put("data", data);
		
		return jo;
	}
	
	public static final JSONObject serializeUpdate(boolean gameFinished, int numPlayers, int remainingTiles, String currentPlayerName, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", "update");
		
		JSONObject data = new JSONObject();
		
		data.put("game_finished", gameFinished);
		data.put("num_players", numPlayers);
		data.put("remaining_tiles", remainingTiles);
		data.put("current_player_name", currentPlayerName);
		data.put("game_players", gamePlayers.report());
		data.put("current_turn", currentTurn);
		data.put("game_initiated", gameInitiated);
		
		jo.put("data", data);
		
		return jo;
	}
}
