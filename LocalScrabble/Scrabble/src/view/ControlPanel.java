package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
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
import logic.Game;
import scrabble.Controller;

/* APUNTES GENERALES:
   
   Esta clase es la contenedora de todos los botones presentes en el juego (excepto los del tablero).
   
   La parte visual que representa se sitúa en el norte de la pantalla.
   Los botones que aloja son: nueva partida, carga y guardado de partida, reseteo de partida,
   cambio de ficha, añadido o modificación de jugadores, paso de turno, continuar partidas,
   ayuda y salir de la partida.
 */
public class ControlPanel extends JPanel implements ScrabbleObserver {

	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	
	private JToolBar bar;
	
	private List<JButton> buttonsToBlockGameNotInitiated;
	
	private List<JButton> buttonsToBlockCPUTurn;
	
	private JButton continueButton;
	
	private AddPlayersDialog addPlayersDialog;
	
	private JFileChooser fc;
	
	
	ControlPanel(Controller controller, Component parent) {
		
		this.controller = controller;
		
		this.buttonsToBlockCPUTurn = new ArrayList<JButton>();
		
		this.buttonsToBlockGameNotInitiated = new ArrayList<JButton>();
		
		this.addPlayersDialog = new AddPlayersDialog(parent);
		
		this.fc = new JFileChooser();
		
		this.fc.setCurrentDirectory(new File("resources/existingGames"));
		
		initGUI();
		
		this.controller.addObserver(this);
	}
	
