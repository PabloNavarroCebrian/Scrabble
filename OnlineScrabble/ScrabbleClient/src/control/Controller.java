package control;

import client.Client;
import view.ScrabbleObserver;

/* APUNTES GENERALES
   
   La clase Controller es la encargada de conectar el modelo y la vista usada.
   
   Sus atributos son:
   - Instancia de Game: para poder conectarse con la lógica del juego.
   - Un String que representa el último fichero usado. Así, se puede controlar
     a qué partida volvemos cuando se invoca el comando reset.
   
 */
public class Controller {
	
	private Client client;
	
	public Controller(Client client) {
		this.client = client;
	}
	
	/* Método playTurn:
	 * Delega en la clase Game la acción de jugar un turno, ya sea por un jugador humano, o por uno automático
	 */
	public void playTurn() {
		this.client.sendGameAction(ControllerSerializer.serializePlayTurn());
	}
	
	/* Método writeAWord:
	 * Delega en la clase Game la acción de escribir una palabra en el tablero.
	 */
	public void writeAWord(String word, int posX, int posY, String direction) {
		this.client.sendGameAction(ControllerSerializer.serializeWriteAWord(word, posX, posY, direction));
	}
	
	/* Método passTurn:
	 * Delega en la clase Game el paso de turno.
	 */
	public void passTurn() {
		this.client.sendGameAction(ControllerSerializer.serializePassTurn());
	}
	
	/* Método swapTile:
	 * Delega en la clase Game la acción de intercambiar una ficha.
	 * Devuelve un booleano indicando si se ha podido realizar dicho intercambio.
	 */
	public void swapTile() {
		this.client.sendGameAction(ControllerSerializer.serializeSwapTile());
	}
	
	/* Método userExits:
	 * Delega en la clase Game la terminación del juego.
	 */
	public void userExits() {
		this.client.sendGameAction(ControllerSerializer.serializeUserExits());
	}

	/* Método update:
	 * Delega en la clase Game la actualización del estado de la partida.
	 */
	public void update() {
		this.client.sendGameAction(ControllerSerializer.serializeUpdate());
	}

	/* Método addObserver:
	 * Delega en la clase Game la acción de añadir un observador del modelo en la partida.
	 */
	public void addObserver(ScrabbleObserver o) {
		this.client.addObserver(o);
	}
	
	/* Método removeObserver:
	 * Delega en la clase Game la acción de eliminar un observador del modelo en la partida.
	 */
	public void removeObserver(ScrabbleObserver o) {
		this.client.removeObserver(o);
	}
}
