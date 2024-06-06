package view;

import java.util.List;

import containers.Board;
import containers.GamePlayers;

/* APUNTES GENERALES:
   
   La intefaz SrabbleObserver es implementada por todas aquellas clases que actúen
   de observadores. Así, cada vez que se produce un cambio en el modelo, se notifica
   a los observadores para que efectúen los cambios oportunos.
   
 */
public interface ScrabbleObserver {

	/* Método onWordWritten:
	 * Es llamado cuando una palabra ha sido escrita en el tablero.
	 */
	void onWordWritten(String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn, Board board, boolean gameInitiated);
	
	/* Método onPassed:
	 * Es llamado cuando un jugador ha pasado de turno.
	 */
	void onPassed(int numPlayers, String currentPlayerName, boolean gameInitiated);
	
	/* Método onSwapped:
	 * Es llamado cuando un jugador ha intercambiado una ficha.
	 */
	void onSwapped(int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated);
	
	/* Método onRegister:
	 * Es llamado cuando un observador se añade a la lista de observadores.
	 */
	void onRegister(Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated);
	
	/* Método onReset:
	 * Es llamado cuando un se ejecuta el comando reset.
	 */
	void onReset(Board board, int numPlayers, String currentPlayerName, int remainingTiles, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated);
	
	/* Método onError:
	 * Es llamado cuando un error ha ocurrido.
	 */
	void onError(String error, int currentTurn);
	
	/* Método onUpdate:
	 * Es llamado cuando se actualiza el juego (método update de Game).
	 */
	void onUpdate(boolean gameFinished, int numPlayers, int remainingTiles, String currentPlayerName, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated);
	
	/* Método onEnd:
	 * Es llamado cuando se ha alcanzado el final de la partida.
	 */
	void onEnd(String message);
	
	/* Método onFirstTurnDecided:
	 * Es llamado cuando se ha elegido el orden de turnos de la partida.
	 */
	void onFirstTurnDecided(List<String> lettersObtained, GamePlayers gamePlayers, int numPlayers, int currentTurn, boolean gameInitiated);
	
	/* Método onMovementNeeded:
	 * Es llamado cuando se la partida requiere de una acción para seguir avanzando.
	 */
	void onMovementNeeded(int currentTurn);
}
