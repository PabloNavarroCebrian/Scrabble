package simulatedObjects;

/* APUNTES GENERALES:

   La clase Tile representa una ficha en el juego.
   
   Sus atributos son:
   - La letra de la ficha
   - Los puntos de la ficha
*/

public class Tile {

	private String letter;
	
	public Tile(String letter, int points) {
		this.letter = letter;
	}
	
	// Getters

	public String getLetter() {
		return this.letter;
	}
}
