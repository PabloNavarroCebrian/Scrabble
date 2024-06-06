package wordCheckers;

import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES
   Comprueba que el tamaño de la palabra no sea mayor al tamaño del tablero, y que se haya
   escrito una palabra de al menos dos letras. En caso contrario, se lanza una excepción.
 */
public class WordLengthChecker implements Checker {
	
	// Sobreescritura del método check
	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		if (word.length() > game.getBoardSize())
			throw new CommandExecuteException("La palabra introducida no cabe en el tablero.");
		
		if(word.length() < 2) {
			throw new CommandExecuteException("La palabra introducida debe tener al menos dos letras.");
		}
		
	}

}
