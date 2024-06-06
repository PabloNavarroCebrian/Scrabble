package strategies;

import java.util.List;

import logic.Game;
import simulatedObjects.Tile;
import wordCheckers.WordChecker;

// Ver apuntes de la interfaz Strategy.
public class HumanStrategy implements Strategy {
	
	/* Sobreescritura del método play:
	 * Para la estrategia humana, es necesario que el usuario realice la acción.
	 * Esto se delega en la clase Game.
	 */
	
	@Override
	public void play(Game game, WordChecker wordChecker, List<Tile> tilesForWord) {
		game.movementNeeded();
	}
	
	@Override
	public String toString() {
		return "human_strategy";
	}
}
