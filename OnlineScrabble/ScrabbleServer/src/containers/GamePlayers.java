package containers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Game;
import simulatedObjects.Player;
import simulatedObjects.Tile;
import strategies.EasyStrategy;
import strategies.HardStrategy;
import strategies.HumanStrategy;
import strategies.MediumStrategy;
import strategies.Strategy;
import wordCheckers.WordChecker;

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
	
	/* Método playTurn:
	 * Delega en el player correspondiente la acción de jugar su turno.
	 */
	public void playTurn(Game game, WordChecker wordChecker) {
		this.players.get(game.getCurrentTurn()).play(game, wordChecker);
	}

	/* Método drawTiles:
	 * 
	 * Este método proporciona al jugador actual las fichas que le falten hasta completar
	 * el número máximo permitido, delegando la acción de añadir una ficha en la función
	 * addTile de la clase Player.
	 * 
	 * En caso de que no quedasen fichas para asignar (tile == null), no se hace nada.
	 */
	public void drawTiles(Game game, int player) {
		
		for(int j = players.get(player).getNumTiles(); j < NUM_TILES; j++) {
			
			Tile tile = game.randomTile();
			
			if(tile != null) {
				players.get(player).addTile(tile);
				game.removeTile(tile);
			}
			
			else break;
		}
	}

	/* Método removePlayerTile:
	 * Elimina una ficha de la mano de un jugador.
	 */
	public void removePlayerTile(int player, Tile tile) {
		this.players.get(player).removeTile(tile);		
	}

	/* Método numberOfTilesOf:
	 * Devuelve el número de fichas que tiene un jugador con una letra determinada.
	 * Delega la acción a la función numberOfTilesOf de la clase Player.
	 */
	public int numberOfTilesOf(int player, String letter) {
		return this.players.get(player).numberOfTilesOf(letter);
	}
	
	/* Método givePoints:
	 * Suma a un jugador una cantidad determinada de puntos.
	 * Delega la acción a la función givePoints de la clase Player.
	 */
	public void givePoints(int player, int points) {
		this.players.get(player).givePoints(points);
	}
	
	/* Método playerHasLetter:
	 * Devuelve si un jugador tiene en su mano una letra determinada.
	 * Delega la acción a la función hasLetter de la clase Player.
	 */
	public boolean playerHasLetter(int player, String letter) {
		return this.players.get(player).hasLetter(letter);
	}
	
	/* Método getWinners:
	 * Devuelve la lista de todos los jugadores que hayan ganado
	 * (son posibles los empates).
	 */
	public List<Integer> getWinners() {
		
		List<Integer> winners = new ArrayList<Integer>();
		
		for(int i = 0; i < this.getNumPlayers(); ++i) {
			
			if(winners.size() == 0)
				winners.add(i);
			
			else {
				
				if(this.players.get(winners.get(0)).getPoints() == this.players.get(i).getPoints())
					winners.add(i);
				
				else if (this.players.get(winners.get(0)).getPoints() < this.players.get(i).getPoints()) {
					winners.clear();
					winners.add(i);
				}
			}
		}
		
		return winners;
	}

	// Getters
	
	public int getNumPlayers() {
		return players.size();
	}

	public String getPlayerName(int i) {
		return players.get(i).getName();
	}

	public int getNumPlayerTiles(int i) {
		return this.players.get(i).getNumTiles();
	}

	public Tile getPlayerTile(int player, int tile) {
		return this.players.get(player).getTile(tile);
	}
	
	public Tile getPlayerTile(int player, String letter) {
		return this.players.get(player).getTile(letter);
	}

	public int getPlayerPoints(int player) {
		return this.players.get(player).getPoints();
	}

	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		JSONArray players = new JSONArray();
		
		for(int i = 0; i < this.players.size(); ++i)
			players.put(this.players.get(i).report());
		
		jo.put("players", players);
		
		return jo;
	}

	public List<Tile> getTiles(int player) {
		
		return this.players.get(player).getTiles();
	}

	public void addNewHumanPlayer(String name) {
		this.players.add(new Player(name, 0, new ArrayList<Tile>(), new HumanStrategy()));
	}
	
	public void addNewAutomaticPlayer(String strategyType) {
		
		Strategy strategy = null;
		if(strategyType.equalsIgnoreCase("easy"))
			strategy = new EasyStrategy();
		else if(strategyType.equals("medium"))
			strategy = new MediumStrategy();
		else
			strategy = new HardStrategy();
		
		this.players.add(new Player("CPU " + (this.getNumPlayers() + 1), 0, new ArrayList<Tile>(), strategy));
	}
}
