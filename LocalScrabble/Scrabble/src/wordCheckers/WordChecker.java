package wordCheckers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.CommandExecuteException;
import logic.Game;

/* APUNTES GENERALES

   Esta clase representa la funcionalidad completa de comprobar palabras que se
   desean colocar en el tablero.
   Obsérvese que la clase tiene un atributo que representa la lista de checkers.
 */
public class WordChecker {

	private List<Checker> checkers;
	
	private Game game;
	
	public WordChecker(Game game) {
		this.game = game;
		checkers = new ArrayList<Checker>();
		
		WordExistsChecker wordExistsChecker = new WordExistsChecker();
		checkers.add(wordExistsChecker);
		
		WordNotUsedChecker wordNotUsedChecker = new WordNotUsedChecker();
		checkers.add(wordNotUsedChecker);
		
		checkers.add(new DirectionChecker());
		
		checkers.add(new WordLengthChecker());
		checkers.add(new PosInRangeChecker());
		checkers.add(new WordUnionChecker());
		
		checkers.add(new WordInPosAndDirectionChecker());
		checkers.add(new EnoughLettersChecker());
		
		checkers.add(new WordInCentreChecker());
		checkers.add(new WordNextToOtherChecker());
		
		checkers.add(new NewFormedWordsChecker(wordExistsChecker, wordNotUsedChecker));
	}
	
	public void checkArguments(String word, int posX, int posY, String direction) throws CommandExecuteException {
		
		Map<String, Integer> lettersNeeded = getLettersNeeded(word);
		
		for(Checker checker : checkers)
			checker.check(game, word, posX, posY, direction, lettersNeeded);
	}
	
	/* Método getLettersNeeded:
	 * Este método auxiliar se utiliza para, dada una palabra, obtener
	 * cuantas letras se necesitan para escribirla (teniendo en cuenta
	 * que puede que alguna de las letras ya se encuentren en el tablero).
	 */
	private Map<String, Integer> getLettersNeeded(String word) {
		
		Map<String, Integer> lettersNeeded = new HashMap<String, Integer>();
		
		for (int i = 0; i < word.length(); ++i) {
			String letter = String.valueOf(word.charAt(i));
			
			if (lettersNeeded.containsKey(letter))
				lettersNeeded.put(letter, lettersNeeded.get(letter) + 1);
			
			else lettersNeeded.put(letter, 1);
		}
		
		return lettersNeeded;
	}
}
