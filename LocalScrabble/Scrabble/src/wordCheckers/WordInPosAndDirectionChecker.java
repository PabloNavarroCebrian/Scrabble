package wordCheckers;

import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES:
   Ver funcionalidad en los métodos de la clase.
*/

public class WordInPosAndDirectionChecker implements Checker {
	
	// Sobreescritura del método check
	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		
		int vertical = ("V".equalsIgnoreCase(direction) ? 1 : 0);
		int horizontal = ("H".equalsIgnoreCase(direction) ? 1 : 0);
		
		for (int i = 0; i < word.length(); ++i)
			checkLetterInPos(game, String.valueOf(word.charAt(i)), posX + i * vertical, posY + i * horizontal, lettersNeeded);
	}
	
	/* Método checkLetterInPos:
	 * Comprueba que las posiciones introducidas se ajusten al rango permitido;
	 * que en caso de solapamiento de fichas, la letra recibida por parámetro y la 
	 * letra de la ficha en el tablero coincidan; que en caso de que la casilla esté 
	 * vacía, el jugador tenga la letra correspondiente.
	 * 
	 * Además, si la casilla contiene una ficha se resta una ocurrencia al 
	 * mapa de letras necesitadas para formar una palabra.
	 */
	
	public void checkLetterInPos(Game game, String letter, int posX, int posY, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		
		checkPosInRange(posX, posY, game);
		
		if (game.getBoard().getTile(posX, posY) != null && !game.getBoard().getTile(posX, posY).getLetter().equalsIgnoreCase(letter))
			throw new CommandExecuteException(String.format("En la casilla (%s,%s) está la letra %s que no coincide con tu palabra.", posX, posY, game.getBoard().getTile(posX, posY).getLetter()));
		
		if (game.getBoard().getTile(posX, posY) == null && !game.getPlayers().playerHasLetter(game.getCurrentTurn(), letter))
			throw new CommandExecuteException("No tienes la letra \"" + letter + "\" y no se encuentra en la casilla indicada.");
		
		if (game.getBoard().getTile(posX, posY) != null)
			lettersNeeded.put(letter, lettersNeeded.get(letter) - 1);
	}
	
	/* Método checkPosInRange:
	 * Comprueba que la posición de la palabra introducida se encuentre en el rango permitido.
	 * En caso contrario, se lanza una excepción.
	 */
	
	private void checkPosInRange(int posX, int posY, Game game) throws CommandExecuteException {
		if(posX < 0 || posX > game.getBoardSize() - 1 || posY < 0 || posY > game.getBoardSize() - 1)
			throw new CommandExecuteException("La posición introducida se sale del tablero.");
	}

}
