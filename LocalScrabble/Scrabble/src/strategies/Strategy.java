package strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exceptions.CommandExecuteException;
import logic.Game;
import simulatedObjects.Tile;
import utils.Pair;
import wordCheckers.WordChecker;

/* APUNTES GENERALES

   Esta interfaz asocia el comportamiento de estrategia, para cualquier clase que la implemente.
   El método a sobreescribir es play. El resto de métodos implementados son comunes a toda estrategia. 
 */

public interface Strategy {
	
	static final List<Pair<Integer, Integer>> movingBoxes = 
			Collections.unmodifiableList(
					new ArrayList<Pair<Integer, Integer>>() {
						private static final long serialVersionUID = 1L;

						{
							add(new Pair<Integer, Integer>(0, -1));
							add(new Pair<Integer, Integer>(-1, 0));
							add(new Pair<Integer, Integer>(0, 1));
							add(new Pair<Integer, Integer>(1, 0));
						}
					}
			);
	
	void play(Game game, WordChecker wordChecker, List<Tile> tilesForWord);
	
	/* Método tryWritingInBoard:
	 * 
	 * Este método es usado por los jugadores automáticos para intentar 
	 * colocar una palabra en el tablero, y devuelve un booleano
	 * indicando si se ha podido escribir una palabra o no.
	 * 
	 * Primero se comprueba si el tablero es vacío o no, pues dependiendo de ello, se podrá usar
	 * una ficha más o no (extraTile).
	 * 
	 * Después, se crea un marcaje para las letras usadas y así poder controlar cuáles se han usado ya.
	 * 
	 * Si el tablero no está vacío (extraTile == 1), con el objetivo de que el jugador automático sea 
	 * lo más uniforme posible al colocar palabras, se crean una fila y una columna aleatorias (dentro del rango del tablero),
	 * desde las cuales el jugador automático empezará a intentar formar palabras (método tryWritingAWord).
	 * 
	 *	   Puesto que se desea encadenar palabras, si la posición comprobada no tiene una ficha, seguimos buscando.
	 *	  
	 *	   En caso contrario:
	 *	   - Se añade esta ficha a la lista de fichas disponibles recibida por parámetro.
	 *	   - Se intenta escribir una palabra con los parámetros indicados (la palabra empieza siendo vacía).
	 *	   - Se elimina la última ficha añadida de la lista de fichas disponibles recibida por parámetro.
	 * 
	 * En caso de que el tablero estuviese vacío (extraTile == 0), 
	 * simplemente se llamará al método tryWritingAWord, indicando que no hay palabras en el tablero.
	 */
	
	default boolean tryWritingInBoard(int wordLength, List<Tile> playerTiles, Game game, WordChecker wordChecker) {
		
		int extraTile = (Game.getWordsInBoard() ? 1 : 0);
		
		List<Tile> tilesForWord = new ArrayList<Tile>(playerTiles);
		
		if(wordLength > tilesForWord.size() + extraTile)
			return false;
		
		boolean wordWritten = false;
		List<Boolean> marks = createMarks(tilesForWord.size() + extraTile);
		
		if(extraTile == 1) {
			
			int randomRow = (int) Game.getRandom().nextDouble() * game.getBoardSize();
			int randomColumn = (int) Game.getRandom().nextDouble() * game.getBoardSize();
			
			for(int i = 0; i < game.getBoardSize() && !wordWritten; ++i)
				for(int j = 0; j < game.getBoardSize() && !wordWritten; ++j)  {
					
					int posX = (i + randomRow) % game.getBoardSize();
					int posY = (j + randomColumn) % game.getBoardSize();
					
					if(game.getBoard().getTile(posX, posY) != null) {
						tilesForWord.add(game.getBoard().getTile(posX, posY));
						wordWritten = tryWritingAWord("", wordLength, tilesForWord, marks, posX, posY, -1, game, true, wordChecker);
						tilesForWord.remove(tilesForWord.size() - 1);
					}
				}
			}
		
		else wordWritten = tryWritingAWord("", wordLength, tilesForWord, marks, -1, -1, -1, game, false, wordChecker);
		
		return wordWritten;
	}
	
