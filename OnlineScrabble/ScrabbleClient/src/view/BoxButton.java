package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import containers.Board;
import containers.GamePlayers;
import control.Controller;
import simulatedObjects.Box;

/* APUNTES GENERALES:

   La clase BoxButton es la que representa cada casilla del tablero. Se trata de un botón
   porque de esta manera, es suficiente con que el usuario presione sobre la casilla en la
   que se quiere colocar una palabra. 

   Dependiendo del tipo de casilla, atendiendo a su letra y a su tipo especial (si es que tiene), 
   el botón tendrá una imagen u otra (véanse los iconos del directorio resources/icons).
*/

public class BoxButton extends JButton implements ScrabbleObserver {
	
	private static final long serialVersionUID = 1L;
	
	private int posX;
	private int posY;
	private ChooseWordDialog chooseWordDialog;
	
	private Controller controller;
	private int clientNumPlayer;
	
	private boolean enableButton;

	BoxButton(Controller controller, int x, int y, ChooseWordDialog chooseWordDialog, int clientNumPlayer) {
		
		this.posX = x;
		this.posY = y;
		this.chooseWordDialog = chooseWordDialog;
		this.controller = controller;
		this.clientNumPlayer = clientNumPlayer;
		
		initGUI();
		this.controller.addObserver(this);
	}
	
	private void initGUI() {
		
		enableButton = false;
		
		setToolTipText(String.format("Casilla (%s, %s)", this.posX, this.posY));
		setIcon(new ImageIcon("resources/icons/letters/box_default_icon.png"));
		setPreferredSize(new Dimension(49, 49));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(enableButton) {
					int status = chooseWordDialog.open(posX, posY);
					if(status == 1) {
						String word = chooseWordDialog.getSelectedWord();
						String direction = chooseWordDialog.getSelectedDirection();
						controller.writeAWord(word, posX, posY, direction);
					}
				}
			}
		});	
	}

	@Override
	public void onWordWritten(String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn, Board board, boolean gameInitiated) {
		setImage(board.getBoxAt(this.posX, this.posY));
		enableButton = false;
	}

	@Override
	public void onRegister(Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		setImage(board.getBoxAt(this.posX, this.posY));
		enableButton = false;
	}

	@Override
	public void onReset(Board board, int numPlayers, String currentPlayerName, int remainingTiles, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		setImage(board.getBoxAt(this.posX, this.posY));
		enableButton = false;
	}
	
	private void setImage(Box box) {
		
		if(box.getTile() != null) {
			this.setIcon(new ImageIcon("resources/icons/letters/" + box.getTile().getLetter().toUpperCase() + ".png"));
		}
		else if(box.getSpecialEffect() != null)
			this.setIcon(new ImageIcon("resources/icons/special_effects/" + box.getSpecialEffect() + ".png"));
	}

	@Override
	public void onPassed(int numPlayers, String currentPlayerName, boolean gameInitiated) {
		enableButton = false;
	}

	@Override
	public void onSwapped(int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		enableButton = false;
	}

	@Override
	public void onError(String error, int currentTurn) {}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, int remainingTiles, String currentPlayerName, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		enableButton = false;
	}

	@Override
	public void onEnd(String message) {}

	@Override
	public void onFirstTurnDecided(List<String> lettersObtained, GamePlayers gamePlayers, int numPlayers, int currentTurn, boolean gameInitiated) {}

	@Override
	public void onMovementNeeded(int currentTurn) {
		if(currentTurn == clientNumPlayer)
			enableButton = true;
	}
}
