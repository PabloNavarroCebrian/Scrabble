package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import containers.Board;
import containers.GamePlayers;
import logic.Game;
import scrabble.Controller;

/* APUNTES GENERALES:

   La clase PlayerPanel representa la vista de la información de un jugador en la interfaz gráfica.
   Esta clase contiene las fichas del jugador (tapadas si no es su turno), el nombre del mismo, y sus puntos.
   Dependiendo de qué jugador se trate, la orientación de sus fichas será en vertical u horizontal.
*/

public class PlayerPanel extends JPanel implements ScrabbleObserver {

	private static final long serialVersionUID = 1L;
	
	private JPanel tilesPanel;
	
	private JLabel nameLabel;
	
	private JLabel pointsLabel;
	
	private int numPlayer;

	public PlayerPanel(Controller controller, int numPlayer) {
		
		this.numPlayer = numPlayer;
		
		initGUI();
		
		controller.addObserver(this);
	}
	
	private void initGUI() {
		
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
		
		this.tilesPanel = new JPanel();
		if(numPlayer == 1 || numPlayer == 3) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(Box.createRigidArea(new Dimension(20, 120)));
			this.tilesPanel.setLayout(new BoxLayout(this.tilesPanel, BoxLayout.Y_AXIS));
			this.tilesPanel.setPreferredSize(new Dimension(45, 315));
		}
		else {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(Box.createRigidArea(new Dimension(1, 70)));
			this.tilesPanel.setLayout(new BoxLayout(this.tilesPanel, BoxLayout.X_AXIS));
			this.tilesPanel.setPreferredSize(new Dimension(315, 45));
		}
		
		JPanel nameAndPointsPanel = new JPanel();
		nameAndPointsPanel.setLayout(new BoxLayout(nameAndPointsPanel, BoxLayout.Y_AXIS));
		
		this.nameLabel = new JLabel();
		this.pointsLabel = new JLabel();
		
		nameAndPointsPanel.add(this.nameLabel);
		nameAndPointsPanel.add(this.pointsLabel);
		
		if(this.numPlayer == 0 || this.numPlayer == 3) {
			add(nameAndPointsPanel);
			add(Box.createRigidArea(new Dimension(50, 50)));			
		}
			
		add(this.tilesPanel);
		
		if(this.numPlayer == 1 || this.numPlayer == 2) {
			add(Box.createRigidArea(new Dimension(50, 50)));			
			add(nameAndPointsPanel);
		}	
	}
	
	private void createTiles(int currentTurn, GamePlayers gamePlayers) {
		
		this.tilesPanel.removeAll();
		
		if(currentTurn == this.numPlayer) {
			for(int i = 0; i < gamePlayers.getNumPlayerTiles(this.numPlayer); ++i) {
				JButton tileButton = new JButton();
				tileButton.setIcon(new ImageIcon("resources/icons/letters/" + gamePlayers.getPlayerTile(this.numPlayer, i).getLetter() + ".png"));
				tileButton.setPreferredSize(new Dimension(45, 45));
				tileButton.setMaximumSize(new Dimension(45, 45));
				tileButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
				this.tilesPanel.add(tileButton);
			}
		}
		else {
			for(int i = 0; i < gamePlayers.getNumPlayerTiles(this.numPlayer); ++i) {
				JButton tileButton = new JButton();
				tileButton.setIcon(new ImageIcon("resources/icons/letters/reversed_tile.png"));
				tileButton.setPreferredSize(new Dimension(45, 45));
				tileButton.setMaximumSize(new Dimension(45, 45));
				tileButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
				this.tilesPanel.add(tileButton);
			}
		}
	}
	
	@Override
	public void onWordWritten(String currentPlayerName, Board board, String word, int posX, int posY, String direction,
			int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn) {
		if(currentTurn == this.numPlayer) 
			createTiles(currentTurn, gamePlayers);
	}

	@Override
	public void onPassed(int numPlayers, Board board, String currentPlayerName) {}

	@Override
	public void onSwapped(String currentPlayerName, Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn) {
		if(currentTurn == this.numPlayer) createTiles(currentTurn, gamePlayers);
	}

	@Override
	public void onRegister(Board board, int numPlayers, boolean gameFinished, GamePlayers gamePlayers, int currentTurn) {
		if(Game.getGameInitiated() && this.numPlayer < numPlayers) {
			this.nameLabel.setText("Jugador: " + gamePlayers.getPlayerName(this.numPlayer));
			this.pointsLabel.setText("Puntos totales: " + gamePlayers.getPlayerPoints(this.numPlayer));
			createTiles(currentTurn, gamePlayers);
		}
		else {
			this.nameLabel.setText("");
			this.pointsLabel.setText("");
			this.tilesPanel.removeAll();
		}
	}

	@Override
	public void onReset(Board board, int numPlayers, String currentTurnName, int remainingTiles, GamePlayers gamePlayers, int currentTurn) {
		if(Game.getGameInitiated() && this.numPlayer < numPlayers) {
			this.nameLabel.setText("Jugador: " + gamePlayers.getPlayerName(this.numPlayer));
			this.pointsLabel.setText("Puntos totales: " + gamePlayers.getPlayerPoints(this.numPlayer));
			createTiles(currentTurn, gamePlayers);
		}
		else {
			this.nameLabel.setText("");
			this.pointsLabel.setText("");
			this.tilesPanel.removeAll();
		}
	}

	@Override
	public void onError(String error) {}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, String status, int remainingTiles, String currentTurnName, GamePlayers gamePlayers, int currentTurn) {
		if(Game.getGameInitiated() && this.numPlayer < numPlayers) {
			this.nameLabel.setText("Jugador: " + gamePlayers.getPlayerName(this.numPlayer));
			this.pointsLabel.setText("Puntos totales: " + gamePlayers.getPlayerPoints(this.numPlayer));
			createTiles(currentTurn, gamePlayers);
		}
		else {
			this.nameLabel.setText("");
			this.pointsLabel.setText("");
			this.tilesPanel.removeAll();
		}
	}

	@Override
	public void onEnd(String message) {}

	@Override
	public void onFirstTurnDecided(String[] lettersObtained, GamePlayers gamePlayers, Board board, int numPlayers, int currentTurn) {}

	@Override
	public void onMovementNeeded() {}
}
