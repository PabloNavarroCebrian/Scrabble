package logic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.json.JSONException;

import containers.Board;
import containers.GamePlayers;
import containers.GameTiles;
import exceptions.CommandExecuteException;
import server.Server;
import simulatedObjects.Player;
import simulatedObjects.Tile;
import storage.GameLoader;
import utils.StringUtils;
import wordCheckers.WordChecker;

/* APUNTES GENERALES

La clase Game encapsula toda la lógica principal de juego.

Para ello, se necesita:

- Saber si el juego ha sido inicializado, y si ya se ha colocado alguna palabra en el tablero.
- Contenedores de tipo Board, GamePlayers, GameTiles.
- El turno del jugador actual y el número consecutivo de turnos saltados.
- Conocer si se han puesto palabras en el tablero, si el juego ha terminado, y la causa de ello.
- Listas de las palabras usadas, y todas las palabras válidas.
- Un comprobador de palabras.
- Un objeto Random para establecer aleatoriedad.
- Lista de observadores para actualizar las vistas en función del modelo.

*/

public class Game {
	
	private static final int EXTRA_POINTS = 50;
	private static final int PASSED_TURNS_TO_END_GAME = 2;
	
	private static boolean _gameInitiated;
	private static boolean _wordsInBoard;
	private static boolean _gameFinished;

	public static Random _random;
	
	private Board board;
	private GamePlayers players;
	private GameTiles tiles;
	
	private int currentTurn;
	private int numConsecutivePassedTurns;
	
	private String gameFinishedCause;
	
	private List<String> usedWords;
	private static List<String> words;
	
	private WordChecker wordChecker;
	
	private int currentNumOfUpdates;
	
	private Server server;

	public Game(Server server) {
		
		_gameInitiated = false;
		_wordsInBoard = false;
		
		this.board = null;
		this.players = new GamePlayers((List<Player>) new ArrayList<Player>());
		this.tiles = null;
		
		this.currentTurn = 0;
		this.numConsecutivePassedTurns = 0;
		
		_gameFinished = false;
		gameFinishedCause = "";
		
		this.usedWords = null;
		
		this.wordChecker = new WordChecker(this);
		
		this.server = server;
		
		this.currentNumOfUpdates = 0;
	}
	
	/* Método reset:
	 * 
	 * Este método recibe desde la clase GameLoader ciertos valores con los que inicializar atributos de esta clase.
	 * 
	 * Si el fichero que se haya cargado tenía jugadores predeterminados, se llama al método addPlayers de esta clase
	 * (ver su funcionalidad).
	 * 
	 * En caso contrario, hay que pedirlos al usuario (ya sea por consola o por interfaz gráfica).
	 * 
	 * IMPORTANTE: la inicialización del atributo de la clase Random y el atributo que representa el turno actual,
	 * deben inicializarse antes de ejecutar el gestionar el añadido de jugadores (if-else), por razones de diseño.
	 */
	public void reset(int currentTurn, int numConsecutivePassedTurns, boolean wordsInBoard,
			boolean gameFinished, GamePlayers players, GameTiles tiles, Board board, List<String> usedWords) {
		
		this.board = board;
		this.tiles = tiles;
		
		_random = new Random(System.currentTimeMillis());

		this.currentTurn = currentTurn;
		this.numConsecutivePassedTurns = numConsecutivePassedTurns;
		
		_wordsInBoard = wordsInBoard;
		_gameFinished = gameFinished;
		
		this.usedWords = usedWords;
		
		this.server.sendViewAction(GameSerializer.serializeReset(board, this.getNumPlayers(), getNumPlayers() == 0 || this.currentTurn < 0 ? null : this.players.getPlayerName(this.currentTurn), this.getRemainingTiles(), this.players, this.currentTurn, _gameInitiated));
	}
	
	/* Método playTurn:
	 * Delega en la clase GamePlayers la acción de jugar un turno.
	 */
	public void playTurn() {
		if(this.currentNumOfUpdates == 0)
			this.players.playTurn(this, wordChecker);
	}
	
