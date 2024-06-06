package view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import containers.Board;
import containers.GamePlayers;
import scrabble.Controller;

/* APUNTES GENERALES:

   La clase BoardPanel es la clase que representa el tablero de juego.
   Como puede observarse, su único método sobrescrito de la interfaz ScrabbleObserver
   es onReset, en el que inicializa todos los botones que lo conforman.
*/
public class BoardPanel extends JPanel implements ScrabbleObserver {
	
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	
	private ChooseWordDialog chooseWordDialog;

	BoardPanel(Controller controller) {
		
		this.controller = controller;
		
		this.chooseWordDialog = new ChooseWordDialog(this);
		
		this.setPreferredSize(new Dimension(730, 730));
		
		setVisible(true);
		
		this.controller.addObserver(this);
	}

	@Override
	public void onWordWritten(String currentPlayerName, Board board, String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn) {}

	@Override
	public void onRegister(Board board, int numPlayers, boolean gameFinished, GamePlayers gamePlayers, int currentTurn) {}

	@Override
	public void onReset(Board board, int numPlayers, String currentTurnName, int remainingTiles, GamePlayers gamePlayers, int currentTurn) {
		
		this.removeAll();
		
		this.setLayout(new GridLayout(board.getBoardSize(), board.getBoardSize()));
		for(int i = 0; i < board.getBoardSize(); ++i)
			for(int j = 0; j < board.getBoardSize(); ++j) {
				this.add(new BoxButton(this.controller, i, j, this.chooseWordDialog));
			}
		setPreferredSize(new Dimension(730, 730));
	}

	@Override
	public void onPassed(int numPlayers, Board board, String currentPlayerName) {}

	@Override
	public void onSwapped(String currentPlayerName, Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn) {}

	@Override
	public void onError(String error) {}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, String status, int remainingTiles, String currentTurnName, GamePlayers gamePlayers, int currentTurn) {}

	@Override
	public void onEnd(String message) {}

	@Override
	public void onFirstTurnDecided(String[] lettersObtained, GamePlayers gamePlayers, Board board, int numPlayers, int currentTurn) {}

	@Override
	public void onMovementNeeded() {}
}
