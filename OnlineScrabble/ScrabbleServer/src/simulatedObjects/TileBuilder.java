package simulatedObjects;

import org.json.JSONObject;

public class TileBuilder {

	/* Sobrescritura del método createTheInstance:
	 * 
	 * Construye y devuelve una instancia de la clase Tile,
	 * obteniendo la información necesaria del JSONObject recibido por parámetro.
	 */
	
	public Tile createTile(JSONObject data) {
		
		String letter = data.getString("letter");
		int points = data.getInt("points");
		
		return new Tile(letter, points);
	}
}
