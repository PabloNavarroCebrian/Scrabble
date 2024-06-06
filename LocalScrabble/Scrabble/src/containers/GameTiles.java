package containers;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulatedObjects.Tile;

/* APUNTES GENERALES

   La clase GameTiles es usada para modelizar el conjunto de fichas de juego (saco).
	
   Consta de tan solo un atributo: una lista de objetos de tipo Tile.
*/
public class GameTiles {

	private List<Tile> tiles;
	
	public GameTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}

	/* Método add:
	 * Añade una ficha al saco de fichas del juego.
	 */
	public void add(Tile tile) {
		tiles.add(tile);
	}
	
	/* Método remove:
	 * Borra una ficha al saco de fichas del juego.
	 */
	public void remove(Tile tile) {
		tiles.remove(tile);
	}
	
	/* Método addAll:
	 * Añade a la lista de fichas del juego las recibidas por parámetro.
	 */
	public void addAll(List<Tile> tiles) {
		this.tiles.addAll(tiles);
	}
	
	// Getters
	
	public int getNumTiles() {
		return tiles.size();
	}

	public Tile getTile(int i) {
		return tiles.get(i);
	}

	public int getSize() {
		return tiles.size();
	}
	
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		
		JSONArray tiles = new JSONArray();
		
		for(int i = 0; i < this.tiles.size(); ++i)
			tiles.put(this.tiles.get(i).report());
		
		jo.put("tiles", tiles);
		
		return jo;
	}
}
