package view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import containers.Board;
import containers.GamePlayers;
import control.Controller;

/* APUNTES GENERALES:

   La clase InfoPanel se trata de la zona situada sobre el tablero, y cuya función es informar al usuario a lo largo
   de una partida de los distintos eventos que ocurren. Por ejemplo, de quién es el turno actual, el comienzo de una
   partida o la acción tomada por un determinado jugador.
*/

public class InfoPanel extends JPanel implements ScrabbleObserver {

	private static final long serialVersionUID = 1L;
	
	private String currentTurnName;
	private int clientNumPlayer;
	
	private JLabel currentTurnLabel;
	private JLabel remainingTilesLabel;
	private JLabel infoLabel;
	private JLabel pointsLabel;
	
	private Component parent;

	InfoPanel(Controller controller, Component parent, int clientNumPlayer) {
		
		this.clientNumPlayer = clientNumPlayer;
		this.parent = parent;
		initGUI();
		controller.addObserver(this);
	}
	
	private void initGUI() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(Box.createRigidArea(new Dimension(10, 50)));
		
		JPanel turnAndTilesPanel = new JPanel();
		add(turnAndTilesPanel);
		turnAndTilesPanel.setLayout(new BoxLayout(turnAndTilesPanel, BoxLayout.Y_AXIS));

		currentTurnLabel = new JLabel("");
		turnAndTilesPanel.add(currentTurnLabel);
		
		turnAndTilesPanel.add(Box.createRigidArea(new Dimension(1, 5)));
		
		remainingTilesLabel = new JLabel("");
		turnAndTilesPanel.add(remainingTilesLabel);
		
		add(Box.createRigidArea(new Dimension(30, 30)));
		
		JPanel infoPanel = new JPanel();
		add(infoPanel);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		infoLabel = new JLabel("¡Bienvenido al Scrabble!");
		infoPanel.add(infoLabel);
		
		infoPanel.add(Box.createRigidArea(new Dimension(1, 5)));
		
		pointsLabel = new JLabel();
		infoPanel.add(pointsLabel);
	}
	
	@Override
	public void onWordWritten(String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn, Board board, boolean gameInitiated) {
		infoLabel.setText(String.format("%s escribe la palabra \"%s\" en la posición (%s, %s) y dirección \"%s\"", this.currentTurnName, word.toUpperCase(), posX, posY, direction.toUpperCase()));
		
		String pointsString = String.format("¡Gana %s puntos!", points);
		if(extraPoints != 0)
			pointsString += String.format(" Además, ¡gana %s puntos extra!", extraPoints);
		pointsLabel.setText(pointsString);
	}

	@Override
	public void onPassed(int numPlayers, String currentPlayerName, boolean gameInitiated) {
		infoLabel.setText(String.format("%s pasa de turno", this.currentTurnName));
	}

	@Override
	public void onSwapped(int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		infoLabel.setText(String.format("%s intercambia una ficha", this.currentTurnName));
	}

	@Override
	public void onRegister(Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {}

	@Override
	public void onReset(Board board, int numPlayers, String currentPlayerName, int remainingTiles, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		if (gameInitiated && numPlayers != 0) {
			infoLabel.setText("Partida iniciada");
			if(currentTurn < 0)
				this.currentTurnName = "";
			else
				this.currentTurnName = gamePlayers.getPlayerName(currentTurn);
			
			currentTurnLabel.setText("Turno de: " + this.currentTurnName);
			remainingTilesLabel.setText("Fichas restantes: " + remainingTiles);
			pointsLabel.setText("");
		}
		
		else if(gameInitiated) {
			currentTurnLabel.setText("");
			remainingTilesLabel.setText("Fichas restantes: " + remainingTiles);
			infoLabel.setText("Nueva partida iniciada, pero... ¡hay que añadir jugadores!");
		}
	}

	@Override
	public void onError(String error, int currentTurn) {
		if(currentTurn == this.clientNumPlayer)
			JOptionPane.showMessageDialog(parent, error, "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, int remainingTiles, String currentPlayerName, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		
		if(!gameFinished) {
			this.currentTurnName = gamePlayers.getPlayerName(currentTurn);
			this.currentTurnLabel.setText("Turno de: " + this.currentTurnName);
			this.remainingTilesLabel.setText("Fichas restantes: " + remainingTiles);
			
			this.infoLabel.setText("Eligiendo movimiento...");
		}
		
		else {
			this.infoLabel.setText("¡El juego ha finalizado!");
			this.currentTurnLabel.setText("");
			this.remainingTilesLabel.setText("");
		}
		
		this.pointsLabel.setText("");
	}

	@Override
	public void onEnd(String message) {
		JOptionPane.showMessageDialog(this, message, "Scrabble", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	@Override
	public void onFirstTurnDecided(List<String> lettersObtained, GamePlayers gamePlayers, int numPlayers, int currentTurn, boolean gameInitiated) {
		
		StringBuilder buffer = new StringBuilder();
		
		for(int i = 0; i < numPlayers; i++) {
			buffer.append(gamePlayers.getPlayerName(i)).append(" ha cogido una ")
				  .append(lettersObtained.get(i)).append(System.lineSeparator());
		}
		
		buffer.append(System.lineSeparator()).append("El orden de juego es: ");
		
		for(int i = 0; i < numPlayers; i++) {
			buffer.append(gamePlayers.getPlayerName((i + currentTurn) % numPlayers));
			if(i != numPlayers - 1) 
				buffer.append(" -> ");
		}
			
		
		buffer.append(System.lineSeparator());
		
		JOptionPane.showMessageDialog(parent, buffer.toString(), "Elección de Turnos", JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void onMovementNeeded(int currentTurn) {
		if(currentTurn == clientNumPlayer)
			this.infoLabel.setText("Elige tu siguiente movimiento");
	}
}
