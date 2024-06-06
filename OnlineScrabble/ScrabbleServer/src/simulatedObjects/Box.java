package simulatedObjects;

import org.json.JSONObject;

/* APUNTES GENERALES:

	La clase Box representa una casilla del tablero.
	
	Por ello, sus atributos son:
	- La ficha que podría contener.
	- El efecto especial asociado (puede ser nulo; ver clase enumerada SpecialEffects).
	- Booleano indicando si ya se ha aplicado el efecto especial de esta casilla (para que no se cuenten puntos dobles).
*/

public class Box {

	private Tile tile;
	private SpecialEffects specialEffect;
	private boolean specialEffectAlreadyDone;
	
	public Box(SpecialEffects specialEffect, Tile tile, boolean specialEffectAlreadyDone) {
		this.tile = tile;
		this.specialEffect = specialEffect;
		this.specialEffectAlreadyDone = specialEffectAlreadyDone;
	}
	
	/* Método assignTile:
	 * Asigna a esta casilla la ficha recibida por parámetro.
	 * Si se intenta asignar una ficha a una casilla ya ocupada,
	 * se lanza una excepción.
	 */
	public void assignTile(Tile tile) {
		
		if (this.tile != null) 
			throw new IllegalArgumentException("Esta casilla ya está ocupada!");
		
		this.tile = tile;
	}
	
	/* Método getPoints:
	 * Devuelve los puntos asociados a esta casilla, es decir, los puntos de la letra, 
	 * multiplicados por el posible efecto especial de la casilla (si no existe, será 1).
	 */
	public int getPoints() {
		
		int points = this.tile.getPoints();
		
		if(!this.specialEffectAlreadyDone && this.specialEffect != null) {
			
			int letterMultiplier = this.specialEffect.getLetterPointsMultiplier();
			
			if (letterMultiplier != 1) 
				this.specialEffectAlreadyDone = true;
			
			points *= letterMultiplier;
		}
		
		return points;
	}
	
	/* Método getWordMultiplier:
	 * Devuelve el valor del modificador de puntos de una palara asociado a una casilla.
	 * En caso de no existir, devuelve el elemento neutro para el producto (1).
	 */
	public int getWordMultiplier() {
		
		int wordMultiplier = 1;
		
		if (!this.specialEffectAlreadyDone && this.specialEffect != null) {
			wordMultiplier = this.specialEffect.getWordPointsMultiplier();
			
			if (wordMultiplier != 1) 
				this.specialEffectAlreadyDone = true;
		}
		
		return wordMultiplier;
	}
	
	@Override
	public String toString() {
		if(tile == null)
			return "";
		
		return tile.getLetter();
	}
	
	// Getters

	public Tile getTile() {
		return this.tile;
	}

	public boolean isCentre() {
		return SpecialEffects.CENTRE.equals(this.specialEffect);
	}
	
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		
		if(this.specialEffect != null)
			jo.put("special_effect", this.specialEffect.toString());
		
		if(this.tile != null)
			jo.put("tile", this.tile.report());
		
		return jo;
	}
}