	/* Método writeAWord:
	 * 
	 * Lleva a cabo la acción de escribir una palabra en el tablero.
	 * 
	 * Primero convierte la palabra a un formato deseado (en minúsculas y sin acentos), y delega
	 * en la clase WordChecker la comprobación de la validez de los parámetros recibidos (cabiendo
	 * la posibilidad de que se lancen excepciones).
	 * 
	 * Si no se ha detectado ningún error en los parámetros, se añade la palabra introducida a la lista
	 * de palabras usadas. También se comprueba si el jugador ha usado todas sus fichas, en cuyo caso,
	 * se le añaden EXTRA_POINTS a los puntos que hubiese acumulado con la palabra colocada.
	 * 
	 * A continuación se delega en GamePlayers la entraga de puntos al jugador; se establece que el tablero
	 * no está vacío, y que el número de turnos consecutivos saltados es cero.
	 * 
	 * Por último, se notifica a los observadores que se ha escrito una palabra; se delega en GamePlayers
	 * el robo de fichas por parte del jugador, y se avanza el turno.
	 */
	public void writeAWord(String word, int posX, int posY, String direction) {
		
		try {
			word = StringUtils.removeAccents(word);
			word = word.toLowerCase();
			this.wordChecker.checkArguments(word, posX, posY, direction);
		}
		catch(CommandExecuteException cee) {
			this.server.sendViewAction(GameSerializer.serializeError(cee.getMessage(), currentTurn));
			return;
		}
		
		List<String> wordsToAdd = getNewFormedWords(word, posX, posY, direction);
		
		wordsToAdd.add(StringUtils.removeAccents(word.toLowerCase()));
		
		addUsedWords(wordsToAdd);
		
		int numPlayerTilesBefore = this.players.getNumPlayerTiles(this.currentTurn);
		
		assignTiles(word, posX, posY, direction);
		int points = calculatePoints(word, posX, posY, direction);
		
		int extraPoints = 0;
		if(numPlayerTilesBefore == 7 && this.players.getNumPlayerTiles(this.currentTurn) == 0)
			extraPoints = EXTRA_POINTS;
		
		players.givePoints(this.currentTurn, points + extraPoints);
		
		_wordsInBoard = true;
		numConsecutivePassedTurns = 0;
		
		this.server.sendViewAction(GameSerializer.serializeWordWritten(word, posX, posY, direction, points, extraPoints, getNumPlayers(), players, this.currentTurn, board, _gameInitiated));
		
		players.drawTiles(this, currentTurn);
	
		nextTurn();
	}
	
	/* Método decideFirstTurn:
	 * 
	 * Establece, si es necesario, el orden de juego de una partida.
	 * 
	 * Primero comprueba si el turno ya se ha inicializado (se encuentra en el
	 * intervalo [0, players.getNumPlayers()]; en caso de no haberse inicializado
	 * tendría el valor -1).
	 * 
	 * Se construye un array de letras aleatorias obtenidas por los jugadores,
	 * y se comparan en orden lexicográfico para decidir quién empieza la partida
	 * (gana quién más se acerque a la A, y en caso de empate, se prioriza el orden 
	 * en el que se cogió la letra).
	 * 
	 * Por último, se notifica a los observadores.
	 */
	public void decideFirstTurn() {
		
		if(0 <= this.currentTurn && this.currentTurn < this.players.getNumPlayers())
			return;
		
		this.currentTurn = 0;
		
		List<String> lettersObtained = new ArrayList<String>();
		
		for(int i = 0; i < this.getNumPlayers(); ++i) 
			lettersObtained.add(this.randomTile().getLetter());
	
		// La partida es empezada por quien obtenga la letra mas cercana a la A.
		for(int i = 1; i < this.getNumPlayers(); ++i)
			if (lettersObtained.get(i).compareTo(lettersObtained.get(this.currentTurn)) < 0) 
				this.currentTurn = i;
		
		initPlayerTiles();
		
		this.server.sendViewAction(GameSerializer.serializeFirstTurnDecided(lettersObtained, players, getNumPlayers(), currentTurn, _gameInitiated));
	}
	
	/* Método passTurn:
	 * 
	 * Pasa de turno en el juego (método nextTurn), incrementando previamente el número de turnos
	 * consecutivos saltados, y notificando a los observadores.
	 */
	public void passTurn() {
		++this.numConsecutivePassedTurns;
		
		this.server.sendViewAction(GameSerializer.serializePassed(getNumPlayers(), players.getPlayerName(currentTurn), null, _gameInitiated));
		
		nextTurn();
	}
	
	/* Método nextTurn:
	 * Avanza el turno actual en uno.
	 */
	private void nextTurn() {
		this.currentTurn = (this.currentTurn + 1) % this.getNumPlayers();
	}

