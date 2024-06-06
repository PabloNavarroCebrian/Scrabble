package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import containers.Board;
import containers.GamePlayers;
import control.Controller;

/* APUNTES GENERALES:

   Esta clase es la contenedora de todos los botones presentes en el juego (excepto los del tablero).

   La parte visual que representa se sitúa en el norte de la pantalla.
   Los botones que aloja son: paso de turno, cambio de ficha, paso de turno, ayuda y salir de la partida.

   Nótese que a diferencia de la versión local, la versión en línea tiene un atributo clientNumPlayer,
   para saber cuando se han de permitir el uso de botones.
*/

public class ControlPanel extends JPanel implements ScrabbleObserver {

	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	private int clientNumPlayer;
	
	private JToolBar bar;
	
	private List<JButton> buttonsToBlockGameNotInitiated;
	
	private List<JButton> buttonsToBlockCPUTurn;
	
	private JButton continueButton;
	
	private JFileChooser fc;
	
	
	ControlPanel(Controller controller, Component parent, int clientNumPlayer) {
		
		this.controller = controller;
		this.clientNumPlayer = clientNumPlayer;
		
		this.buttonsToBlockCPUTurn = new ArrayList<JButton>();
		
		this.buttonsToBlockGameNotInitiated = new ArrayList<JButton>();
	
		this.fc = new JFileChooser();
		
		this.fc.setCurrentDirectory(new File("resources/existingGames"));
		
		initGUI();
		
		this.controller.addObserver(this);
	}
	
	private void initGUI() {
		
		this.bar = new JToolBar();
		this.add(bar);
		this.bar.setPreferredSize(new Dimension(1100, 50));
		
		
		JButton passButton = new JButton();
		passButton.setToolTipText("Pasar de turno");
		passButton.setIcon(new ImageIcon("resources/icons/control_panel/pass.png"));
		passButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.passTurn();
			}
		});
		
		bar.add(passButton);
		bar.add(Box.createRigidArea(new Dimension(5, 1)));
		this.buttonsToBlockCPUTurn.add(passButton);
		this.buttonsToBlockGameNotInitiated.add(passButton);
		
		JButton swapButton = new JButton();
		swapButton.setToolTipText("Intercambiar una ficha por otra aleatoriamente");
		swapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.swapTile();
			}
		});
		swapButton.setIcon(new ImageIcon("resources/icons/control_panel/swap.png"));
		bar.add(swapButton);
		this.buttonsToBlockCPUTurn.add(swapButton);
		this.buttonsToBlockGameNotInitiated.add(swapButton);
		
		bar.add(Box.createRigidArea(new Dimension(5, 1)));
		
		continueButton = new JButton();
		continueButton.setToolTipText("Continuar el juego");
		continueButton.setIcon(new ImageIcon("resources/icons/control_panel/continue.png"));
		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				continueButton.setEnabled(false);
				controller.update();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						controller.playTurn();
					}
				});
			}
		});
		bar.add(continueButton);
		this.buttonsToBlockGameNotInitiated.add(continueButton);
		
		bar.add(Box.createGlue());
		
		JButton helpButton = new JButton();
		helpButton.setToolTipText("Información");
		helpButton.setIcon(new ImageIcon("resources/icons/control_panel/info.png"));
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String help = String.format("Posibles acciones:%n%n"
						+ "• Colocar palabra: hacer click en la casilla "
						+ "donde se quiere que empiece y escribir la palabra y su dirección.%n"
						+ "• Pasar de turno: pulsar el botón situado en primera posición desde la izquierda.%n"
						+ "• Intercambiar una ficha: pulsar el botón situado en segunda posición desde la izquierda.%n"
						+ "• Continuar ejecución: pulsar el botón situado en tercera posición desde la izquierda%n"
						+ "• Salir del juego: pulsar el botón situado en la primera posición desde la derecha.%n%n"
						+ "Proyecto Scrabble ©");
				
				JOptionPane.showMessageDialog(ControlPanel.this, help, "AYUDA", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		bar.add(helpButton);
		bar.addSeparator();
		
		JButton exitButton = new JButton();
		exitButton.setToolTipText("Salir del juego");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] options = {"No", "Sí"};
				int n = JOptionPane.showOptionDialog(ControlPanel.this,
						"¿Estás seguro de que quieres salir?", "Salir",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, null);
				
				if(n == 1) 
					controller.userExits();
			}
		});
		exitButton.setIcon(new ImageIcon("resources/icons/control_panel/exit.png"));
		bar.add(exitButton);	
	}
	
	@Override
	public void onWordWritten(String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn, Board board, boolean gameInitiated) {
		resetEnabledButtons(numPlayers, gameInitiated);
	}

	@Override
	public void onPassed(int numPlayers, String currentPlayerName, boolean gameInitiated) {
		resetEnabledButtons(numPlayers, gameInitiated);
	}

	@Override
	public void onSwapped(int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		resetEnabledButtons(numPlayers, gameInitiated);
	}

	@Override
	public void onRegister(Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		resetEnabledButtons(numPlayers, gameInitiated);
	} 

	@Override
	public void onReset(Board board, int numPlayers, String currentPlayerName, int remainingTiles, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		resetEnabledButtons(numPlayers, gameInitiated);
	}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, int remainingTiles, String currentPlayerName, GamePlayers gamePlayers, int currentTurn, boolean gameInitiated) {
		resetEnabledButtons(numPlayers, gameInitiated);
	}

	@Override
	public void onError(String error, int currentTurn) {}

	@Override
	public void onEnd(String message) {}

	@Override
	public void onFirstTurnDecided(List<String> lettersObtained, GamePlayers gamePlayers, int numPlayers, int currentTurn, boolean gameInitiated) {
		resetEnabledButtons(numPlayers, gameInitiated);
	}
	
	@Override
	public void onMovementNeeded(int currentTurn) {
		enableButtons(this.buttonsToBlockCPUTurn, currentTurn == clientNumPlayer);
		this.continueButton.setEnabled(false);
	}
	
	private void enableButtons(List<JButton> buttons, boolean enable) {
		for(JButton b : buttons)
			b.setEnabled(enable);
	}
	
	private void resetEnabledButtons(int numPlayers, boolean gameInitiated) {
		
		enableButtons(this.buttonsToBlockGameNotInitiated, gameInitiated);
		
		enableButtons(this.buttonsToBlockCPUTurn, false);
		
		this.continueButton.setEnabled(gameInitiated && numPlayers != 0);	
	}
}