	private void initGUI() {
		
		this.bar = new JToolBar();
		this.add(bar);
		this.bar.setPreferredSize(new Dimension(1100, 50));
		
		JButton newGameButton = new JButton();
		newGameButton.setToolTipText("Iniciar una partida nueva");
		newGameButton.setIcon(new ImageIcon("resources/icons/control_panel/new_game.png"));
		newGameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.newGame();
				}
				catch (FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(ControlPanel.this, "El fichero de nueva partida no es válido", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		bar.add(newGameButton);
		bar.add(Box.createRigidArea(new Dimension(5, 1)));
		
		JButton loadButton = new JButton();
		loadButton.setToolTipText("Cargar una partida de fichero");
		loadButton.setIcon(new ImageIcon("resources/icons/control_panel/open.png"));
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int ret = fc.showOpenDialog(ControlPanel.this);
				if (ret == JFileChooser.APPROVE_OPTION) {
					try {
						controller.loadGame(fc.getSelectedFile().getAbsolutePath());
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(ControlPanel.this, "El fichero seleccionado no es válido", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		bar.add(loadButton);
		bar.add(Box.createRigidArea(new Dimension(5, 1)));
		
		JButton saveButton = new JButton();
		saveButton.setToolTipText("Guardar la partida actual en un fichero");
		saveButton.setIcon(new ImageIcon("resources/icons/control_panel/save.png"));
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int ret = fc.showSaveDialog(ControlPanel.this);
				if (ret == JFileChooser.APPROVE_OPTION) {
					try {
						controller.saveGame(fc.getSelectedFile().getName());
						JOptionPane.showMessageDialog(ControlPanel.this, "La partida ha sido guardada con éxito", "GUARDAR", JOptionPane.INFORMATION_MESSAGE);
					} catch (FileNotFoundException | IllegalArgumentException exc) {
						JOptionPane.showMessageDialog(ControlPanel.this, exc.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		bar.add(saveButton);
		this.buttonsToBlockGameNotInitiated.add(saveButton);
		
		bar.addSeparator();
		
		JButton resetButton = new JButton();
		resetButton.setToolTipText("Resetear el juego");
		resetButton.setIcon(new ImageIcon("resources/icons/control_panel/reset.png"));
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String seedString = JOptionPane.showInputDialog(ControlPanel.this, "Introduce una semilla (actual: " + Game.getSeed() + ")", "Reset", JOptionPane.PLAIN_MESSAGE);
					
					if(seedString != null) {
						try {
							Game.setSeed(Integer.parseInt(seedString));
							controller.reset();
						}
						
						catch(NumberFormatException nfe) {
							JOptionPane.showMessageDialog(ControlPanel.this, "La semilla debe ser un número", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				catch (FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(ControlPanel.this, "El fichero de reseteo no es válido", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		bar.add(resetButton);
		this.buttonsToBlockGameNotInitiated.add(resetButton);
		
		bar.add(Box.createRigidArea(new Dimension(5, 1)));
		
		JButton addPlayersButton = new JButton();
		addPlayersButton.setToolTipText("Añadir o cambiar jugadores a la partida");
		addPlayersButton.setIcon(new ImageIcon("resources/icons/control_panel/player.png"));
		addPlayersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int status = addPlayersDialog.open();
				if(status == 1) {
					controller.addOrChangePlayers(addPlayersDialog.createPlayers());
					controller.update();
				}
			}
		});
		bar.add(addPlayersButton);
		bar.add(Box.createRigidArea(new Dimension(5, 1)));
		this.buttonsToBlockGameNotInitiated.add(addPlayersButton);
		
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
						+ "• Nueva partida: pulsar el botón situado en primera posición desde la izquierda.%n"
						+ "• Cargar partida: pulsar el botón situado en segunda posición desde la izquierda.%n"
						+ "• Guardar partida: pulsar el botón situado en tercera posición desde la izquierda.%n"
						+ "• Resetar partida: pulsar el botón situado en cuarta posición desde la izquierda.%n"
						+ "• Añadir o cambiar jugadores: pulsar el botón situado en quinta posición desde la izquierda.%n"
						+ "• Pasar de turno: pulsar el botón situado en sexta posición desde la izquierda.%n"
						+ "• Intercambiar una ficha: pulsar el botón situado en séptima posición desde la izquierda.%n"
						+ "• Continuar ejecución: pulsar el botón situado en octava posición desde la izquierda%n"
						+ "• Salir del juego: pulsar el botón situado en la primera posición desde la derecha.%n%n"
						+ "Proyecto Scrabble ©");
				
				
				JOptionPane.showMessageDialog(ControlPanel.this, help, "Información", JOptionPane.INFORMATION_MESSAGE);
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
					System.exit(0);
			}
		});
		exitButton.setIcon(new ImageIcon("resources/icons/control_panel/exit.png"));
		bar.add(exitButton);	
	}
	
	@Override
	public void onWordWritten(String currentPlayerName, Board board, String word, int posX, int posY, String direction, int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn) {
		resetEnabledButtons(numPlayers);
	}

	@Override
	public void onPassed(int numPlayers, Board board, String currentPlayerName) {
		resetEnabledButtons(numPlayers);
	}

	@Override
	public void onSwapped(String currentPlayerName, Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn) {
		resetEnabledButtons(numPlayers);
	}

	@Override
	public void onRegister(Board board, int numPlayers, boolean gameFinished, GamePlayers gamePlayers, int currentTurn) {
		resetEnabledButtons(numPlayers);
	} 

	@Override
	public void onReset(Board board, int numPlayers, String currentTurnName, int remainingTiles, GamePlayers gamePlayers, int currentTurn) {
		resetEnabledButtons(numPlayers);
	}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, String status, int remainingTiles, String currentTurnName, GamePlayers gamePlayers, int currentTurn) {
		resetEnabledButtons(numPlayers);
	}

	@Override
	public void onError(String error) {}

	@Override
	public void onEnd(String message) {}

	@Override
	public void onFirstTurnDecided(String[] lettersObtained, GamePlayers gamePlayers, Board board, int numPlayers, int currentTurn) {
		resetEnabledButtons(numPlayers);
	}
	
	@Override
	public void onMovementNeeded() {
		enableButtons(this.buttonsToBlockCPUTurn, true);
		this.continueButton.setEnabled(false);
	}
	
	private void enableButtons(List<JButton> buttons, boolean enable) {
		for(JButton b : buttons)
			b.setEnabled(enable);
	}
	
	private void resetEnabledButtons(int numPlayers) {
		
		enableButtons(this.buttonsToBlockGameNotInitiated, Game.getGameInitiated());
		
		enableButtons(this.buttonsToBlockCPUTurn, false);
		
		this.continueButton.setEnabled(Game.getGameInitiated() && numPlayers != 0);
			
	}
}