	/* Método swapTile:
	 * 
	 * Primero comprueba que queden fichas para poder realizar un intercambio.
	 * 
	 * En caso de que así sea, se obtiene una ficha aleatoria del jugador,
	 * que es introducida en el saco, y eliminada de su mano; después el jugador
	 * obtiene su nueva ficha (acciones delegadas en los containers respectivos).
	 * 
	 * Se incrementan los turnos saltados, se notifica a los observadores y se
	 * avanza al siguiente turno (método nextTurn).
	 */
	public void swapTile() {
		
		if(tiles.getSize() <= 0) {
			
			this.server.sendViewAction(GameSerializer.serializeError("No hay fichas para robar", currentTurn));
		
			return;
		}
		
		int randomNumberForTile = (int) (getRandomDouble() * players.getNumPlayerTiles(currentTurn));
		
		Tile randomTile = players.getPlayerTile(currentTurn, randomNumberForTile);
		
		// Aniadimos la ficha al saco original
		tiles.add(randomTile);
		
		// Quitamos la ficha al jugador
		players.removePlayerTile(currentTurn, randomTile);
		
		// Le damos una ficha al jugador aleatoria
		players.drawTiles(this, currentTurn);
		
		++this.numConsecutivePassedTurns;
		
		this.server.sendViewAction(GameSerializer.serializeSwapped(getNumPlayers(), players, currentTurn, _gameInitiated));
		
		nextTurn();
	}
	
	/* Método removeTile:
	 * Delega en la clase GameTiles la acción de eliminar una ficha del saco.
	 */
	public void removeTile(Tile tile) {
		tiles.remove(tile);
	}
	
	/* Método randomTile:
	 * Devuelve una ficha aleatoria del saco (si la hay).
	 */
	public Tile randomTile() {
		if (tiles.getNumTiles() == 0)
			return null;
		return tiles.getTile((int) (this.getRandomDouble() * this.tiles.getNumTiles()));
	}
	
	/* Método calculatePoints:
	 * 
	 * Para cada letra de la palabra recibida por parámetrp, se delega en la clase Board la acción de 
	 * obtener el total de puntos asociado a esa letra (points) en esa casilla (wordMultiplier).
	 * 
	 * Durante el bucle, se obtiene el total de puntos asociados a las letras, y el total asociado a los
	 * multiplicadores de palabras (modificadores DOUBLE_WORD y TRIPLE_WORD).
	 * 
	 * Finalmente se devuelve el resultado de multiplicar los puntos de letras con los puntos de los modificadores.
	 */
	private int calculatePoints(String word, int posX, int posY, String direction) {
		
		int vertical = ("V".equalsIgnoreCase(direction) ? 1 : 0);
		int horizontal = ("H".equalsIgnoreCase(direction) ? 1 : 0);
		
		int points = 0, wordMultiplier = 1;
		
		for (int i = 0; i < word.length(); ++i) {
			points += this.board.getPoints(posX + i * vertical, posY + i * horizontal);
			wordMultiplier *= this.board.getWordMultiplier(posX + i * vertical, posY + i * horizontal);
		}
		
		points *= wordMultiplier;
		
		return points;
	}

	/* Método assignTiles:
	 * 
	 * Por cada letra de la palabra recibida por parámetro, se obtiene la ficha asociada a esa letra (acción delegada
	 * en la clase GamePlayers), se asigna dicha dicha en el tablero (acción delegada en la clase Board), y se elimina
	 * dicha ficha de la mano del jugador (acción delegada en la clase GamePlayers).
	 */
	public void assignTiles(String word, int posX, int posY, String direction) {
		
		int vertical = ("V".equalsIgnoreCase(direction) ? 1 : 0);
		int horizontal = ("H".equalsIgnoreCase(direction) ? 1 : 0);
		
		for (int i = 0; i < word.length(); ++i) {
			
			Tile tile = this.players.getPlayerTile(this.currentTurn, String.valueOf(word.charAt(i)));
			
			try {
				this.board.assignTile(tile, posX + i * vertical, posY + i * horizontal);
				this.players.removePlayerTile(this.currentTurn, tile);
			}
			
			catch (IllegalArgumentException iae) {};
		}
	}

