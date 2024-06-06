package storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import containers.Board;
import containers.BoardBuilder;
import containers.GamePlayers;
import containers.GamePlayersBuilder;
import containers.GameTiles;
import containers.GameTilesBuilder;
import logic.Game;
import logic.WordsBuilder;
import simulatedObjects.BoxBuilder;
import simulatedObjects.PlayerBuilder;
import simulatedObjects.TileBuilder;
import strategies.EasyStrategyBuilder;
import strategies.HardStrategyBuilder;
import strategies.HumanStrategyBuilder;
import strategies.MediumStrategyBuilder;
import strategies.StrategyBuilder;

/* APUNTES GENERALES:
   
   La clase GameLoader es la encargada de cargar una partida, ya sea nueva o existente.
   
   La clase tiene como atributos estáticos instancias de builders:
   - BoardBuilder
   - GameTilesBuilder
   - GamePlayersBuilder
   - WordsBuilder
   
 */
public class GameLoader {
	
	private static final String wordsFile = "resources/files/words.json";
	public static final String NEW_GAME = "resources/existingGames/new_game.json";
	
	private static BoardBuilder boardBuilder;
	private static GameTilesBuilder gameTilesBuilder;
	private static GamePlayersBuilder gamePlayersBuilder;
	private static WordsBuilder wordsBuilder = new WordsBuilder();
	
	/* Método newGame:
	 * Delega la creación de la partida al método createGame.
	 * En este caso, se crea una nueva partida (fichero NEW_GAME).
	 */
	public static void newGame(Game game) throws FileNotFoundException {
		createGame(new FileInputStream(NEW_GAME), game);
	}
	
	/* Método createGame:
	 * Recoge en un objeto JSON todos los datos necesarios para crear o cargar una partida.
	 * Delega la inicialización de los atributos del parámetro recibido de la clase Game, en
	 * el método reset.
	 */
	private static void createGame(InputStream input, Game game) {
		
		JSONObject json = new JSONObject(new JSONTokener(input));
		
		int currentTurn = json.getInt("current_turn"); // -1 si es partida nueva
		int numConsecutivePassedTurns = json.getInt("consecutive_turns_passed");
		boolean wordsInBoard = json.getBoolean("words_in_board");
		boolean gameFinished = json.getBoolean("game_finished");
		
		GamePlayers players = gamePlayersBuilder.createGamePlayers(json.getJSONObject("game_players"));
		
		GameTiles tiles = gameTilesBuilder.createGameTiles(json.getJSONObject("game_tiles"));
		Board board = boardBuilder.createBoard(json.getJSONObject("game_board"));
		
		List<String> usedWords = wordsBuilder.createWords(json.getJSONObject("used_words"));
		
		game.reset(currentTurn, numConsecutivePassedTurns, wordsInBoard, gameFinished, 
				players, tiles, board, usedWords);
	}
		
	
	/* Método loadWordList:
	 * Devuelve, mediante la clase WordsBuilder, la lista formada por todas las palabras válidas del juego.
	 */
	public static List<String> loadWordList() throws JSONException, FileNotFoundException {
		
		JSONObject jo = new JSONObject(new JSONTokener(new FileInputStream(wordsFile)));
		return wordsBuilder.createWords(jo);
	}
	
	/* Método initBuilders:
	 * Inicializa todos los builders necesarios para cargar o crear una partida.
	 * Se llama estáticamente desde el main.
	 */
	public static void initBuilders() {
		
		TileBuilder tileBuilder = new TileBuilder();
		
		boardBuilder = new BoardBuilder(new BoxBuilder(tileBuilder));
		
		gameTilesBuilder = new GameTilesBuilder(tileBuilder);
		
		List<StrategyBuilder> strategyBuilders = new ArrayList<StrategyBuilder>();
		
		strategyBuilders.add(new EasyStrategyBuilder());
		strategyBuilders.add(new MediumStrategyBuilder());
		strategyBuilders.add(new HardStrategyBuilder());
		strategyBuilders.add(new HumanStrategyBuilder());
		
		PlayerBuilder playerBuilder = new PlayerBuilder(tileBuilder, strategyBuilders);
		gamePlayersBuilder = new GamePlayersBuilder(playerBuilder);
	}
}
