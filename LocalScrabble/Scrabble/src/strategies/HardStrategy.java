package strategies;

import java.util.List;

import logic.Game;
import simulatedObjects.Tile;
import wordCheckers.WordChecker;

// Ver apuntes de la interfaz Strategy.
public class HardStrategy implements Strategy {
	
	/* Sobreescritura del método play:
	 * La estrategia difícil consiste en que el jugador forme palabras de tamaño máximo.
	 * 
	 * En caso de no haber escrito ninguna palabra, intenta
	 * intercambiar ficha, y si no puede, pasa de turno.
	 */
	@Override
	public void play(Game game, WordChecker wordChecker, List<Tile> tilesForWord) {
		boolean wordWritten = false;
		
		int extraTile = (Game.getWordsInBoard() ? 1 : 0);
		for(int wordLength = tilesForWord.size() + extraTile; wordLength > 1 && !wordWritten; --wordLength)
			wordWritten = tryWritingInBoard(wordLength, tilesForWord, game, wordChecker);
		
		if(!wordWritten) {
			
			if(game.getRemainingTiles() > 0)
				game.swapTile();
			else 
				game.passTurn();
		}
	}
	
	@Override
	public String toString() {
		return "hard_strategy";
	}

}