	/* Método tryWritingAWord:
	 * 
	 * Se trata de un método que sigue la técnica de vuelta atrás (backtracking) para realizar
	 * combinaciones con repetición de las letras de las fichas de la lista "tiles" y de longitud "length".
	 * 
	 * Cuando existe una posible solución (se ha formado una palabra de tamaño length), se llama al método
	 * tryDirectionsForWord. Si no, se llama recursivamente al método tryWritingAWord hasta que se alcance el tamaño length.
	 * 
	 * En el momento en el que una palabra haya sido escrita, se devolverá true en todas las llamadas recursivas.
	 */
	default boolean tryWritingAWord(String word, int wordLength, List<Tile> tilesForWord, List<Boolean> marks, int posX, int posY, int posBoardTile, Game game, boolean wordsInBoard, WordChecker wordChecker) {
		
		for(int i = 0; i < tilesForWord.size(); ++i) {
			
			// Si la letra ya se ha usado, no entra.
			if(!marks.get(i)) {
				
				marks.set(i, Boolean.TRUE);
				
				if(i == tilesForWord.size() - 1)
					posBoardTile = word.length();
				
				word += tilesForWord.get(i).getLetter();
				
				boolean wordWritten = false;
				
				// Hay posible solución
				if(word.length() == wordLength)
					wordWritten = tryDirectionsForWord(word, posX, posY, posBoardTile, game, wordsInBoard, wordChecker);
				
				else wordWritten = tryWritingAWord(word, wordLength, tilesForWord, marks, posX, posY, posBoardTile, game, wordsInBoard, wordChecker);
				
				if(wordWritten) 
					return true;
				
				word = word.substring(0, word.length() - 1);
				
				marks.set(i, Boolean.FALSE);
			}
		}
		
		return false;
	}
	
	/* Método tryDirectionsForWord:
	 * Devuelve un booleano indicando si se ha podido escribir la palabra recibida por parámetro.
	 * 
	 * Si no hay palabras en el tablero, se intenta escribir en la casilla central (50% de probabilidad para la dirección).
	 * 
	 * Si posBoardTile != -1, se recorre el array de posiciones y se intenta escribir una palabra
	 * con las características establecidas.
	 */
	default boolean tryDirectionsForWord(String word, int posX, int posY, int posBoardTile, Game game, boolean wordsInBoard, WordChecker wordChecker) {
		
		if(wordsInBoard && posBoardTile == -1)
			return false;
		
		if(!wordsInBoard) {
			
			Pair<Integer, Integer> center = game.getBoard().getCenter();
			
			int newPosX = center.getFirst(), newPosY = center.getSecond();
			String direction = (Game.getRandom().nextDouble() < 0.5 ? "V" : "H");
			
			try {
				wordChecker.checkArguments(word, newPosX, newPosY, direction);
				game.writeAWord(word, newPosX, newPosY, direction);
				return true;
			}
			catch (CommandExecuteException cee) {}	
		}
		
		else if(posBoardTile != -1){
			
			for(Pair<Integer, Integer> move : movingBoxes) {
				
				int newPosX = posX, newPosY = posY;
				
				String direction;
				
				if(move.getFirst().equals(0)) {
					direction = "H";
					newPosY -= posBoardTile;
				}
				
				else {
					direction = "V";
					newPosX -= posBoardTile;
				}
				
				try {
					wordChecker.checkArguments(word, newPosX, newPosY, direction);
					game.writeAWord(word, newPosX, newPosY, direction);
					return true;
				}
				catch (CommandExecuteException cee) {}	
			}
		}
		
		return false;
	}
	
	/* Método createMarks:
	 * Crea un marcaje de booleanos inicializados a false
	 * de tamaño el recibido por parámetro.
	 */
	default List<Boolean> createMarks(int size) {
		
		List<Boolean> marks = new ArrayList<Boolean>();
		for(int i = 0; i < size; ++i)
			marks.add(Boolean.FALSE);
		
		return marks;
	}
	
	@Override
	public String toString();
}
