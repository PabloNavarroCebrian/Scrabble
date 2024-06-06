package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import containers.Board;
import containers.GamePlayers;
import control.Controller;
/* APUNTES GENERALES:

   La clase BoardPanel es la clase que representa el tablero de juego.
   A diferencia de la clase BoardPanel del proyecto Scrabble en modo local,
   se tiene un array de los botones que conforman el tablero, para que, cuando no se trate
   del turno de un jugador, no pueda interaccionar con el tablero. 
*/

public class BoardPanel extends JPanel implements ScrabbleObserver {
	
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	private int clientNumPlayer;
	
	private ChooseWordDialog chooseWordDialog;
	private List<BoxButton> addedBoxButtons;

	BoardPanel(Controller controller, int clientNumPlayer) {
		
		this.controller = controller;
		this.clientNumPlayer = clientNumPlayer;
		this.addedBoxButtons = new ArrayList<BoxButton>();
		
		this.chooseWordDialog = new ChooseWordDialog(this);
		
		this.setPreferredSize(new Dimension(730, 730));
		
		setVisible(true);
		
		this.controller.addObserver(this);
	}

	@Override
	public void onWordWritten(String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn, Board board, boolean gameInitiated) {}

	@Override
	public void onRegister(Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {

		this.removeAll();
		for(BoxButton bb : this.addedBoxButtons)
			this.controller.removeObserver(bb);
		
		this.setLayout(new GridLayout(board.getBoardSize(), board.getBoardSize()));
		for(int i = 0; i < board.getBoardSize(); ++i)
			for(int j = 0; j < board.getBoardSize(); ++j) {
				BoxButton bb = new BoxButton(this.controller, i, j, this.chooseWordDialog, clientNumPlayer);
				this.add(bb);
				this.addedBoxButtons.add(bb);
				
			}
		setPreferredSize(new Dimension(730, 730));
	}

	@Override
	public void onReset(Board board, int numPlayers, String currentPlayerName, int remainingTiles, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		
		this.removeAll();
		for(BoxButton bb : this.addedBoxButtons)
			this.controller.removeObserver(bb);
		
		this.setLayout(new GridLayout(board.getBoardSize(), board.getBoardSize()));
		for(int i = 0; i < board.getBoardSize(); ++i)
			for(int j = 0; j < board.getBoardSize(); ++j) {
				BoxButton bb = new BoxButton(this.controller, i, j, this.chooseWordDialog, clientNumPlayer);
				this.add(bb);
				this.addedBoxButtons.add(bb);
			}
		setPreferredSize(new Dimension(730, 730));
	}

	@Override
	public void onPassed(int numPlayers, String currentPlayerName, boolean gameInitiated) {}

	@Override
	public void onSwapped(int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {}

	@Override
	public void onError(String error, int currentTurn) {}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, int remainingTiles, String currentPlayerName, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {}

	@Override
	public void onEnd(String message) {}

	@Override
	public void onFirstTurnDecided(List<String> lettersObtained, GamePlayers gamePlayers, int numPlayers, int currentTurn, boolean gameInitiated) {}

	@Override
	public void onMovementNeeded(int currentTurn) {}
}
