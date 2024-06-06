package strategies;

import java.util.ArrayList;
import java.util.List;

import logic.Game;
import simulatedObjects.Tile;
import wordCheckers.WordChecker;

public class MediumStrategy implements Strategy {
	
	@Override
	public void play(Game game, WordChecker wordChecker, List<Tile> tilesForWord) {
		boolean wordWritten = false;
		
		List<Integer> lengths = new ArrayList<Integer>();
		for(int i = 2; i <= tilesForWord.size(); ++i)
			lengths.add(i);
		
		if(Game.getWordsInBoard())
			lengths.add(tilesForWord.size() + 1);

		while(!wordWritten && lengths.size() > 0) {
			int i = (int) (Game.getRandom().nextDouble() * lengths.size());
			wordWritten = tryWritingInBoard(lengths.remove(i), tilesForWord, game, wordChecker);
		}
		
		if(!wordWritten) {
			
			if(Game.getRandom().nextDouble() > 0.5 && game.getRemainingTiles() > 0)
				game.swapTile();
			else 
				game.passTurn();
		}
	}
	
	@Override
	public String toString() {
		return "medium_strategy";
	}

}
