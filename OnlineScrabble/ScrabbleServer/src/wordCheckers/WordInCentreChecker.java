package wordCheckers;

import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES:
   Comprueba si alguna de las posiciones ocupadas por la palabra es la del centro.
   En caso contrario, lanza una excepción.
 */ 
public class WordInCentreChecker implements Checker{

	// Sobreescritura del método check
	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		if(!Game.getWordsInBoard()) {
			int vertical = ("V".equalsIgnoreCase(direction) ? 1 : 0);
			int horizontal = ("H".equalsIgnoreCase(direction) ? 1 : 0);
			
			for (int i = 0; i < word.length(); ++i)
				if (game.getBoard().isCenter(posX + i * vertical, posY + i * horizontal)) 
					return;
			
			throw new CommandExecuteException("La primera palabra introducida en el tablero debe situarse en la casilla central.");
		}
		
	}

}