	/* Método update:
	 * 
	 * Comprueba si el juego ha terminado por alguna de las siguientes razones:
	 * - No quedan fichas en el saco y el jugador anterior al actual no tiene tampoco.
	 * - Todos los jugadores han pasado su turno PASSED_TURNS_TO_END_GAME veces.
	 * 
	 * En caso de que alguna se dé, se registra la causa del final de la partida.
	 * 
	 * Después notifica a los observadores para que actualicen su estado, 
	 * y si el juego se ha acabado, también se les notifica de ello.
	 */
	public void update() {
		
		this.currentNumOfUpdates = (this.currentNumOfUpdates + 1) % this.server.getNumHumanPlayers();
		
		if(this.currentNumOfUpdates == 0) {
			
			if(_gameInitiated) {
				// Si no quedan fichas en el saco, y el jugador actual no tiene fichas
				if(this.getRemainingTiles() == 0 && 
						this.players.getNumPlayerTiles((this.currentTurn + this.getNumPlayers() - 1) % this.getNumPlayers()) == 0) {
					_gameFinished = true;
					
					gameFinishedCause = "La partida ha finalizado: no quedan fichas para robar y el jugador " 
					+ this.players.getPlayerName((this.currentTurn + this.getNumPlayers() - 1) % this.getNumPlayers())
					+ " se ha quedado sin fichas."+ StringUtils.DOUBLE_LINE_SEPARATOR;
				}
				
				if(this.numConsecutivePassedTurns == this.getNumPlayers() * PASSED_TURNS_TO_END_GAME) {
					_gameFinished = true;
					gameFinishedCause = "La partida ha finalizado: todos los jugadores han pasado " 
							+ PASSED_TURNS_TO_END_GAME + " turnos." + StringUtils.LINE_SEPARATOR;
				}
			}
			
			if(getGameIsFinished())			
				this.server.sendViewAction(GameSerializer.serializeEnd(gameFinishedCause + getWinnerName()));
			
			else
				this.server.sendViewAction(GameSerializer.serializeUpdate(getGameIsFinished(), this.getNumPlayers(), this.getRemainingTiles(), this.players.getPlayerName(this.currentTurn), this.players, this.currentTurn, _gameInitiated));
		}
	}
	
	/* Método getNewFormedWords:
	 * Este método añade al array de palabras colocadas en el tablero
	 * toda palabra que se haya podido formar al colocar otra.
	 * Para cada letra de la palabra, dependiendo de si se colocó
	 * vertical u horizontalmente, se recorren sus posiciones perpendiculares
	 * para ver si efectivamente se ha formado alguna palabra nueva.
	 */
	
	private List<String> getNewFormedWords(String word, int posX, int posY, String direction) {
		
		int vertical = ("V".equalsIgnoreCase(direction) ? 1 : 0);
		int horizontal = ("H".equalsIgnoreCase(direction) ? 1 : 0);
		
		List<String> newFormedWords = new ArrayList<String>();
		
		for(int i = 0; i < word.length(); i++) {
			String newWord = getWordFormed(String.valueOf(word.charAt(i)), posX + i * vertical, posY + i * horizontal, horizontal, vertical);
			if(newWord != null && newWord.length() != 1) {
				newFormedWords.add(StringUtils.removeAccents(newWord.toLowerCase()));
			}
		}
		
		return newFormedWords;
	}
	
	/* Método getWordFormed:
	 * Dependiendo de la orientación (vertical u horizontal; vertical = 1
	 * significa que esta es la dirección tomada y análogamente con horizontal),
	 * devuelve la palabra formada por la letra recibida por parámetro
	 * en las posiciones especificadas también por parámetro.
	 */
	
	private String getWordFormed(String letter, int posX, int posY, int vertical, int horizontal) {
		
		if(this.board.getTile(posX, posY) != null)
			return null;
		
		int auxPosX = posX - vertical;
		int auxPosY = posY - horizontal;
		
		String word = letter;
		
		while(auxPosX >= 0 && auxPosY >= 0 && this.board.getTile(auxPosX, auxPosY) != null) {
			word = this.board.getTile(auxPosX, auxPosY).getLetter() + word;
			auxPosX -= vertical;
			auxPosY -= horizontal;
		}
		
		auxPosX = posX + vertical;
		auxPosY = posY + horizontal;
		
		while(auxPosX < this.getBoardSize() && auxPosY < this.getBoardSize() && this.board.getTile(auxPosX, auxPosY) != null) {
			word += this.board.getTile(auxPosX, auxPosY).getLetter();
			auxPosX += vertical;
			auxPosY += horizontal;
		}
		
		return word;
	}
	
