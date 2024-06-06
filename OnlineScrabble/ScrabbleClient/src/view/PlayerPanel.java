package view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import containers.Board;
import containers.GamePlayers;
import control.Controller;

/* APUNTES GENERALES:

   La clase PlayerPanel representa la vista de la información de un jugador en la interfaz gráfica.
   Esta clase contiene las fichas del jugador (tapadas si no es su turno), el nombre del mismo, y sus puntos.
   Dependiendo de qué jugador se trate, la orientación de sus fichas será en vertical u horizontal.

   Además, a diferencia de la clase PlayerPanel del modo local Scrabble, en esta clase también se incluye
   el atributo clientNumPlayer, para saber si
*/
public class PlayerPanel extends JPanel implements ScrabbleObserver {

	private static final long serialVersionUID = 1L;
	
	private JPanel tilesPanel;
	
	private JLabel nameLabel;
	
	private JLabel pointsLabel;
	
	private int numJugador;
	private int clientNumPlayer;

	public PlayerPanel(Controller controller, int numJugador, int clientNumPlayer) {
		
		this.clientNumPlayer = clientNumPlayer;
		this.numJugador = numJugador;
		
		initGUI();
		
		controller.addObserver(this);
	}
	
	private void initGUI() {
		
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
		
		this.tilesPanel = new JPanel();
		if(numJugador == 1 || numJugador == 3) {
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
		
		if(this.numJugador == 0 || this.numJugador == 3) {
			add(nameAndPointsPanel);
			add(Box.createRigidArea(new Dimension(50, 50)));			
		}
			
		add(this.tilesPanel);
		
		if(this.numJugador == 1 || this.numJugador == 2) {
			add(Box.createRigidArea(new Dimension(50, 50)));			
			add(nameAndPointsPanel);
		}	
	}
	
	private void createTiles(GamePlayers gamePlayers, int numPlayers) {
		
		this.tilesPanel.removeAll();
		
		if(this.numJugador == this.clientNumPlayer) {
			for(int i = 0; i < gamePlayers.getNumPlayerTiles(this.numJugador); ++i) {
				JButton tileButton = new JButton();
				tileButton.setIcon(new ImageIcon("resources/icons/letters/" + gamePlayers.getPlayerTile(this.numJugador, i).getLetter() + ".png"));
				tileButton.setPreferredSize(new Dimension(45, 45));
				tileButton.setMaximumSize(new Dimension(45, 45));
				tileButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
				this.tilesPanel.add(tileButton);
			}
		}
		else if (this.numJugador < numPlayers){
			for(int i = 0; i < gamePlayers.getNumPlayerTiles(this.numJugador); ++i) {
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
	public void onWordWritten(String word, int posX, int posY, String direction, int points, int extraPoints,
			int numPlayers, GamePlayers gamePlayers, int currentTurn, Board board, boolean gameInitiated) {
		createTiles(gamePlayers, numPlayers);
	}

	@Override
	public void onPassed(int numPlayers, String currentPlayerName, boolean gameInitiated) {}

	@Override
	public void onSwapped(int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		createTiles(gamePlayers, numPlayers);
	}

	@Override
	public void onRegister(Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		if(gameInitiated && this.numJugador < numPlayers) {
			if(this.numJugador == this.clientNumPlayer)
				this.nameLabel.setText("Tú: " + gamePlayers.getPlayerName(this.numJugador));
			else
				this.nameLabel.setText("Jugador: " + gamePlayers.getPlayerName(this.numJugador));
			
			this.pointsLabel.setText("Puntos totales: " + gamePlayers.getPlayerPoints(this.numJugador));
			createTiles(gamePlayers, numPlayers);
		}
		else {
			this.nameLabel.setText("");
			this.pointsLabel.setText("");
			this.tilesPanel.removeAll();
		}
	}

	@Override
	public void onReset(Board board, int numPlayers, String currentPlayerName, int remainingTiles, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		if(gameInitiated && this.numJugador < numPlayers) {
			if(this.numJugador == this.clientNumPlayer)
				this.nameLabel.setText("Tú: " + gamePlayers.getPlayerName(this.numJugador));
			else
				this.nameLabel.setText("Jugador: " + gamePlayers.getPlayerName(this.numJugador));
			
			this.pointsLabel.setText("Puntos totales: " + gamePlayers.getPlayerPoints(this.numJugador));
			createTiles(gamePlayers, numPlayers);
		}
		else {
			this.nameLabel.setText("");
			this.pointsLabel.setText("");
			this.tilesPanel.removeAll();
		}
	}

	@Override
	public void onError(String error, int currentTurn) {}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, int remainingTiles, String currentPlayerName, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		if(gameInitiated && this.numJugador < numPlayers) {
			this.pointsLabel.setText("Puntos totales: " + gamePlayers.getPlayerPoints(this.numJugador));
			createTiles(gamePlayers, numPlayers);
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
	public void onFirstTurnDecided(List<String> lettersObtained, GamePlayers gamePlayers, int numPlayers, int currentTurn, boolean gameInitiated) {}

	@Override
	public void onMovementNeeded(int currentTurn) {}
}
