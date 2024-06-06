package simulatedObjects;

import org.json.JSONObject;

/* APUNTES GENERALES:

   La clase Tile representa una ficha en el juego.
   
   Sus atributos son:
   - La letra de la ficha
   - Los puntos de la ficha
*/

public class Tile {

	private String letter;
	private int points;
	
	public Tile(String letter, int points) {
		this.letter = letter;
		this.points = points;
	}
	
	// Getters

	public int getPoints() {
		return this.points;
	}

	public String getLetter() {
		return this.letter;
	}
	
	@Override
	public String toString() {
		return this.letter + "[" + this.points + "]";
	}
	
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		jo.put("letter", this.letter);
		jo.put("points", this.points);
		
		return jo;
	}
}
