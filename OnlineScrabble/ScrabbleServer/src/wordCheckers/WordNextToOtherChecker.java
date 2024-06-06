package wordCheckers;

import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES:
   Comprueba que alguna de las casillas ocupadas por la palabra, 
   sea colindante a otra palabra ya existente en el tablero.
   En caso contrario, lanza una excepción.
 */
public class WordNextToOtherChecker implements Checker{
	
	// Sobreescritura del método check
	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		if(Game.getWordsInBoard()) {
			int vertical = ("V".equalsIgnoreCase(direction) ? 1 : 0);
			int horizontal = ("H".equalsIgnoreCase(direction) ? 1 : 0);
			
			for (int i = 0; i < word.length(); ++i)
				if (game.getBoard().getTile(posX + i * vertical, posY + i * horizontal) != null) 
					return;

			throw new CommandExecuteException("La palabra introducida debe cortarse con alguna de las que ya están en el tablero.");
		}
	}
}
