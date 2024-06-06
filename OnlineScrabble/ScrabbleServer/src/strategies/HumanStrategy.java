package strategies;

import java.util.List;

import logic.Game;
import simulatedObjects.Tile;
import wordCheckers.WordChecker;

public class HumanStrategy implements Strategy {
	
	@Override
	public void play(Game game, WordChecker wordChecker, List<Tile> tilesForWord) {
		game.movementNeeded();
	}
	
	@Override
	public String toString() {
		return "human_strategy";
	}
}
