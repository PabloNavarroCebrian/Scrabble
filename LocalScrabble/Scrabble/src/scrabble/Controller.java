package scrabble;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import command.Command;
import containers.GamePlayers;
import exceptions.CommandExecuteException;
import exceptions.CommandParseException;
import logic.Game;
import storage.GameLoader;
import storage.GameSaver;
import view.ScrabbleObserver;

/* APUNTES GENERALES
   
   La clase Controller es la encargada de conectar el modelo y la vista usada.
   
   Sus atributos son:
   - Instancia de Game: para poder conectarse con la lógica del juego.
   - Un String que representa el último fichero usado. Así, se puede controlar
     a qué partida volvemos cuando se invoca el comando reset.
   
 */
public class Controller {
	
	private Game game;
	
	private String lastFileUsed;
	
	Controller() {
		this.game = new Game();
		this.lastFileUsed = null;
	}
	
	/* Método playTurn:
	 * Delega en la clase Game la acción de jugar un turno, ya sea por un jugador humano, o por uno automático
	 */
	public void playTurn() {
		this.game.playTurn();
	}
	
	/* Método executeCommand:
	 * Delega en la clase Command la acción de ejecutar una cierta acción.
	 */
	public void executeCommand(String[] parameters, Scanner in, PrintStream out) throws CommandExecuteException, CommandParseException {
		Command command = Command.getCommand(parameters);
		
		if(command != null) {
			command.execute(this, in, out);
		}
	}
	
	/* Método writeAWord:
	 * Delega en la clase Game la acción de escribir una palabra en el tablero.
	 */
	public void writeAWord(String word, int posX, int posY, String direction) {
		game.writeAWord(word, posX, posY, direction);
	}
	
	/* Método passTurn:
	 * Delega en la clase Game el paso de turno.
	 */
	public void passTurn() {
		this.game.passTurn();
	}
	
	/* Método swapTile:
	 * Delega en la clase Game la acción de intercambiar una ficha.
	 * Devuelve un booleano indicando si se ha podido realizar dicho intercambio.
	 */
	public void swapTile() {
		this.game.swapTile();
	}
	
	/* Método userExits:
	 * Delega en la clase Game la terminación del juego.
	 */
	public void userExits() {
		game.userExits();
	}
	
	/* Método reset:
	 * Si el fichero usado más recientemente es nulo, entonces
	 * significa que no se había cargado anteriormente una partida,
	 * así que se debe resetear el juego creando una partida nueva.
	 * En caso contrario, se reseta el juego cargando la partida 
	 * asociada al fichero usado más reciente.
	 */
	public void reset() throws FileNotFoundException {
		
		if(this.lastFileUsed == null)
			newGame();
		
		else loadGame(this.lastFileUsed);
	}

	/* Método update:
	 * Delega en la clase Game la actualización del estado de la partida.
	 */
	public void update() {
		this.game.update();
	}

	/* Método newGame:
	 * Delega en la clase Game el reseteo de los jugadores.
	 * Delega en la clase GameLoader la creación de una nueva partida 
	 * Establece el último fichero usado a nulo.
	 */
	public void newGame() throws FileNotFoundException {
		
		this.lastFileUsed = null;
		GameLoader.newGame(game);
	}
	
	/* Método loadGame:
	 * Delega en la clase Game el reseteo de los jugadores.
	 * Delega en la clase GameLoader la carga del juego a partir 
	 * del fichero recibido por parámetro, y establece este último
	 * como el fichero usado más reciente.
	 * La excepción lanzada (fichero no encontrado), es recogida en
	 * el método execute de la clase LoadCommand.
	 */
	public void loadGame(String file) throws FileNotFoundException {
		
		this.lastFileUsed = file;
		GameLoader.loadGame(game, file);
	}
	
	/* Método saveGame:
	 * Delega en la clase GameSaver el guardado de partida.
	 * La excepción lanzada (fichero no encontrado), es recogida en
	 * el método execute de la clase SaveCommand.
	 */
	public void saveGame(String file) throws FileNotFoundException {
		GameSaver.saveGame(this.game, file);
	}
	
	/* Método addPlayers:
	 * Delega en la clase Game la inicialización de los jugadores del juego.
	 */
	public void addOrChangePlayers(GamePlayers players) {
		this.game.addOrChangePlayers(players);
	}

	/* Método addObserver:
	 * Delega en la clase Game la acción de añadir un observador del modelo en la partida.
	 */
	public void addObserver(ScrabbleObserver o) {
		this.game.addObserver(o);
	}
	
	/* Método removeObserver:
	 * Delega en la clase Game la acción de eliminar un observador del modelo en la partida.
	 */
	public void removeObserver(ScrabbleObserver o) {
		this.game.removeObserver(o);
	}
}
