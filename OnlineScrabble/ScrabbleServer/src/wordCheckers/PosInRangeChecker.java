package wordCheckers;

import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES
   Comprueba que la posición de la palabra introducida se encuentre en el rango permitido.
   En caso contrario, se lanza una excepción.
*/
public class PosInRangeChecker implements Checker{
	
	// Sobreescritura del método check
	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		if(posX < 0 || posX > game.getBoardSize() - 1 || posY < 0 || posY > game.getBoardSize() - 1)
			throw new CommandExecuteException("La posición introducida se sale del tablero.");
		
	}
}
