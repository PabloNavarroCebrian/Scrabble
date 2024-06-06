package simulatedObjects;

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
	
	public Box(SpecialEffects specialEffect, Tile tile, boolean specialEffectAlreadyDone) {
		this.tile = tile;
		this.specialEffect = specialEffect;
	}
	
	// Getters
	
	public SpecialEffects getSpecialEffect() {
		return this.specialEffect;
	}

	public Tile getTile() {
		return this.tile;
	}
}
