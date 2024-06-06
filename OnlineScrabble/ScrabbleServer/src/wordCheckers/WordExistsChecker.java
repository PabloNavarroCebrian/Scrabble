package wordCheckers;

import java.util.Collections;
import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES
   Realiza una búsqueda binaria de la palabra recibida como parámetro en la lista de palabras válidas (ordenada), 
   y en caso de no encontrarse, se lanza una excepción.
*/
public class WordExistsChecker implements Checker {
	
    // Sobreescritura del método check.

	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		if(Collections.binarySearch(game.getWordsList(), word.toLowerCase()) < 0) {
			String message = (word.length() == 0 ? "¡No has escrito ninguna palabra!" : "La palabra " + "\"" + word.toUpperCase() + "\" no existe.");
			throw new CommandExecuteException(message);

		}
	}
}
