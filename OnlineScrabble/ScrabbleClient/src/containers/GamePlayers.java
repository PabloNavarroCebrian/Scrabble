package containers;

import java.util.List;

import simulatedObjects.Player;
import simulatedObjects.Tile;

/* APUNTES GENERALES

   La clase GamePlayers es usada para modelizar el conjunto de jugadores en una partida.

   Consta de tan solo un atributo: una lista de objetos Player.
*/
public class GamePlayers {
	
	public static final int NUM_TILES = 7;
	
	private List <Player> players;
	
	public GamePlayers(List<Player> players) {
		this.players = players;
	}
	
	// Getters

	public String getPlayerName(int i) {
		return players.get(i).getName();
	}

	public int getNumPlayerTiles(int i) {
		return this.players.get(i).getNumTiles();
	}

	public Tile getPlayerTile(int player, int tile) {
		return this.players.get(player).getTile(tile);
	}
	
	public int getPlayerPoints(int player) {
		return this.players.get(player).getPoints();
	}
}
