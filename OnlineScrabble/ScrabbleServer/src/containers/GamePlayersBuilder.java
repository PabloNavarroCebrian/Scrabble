package containers;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulatedObjects.Player;
import simulatedObjects.PlayerBuilder;

public class GamePlayersBuilder {

	private PlayerBuilder playerBuilder;
	
	public GamePlayersBuilder(PlayerBuilder playerBuilder) {
		this.playerBuilder = playerBuilder;
	}

	/* Sobrescritura del método createTheInstance:
	 * 
	 * Construye y devuelve una instancia de la clase GameTiles,
	 * es decir, las fichas del "saco" que los jugadores irán cogiendo
	 * según avance la partida.
	 * 
	 * Es necesario tener un array auxiliar de objetos de tipo PlayerBuilder
	 * para recorrerlo y poder crear un tipo de player especifico. 
	 */
	
	public GamePlayers createGamePlayers(JSONObject data) {
		
		JSONArray jsonArrayPlayers = data.getJSONArray("players");
		
		List<Player> players = new ArrayList<Player>();
		
		for(int i = 0; i < jsonArrayPlayers.length(); i++) {
			
			Player playerToAdd = null;
			playerToAdd = playerBuilder.createPlayer(jsonArrayPlayers.getJSONObject(i), i);
			
			if(playerToAdd != null)
				players.add(playerToAdd);
				
			else throw new InputMismatchException("El JSON no es válido (GamePlayers).");
		}
		
		return new GamePlayers(players);
		
	}

}
