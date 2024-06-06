package wordCheckers;

import java.util.Collections;
import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES
 * Realiza una búsqueda binaria de la palabra recibida como parámetro en la lista de palabras usadas (ordenada), 
 * y en caso de no encontrarse, se lanza una excepción.
 */
public class WordNotUsedChecker implements Checker {

	// Sobreescritura del método check
	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		if(Collections.binarySearch(game.getUsedWords(), word.toLowerCase()) >= 0)
			throw new CommandExecuteException("La palabra " + "\"" + word.toUpperCase() + "\" ya se encuentra en el tablero.");
	}

}