	/* Método getWinnerGame:
	 * 
	 * Devuelve un mensaje detallando quién o quiénes han ganado la partida.
	 * Delega parte de la acción en la clase GamePlayers (método getWinners).
	 */
	private String getWinnerName() {
		List<Integer> winners = this.players.getWinners();
		String winnersMessage = null;
		
		if(winners.size() == 1) {
			winnersMessage = "¡" + this.players.getPlayerName(winners.get(0)) + " gana la partida ";
		}
		else {
			winnersMessage = "Ha habido un empate... ¡";
			for(int i = 0; i < winners.size(); ++i) {
				winnersMessage += this.players.getPlayerName(winners.get(i));
				if(i < winners.size() - 2)
					winnersMessage += ", ";
				else if (i == winners.size() - 2)
					winnersMessage += " y ";
			}
			
			winnersMessage += " ganan la partida ";
		}
		
		winnersMessage += String.format("con %s puntos!", this.players.getPlayerPoints(winners.get(0)));
		
		return winnersMessage;
	}
	
	/* Método addUsedWord
	 * Añade la palabra recibida por parámetro a la lista de palabras usadas.
	 * La lista se mantiene siempre ordenada para poder realizar una búsqueda eficiente.
	 */
	public void addUsedWords(List<String> words) {
		this.usedWords.addAll(words);
		Collections.sort(this.usedWords);
	}
	
	/* Método initWordList:
	 * Este método estático es llamado desde la clase Main, y delega estáticamente
	 * en la clase GameLoader la carga de las palabras válidas de la partida.
	 */
	public static void initWordList() throws JSONException, FileNotFoundException {
		words = GameLoader.loadWordList();
	}

	
	/* Método initPlayerTiles:
	 * Completa las fichas que a los jugadores les pueda faltar.
	 */
	public void initPlayerTiles() {
		for(int i = 0; i < this.players.getNumPlayers(); i++) {
			players.drawTiles(this, i);
		}
	}

	
	/* Método userExits:
	 * Actualiza el estado del juego a finalizado, y establece la causa de ello.
	 * Notifica a los observadores de la terminación de la partida.
	 * Además, distingue si se ha terminado la partida con jugadores inicializados,
	 * o nada más empezarla.
	 */
	public void userExits() {
		
		_gameFinished = true;
		
		boolean anyPlayer = this.players.getNumPlayers() > 0;
		
		if(anyPlayer) {
			gameFinishedCause = "El jugador " 
					+ this.players.getPlayerName(this.currentTurn) 
					+ " ha finalizado la partida." 
					+ StringUtils.DOUBLE_LINE_SEPARATOR;
		}
		
		else gameFinishedCause = "Juego finalizado.";
		
		String finalMessage = gameFinishedCause;
		
		if(anyPlayer)
			finalMessage += getWinnerName();
		
		this.server.sendViewAction(GameSerializer.serializeEnd(finalMessage));
	}
	
	/* Método movementNeeded:
	 * Notifica a cada observador que es necesario un movimiento
	 * para continuar la partida.
	 */
	public void movementNeeded() {
		this.server.sendViewAction(GameSerializer.serializeOnMovementNeeded(currentTurn));
	}
	
	// Getters
	
	public static boolean getWordsInBoard() {
		return _wordsInBoard;
	}
	
	public static boolean getGameIsFinished() {
		return _gameFinished;
	}
	
	public static Random getRandom() {
		return _random;
	}
	
	public int getBoardSize() {
		return board.getBoardSize();
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public int getNumPlayers() {
		return this.players.getNumPlayers();
	}
	
	public GamePlayers getPlayers() {
		return this.players;
	}

	public int getRemainingTiles() {
		return this.tiles.getNumTiles();
	}
	
	public int getCurrentTurn() {
		return this.currentTurn;
	}
	
	public List<String> getWordsList() {
		return Collections.unmodifiableList(words);
	}

	public List<String> getUsedWords() {
		return Collections.unmodifiableList(usedWords);
	}

	private Double getRandomDouble() {
		return _random.nextDouble();
	}

	public void addNewHumanPlayer(String name) {
		this.players.addNewHumanPlayer(name);
		if(players.getNumPlayers() == (this.server.getNumHumanPlayers() + this.server.getNumAutomaticPlayers()))
			_gameInitiated = true;
	}
	
	public void addAutomaticPlayer(String strategy) {
		this.players.addNewAutomaticPlayer(strategy);
	}

	public void register() {
		this.server.sendViewAction(GameSerializer.serializeRegister(board, this.getNumPlayers(), this.players, currentTurn, _gameInitiated));
	}
}
