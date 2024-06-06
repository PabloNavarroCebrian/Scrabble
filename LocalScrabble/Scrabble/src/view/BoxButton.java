package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import containers.Board;
import containers.GamePlayers;
import scrabble.Controller;
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
	private Box box;
	private ChooseWordDialog chooseWordDialog;
	
	private Controller controller;
	
	private boolean enableButton;

	BoxButton(Controller controller, int x, int y, ChooseWordDialog chooseWordDialog) {
		
		this.posX = x;
		this.posY = y;
		this.box = null;
		this.chooseWordDialog = chooseWordDialog;
		this.controller = controller;
		
		initGUI();
		this.controller.addObserver(this);
	}
	
	private void initGUI() {
		
		enableButton = false;
		
		setToolTipText(String.format("Casilla (%s, %s)", this.posX, this.posY));
		setIcon(new ImageIcon("resources/icons/letters/box_default_icon.png"));
		setPreferredSize(new Dimension(49, 49));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.box = new Box(null, null, false);
		
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(box != null && enableButton) {
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
	public void onWordWritten(String currentPlayerName, Board board, String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn) {
		setImage();
		enableButton = false;
	}

	@Override
	public void onRegister(Board board, int numPlayers, boolean gameFinished, GamePlayers gamePlayers, int currentTurn) {
		this.box = board.getBoxAt(this.posX, this.posY);
		if(box != null) setImage();
		enableButton = false;
	}

	@Override
	public void onReset(Board board, int numPlayers, String currentTurnName, int remainingTiles, GamePlayers gamePlayers, int currentTurn) {
		this.box = board.getBoxAt(this.posX, this.posY);
		setImage();
		enableButton = false;
	}
	
	private void setImage() {
		
		if(box.getTile() != null) {
			this.setIcon(new ImageIcon("resources/icons/letters/" + box.getTile().getLetter().toUpperCase() + ".png"));
		}
		else if(box.getSpecialEffect() != null)
			this.setIcon(new ImageIcon("resources/icons/special_effects/" + box.getSpecialEffect() + ".png"));
	}

	@Override
	public void onPassed(int numPlayers, Board board, String currentPlayerName) {
		enableButton = false;
	}

	@Override
	public void onSwapped(String currentPlayerName, Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn) {
		enableButton = false;
	}

	@Override
	public void onError(String error) {}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, String status, int remainingTiles, String currentTurnName, GamePlayers gamePlayers, int currentTurn) {
		enableButton = false;
	}

	@Override
	public void onEnd(String message) {}

	@Override
	public void onFirstTurnDecided(String[] lettersObtained, GamePlayers gamePlayers, Board board, int numPlayers, int currentTurn) {}

	@Override
	public void onMovementNeeded() {
		enableButton = true;
	}
}
