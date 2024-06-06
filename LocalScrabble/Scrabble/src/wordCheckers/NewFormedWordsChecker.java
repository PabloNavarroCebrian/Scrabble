package wordCheckers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;
import utils.StringUtils;

/* APUNTES GENERALES
   Comprueba que las palabras extras (obtenidas mediante el método getWordFormed), 
   que se hayan podido formar al colocar una palabra en tablero sean válidas.
   En caso contrario, se lanza una excepción.
*/
public class NewFormedWordsChecker implements Checker {
	
	
	private WordExistsChecker checkerWordExists;
	private WordNotUsedChecker checkerWordNotUsed;
	
	public NewFormedWordsChecker(WordExistsChecker checkerWordExists, WordNotUsedChecker checkerWordNotUsed) {
		this.checkerWordExists = checkerWordExists;
		this.checkerWordNotUsed = checkerWordNotUsed;
	}

	// Sobreescritura del método check
	@Override
	public void check(Game game, String word, int posX, int posY, String direction, Map<String, Integer> lettersNeeded)
			throws CommandExecuteException {
		int vertical = ("V".equalsIgnoreCase(direction) ? 1 : 0);
		int horizontal = ("H".equalsIgnoreCase(direction) ? 1 : 0);
		
		List<String> newFormedWords = new ArrayList<String>();
		
		for(int i = 0; i < word.length(); i++) {
			String newWord = getWordFormed(game, String.valueOf(word.charAt(i)), posX + i * vertical, posY + i * horizontal, horizontal, vertical);
			if(newWord != null && newWord.length() > 1) {
				try {
					this.checkerWordExists.check(game, newWord, posX, posY, direction, lettersNeeded);
					this.checkerWordNotUsed.check(game, newWord, posX, posY, direction, lettersNeeded);
					
					if(Collections.binarySearch(newFormedWords, StringUtils.removeAccents(newWord.toLowerCase())) > 0)
						throw new CommandExecuteException("La palabra " + "\"" + newWord.toUpperCase() + "\" ya se encuentra en el tablero.");
					
					newFormedWords.add(newWord);
				}
				
				catch(CommandExecuteException cee) {
					throw new CommandExecuteException("Se forma la palabra adicional " + "\"" + newWord.toUpperCase() + "\".%n" + cee.getMessage(), cee);
				}
			}
		}
	}
	
	/* Método getWordFormed:
	 * Para cada letra, se recorre vertical u horizontalmente (solo una de ellas)
	 * el resto de casillas para ir formando la palabra que se forma.
	 */
	private String getWordFormed(Game game, String letter, int posX, int posY, int vertical, int horizontal) {
		
		if(game.getBoard().getTile(posX, posY) != null)
			return null;
		
		int auxPosX = posX - vertical;
		int auxPosY = posY - horizontal;
		
		String word = letter;
		
		while(auxPosX >= 0 && auxPosY >= 0 && game.getBoard().getTile(auxPosX, auxPosY) != null) {
			word = game.getBoard().getTile(auxPosX, auxPosY).getLetter() + word;
			auxPosX -= vertical;
			auxPosY -= horizontal;
		}
		
		auxPosX = posX + vertical;
		auxPosY = posY + horizontal;
		
		while(auxPosX < game.getBoardSize() && auxPosY < game.getBoardSize() && game.getBoard().getTile(auxPosX, auxPosY) != null) {
			word += game.getBoard().getTile(auxPosX, auxPosY).getLetter();
			auxPosX += vertical;
			auxPosY += horizontal;
		}
		
		return word;
	}

}
