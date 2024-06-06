package wordCheckers;

import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* Interfaz Checker:
 * Define el comportamiento de cualquier comprobador de palabras.
 */
public interface Checker {
	void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded) throws CommandExecuteException;
}
