package wordCheckers;

import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES
 * En función de si la dirección es vertical u horizontal, comprueba que la palabra introducida coincida
 * con la que se forma en total en el tablero. En caso contrario, se lanza una excepción.
 */
public class WordUnionChecker implements Checker {
	
	// Sobreescritura del método check
	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		if ("V".equalsIgnoreCase(direction)) {
			if((posX > 0 && game.getBoard().getTile(posX - 1, posY) != null) 
				|| ((posX + word.length() - 1) < game.getBoardSize() - 1 && game.getBoard().getTile(posX + word.length(), posY) != null))
			throw new CommandExecuteException("La palabra introducida debe ser la que se forma en total en el tablero");
		}
		
		else {
			if((posY > 0 && game.getBoard().getTile(posX, posY - 1) != null) 
					|| ((posY + word.length() - 1) < game.getBoardSize() - 1 && game.getBoard().getTile(posX, posY + word.length()) != null))
				throw new CommandExecuteException("La palabra introducida debe ser la que se forma en total en el tablero");		
		}
	}

}
