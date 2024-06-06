package containers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulatedObjects.Box;
import simulatedObjects.Tile;
import utils.Pair;

/* APUNTES GENERALES
   
   La clase Board es usada para modelizar el tablero de juego.
   
   Consta de tan solo dos atributos: el tablero (matriz de objetos Box),
   y la posición (como un par), de la casilla central.
 */
public class Board {
	
	private List<List<Box>> board;
	private Pair<Integer, Integer> center;
	
	public Board(List<List<Box>> board) {
		this.board = board; // Vacío en caso de nueva partida.
		this.center = searchCenter();
	}
	
	/* Método searchCenter:
	 * 
	 * Dado un tablero (posiblemente no cuadrado), 
	 * busca la posición más centrada en él. Para ello,
	 * delega la comprobación de ser el centro en el método
	 * de isCenter de esta misma clase.
	 */
	private Pair<Integer, Integer> searchCenter() {
		for(int i = 0; i < board.size(); i++) {
			for(int j = 0; j < board.size(); j++) {
				if(isCenter(i, j))
					return new Pair<Integer, Integer>(i, j);
			}
		}
		
		return null;
	}
	
	/* Método assignTile:
	 * Asigna en una posición dada del tablero una ficha recibida por parámetro.
	 */
	public void assignTile(Tile tile, int posX, int posY) {
		board.get(posX).get(posY).assignTile(tile);
	}
	
	// Getters

	public int getBoardSize() {
		return this.board.size();
	}

	public Tile getTile(int posX, int posY) {
		return board.get(posX).get(posY).getTile();
	}

	public int getPoints(int posX, int posY) {
		return board.get(posX).get(posY).getPoints();
	}

	public boolean isCenter(int posX, int posY) {
		return board.get(posX).get(posY).isCentre();
	}

	public Box getBoxAt(int posX, int posY) {
		return board.get(posX).get(posY);
	}

	public int getWordMultiplier(int posX, int posY) {
		return this.board.get(posX).get(posY).getWordMultiplier();
	}
	
	public Pair<Integer, Integer> getCenter() {
		return center;
	}
	
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		JSONArray board = new JSONArray();
		
		for(int i = 0; i < this.board.size(); ++i) {
			JSONArray row = new JSONArray();
			
			for(int j = 0; j < this.board.get(i).size(); ++j) 
				row.put(this.board.get(i).get(j).report());
			
			board.put(row);
		}
		
		jo.put("board", board);
		
		return jo;
	}
}
