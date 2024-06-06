package view;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import command.Command;
import containers.Board;
import containers.GamePlayers;
import exceptions.CommandExecuteException;
import exceptions.CommandParseException;
import logic.Game;
import scrabble.Controller;
import simulatedObjects.Box;
import simulatedObjects.SpecialEffects;
import utils.StringUtils;

/* APUNTES GENERALES:

   La clase ConsoleView es la empleada para mostrar el transcurso de la partida,
   incluyendo el tablero (con las posibles palabras que pueda contener), el estado
   del jugador cuyo turno es el actual (nombre, puntos totales y fichas), así como
   la semilla con la que se está jugando la partida. Además también muestra la solicitud
   de información: elecciones de usuario (como número de jugadores, sus nombres, etc) y comandos.
	
   Cabe destacar que el método showBoard() ha sido generalizado, de manera que para cualquier
   tamaño, se producirá un tablero regular y ordenado.
*/

public class ConsoleView implements ScrabbleObserver {
	
	// Para no estar creando un new Scanner(System.in) cada vez que compruebo.
	public static boolean isInputFromConsole;
	
	private static final String PROMPT = "Comando ([h]elp) > ";
	
	private static final String CENTRE_SYMBOL = "*";
	private static final String DOUBLE_LETTER_SYMBOL = "•";
	private static final String DOUBLE_WORD_SYMBOL = "░";
	private static final String TRIPLE_LETTER_SYMBOL = "▒";
	private static final String TRIPLE_WORD_SYMBOL = "█";
	private static final String ROW_SEPARATOR_SYMBOL = "-";
	private static final String COLUMN_SEPARATOR_SYMBOL = "|";
	
	private Controller controller;
	
	private Scanner in;
	
	private PrintStream out;
	private String outFile;
	
	public ConsoleView(Controller controller, InputStream in, OutputStream out, String outputFile) {
		this.controller = controller;
		
		isInputFromConsole = (System.in == in);

		this.in = new Scanner(in);
		this.out = new PrintStream(out);
		this.outFile = outputFile;

		this.controller.addObserver(this);
	}

	private void showFirstTurn(String[] lettersObtained, GamePlayers players, int turn) {
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(StringUtils.LINE_SEPARATOR)
			  .append("Elección de turnos:")
			  .append(StringUtils.LINE_SEPARATOR);
		
		for(int i = 0; i < players.getNumPlayers(); i++) {
			buffer.append(players.getPlayerName(i)).append(" ha cogido una ")
				  .append(lettersObtained[i]).append(StringUtils.LINE_SEPARATOR);
		}
		
		buffer.append(StringUtils.LINE_SEPARATOR).append("El orden de juego es: ");
		
		for(int i = 0; i < players.getNumPlayers(); i++) {
			buffer.append(players.getPlayerName((i + turn) % players.getNumPlayers()));
			if(i != players.getNumPlayers() - 1) buffer.append(" -> ");
		}
		
		buffer.append(StringUtils.LINE_SEPARATOR);
		
		this.out.println(buffer);
	}

	public void showBoard(Board board) {

		// Numero de digitos del tamaño del tablero.
		int max_indentation_length = (int) Math.log10(board.getBoardSize()) + 1;
		String max_left_indentation = createIndentation(max_indentation_length) + "   ";
		
		// Numero de "ROW_SEPARATOR_SYMBOL", que deben separar filas.
		int row_separator_length = 0;
		
		// Indentación para la primera fila de números.
		this.out.print(max_left_indentation);
		
		// Imprimimos línea de coordenadas del lado superior
		for(int i = 0; i < board.getBoardSize(); i++) {
			
			// Espacio extra que deben tener numeros de ciertas cifras
			int separationBetweenNumbers = max_indentation_length - (i == 0 ? 1 : (int) Math.log10(i) + 1);
			
			if(max_indentation_length % 2 == 0) {
				this.out.print(createIndentation(max_indentation_length / 2) + i + createIndentation(max_indentation_length / 2 + separationBetweenNumbers));
				row_separator_length += max_indentation_length + separationBetweenNumbers + (i == 0 ? 1 : (int) Math.log10(i) + 1);
			}
			
			else {
				this.out.print(createIndentation(max_indentation_length / 2 + 1) + i + createIndentation(max_indentation_length / 2 + separationBetweenNumbers + 2));
				row_separator_length += max_indentation_length + separationBetweenNumbers + 2 + (i == 0 ? 1 : (int) Math.log10(i) + 1);
			}
		}
		
		this.out.print(StringUtils.LINE_SEPARATOR);
		
		// Imprimimos el resto del tablero
		for(int i = 0; i < board.getBoardSize(); i++) {
			
			printRowSeparator(row_separator_length - 1, max_left_indentation);
				
			// Imprimir número de fila
			this.out.print(" ");
			
			int i_indentation_length = max_indentation_length - (i == 0 ? 1 : (int) Math.log10(i) + 1);
			String actual_left_indentation = createIndentation(i_indentation_length);
			
			this.out.print(actual_left_indentation + i + " ");
			
			// Imprimir lo correspondiente a esa fila
			for(int j = 0; j < board.getBoardSize(); j++) {
				Box box = board.getBoxAt(i, j);
				String boxContent = box.toString();
				
				if("".equals(boxContent)) {
					SpecialEffects speEff = box.getSpecialEffect();
					
					if(speEff != null) {
						switch(speEff) {
							case CENTRE: boxContent = CENTRE_SYMBOL; break;
							case DOUBLE_LETTER: boxContent = DOUBLE_LETTER_SYMBOL; break;
							case DOUBLE_WORD: boxContent = DOUBLE_WORD_SYMBOL; break;
							case TRIPLE_LETTER: boxContent = TRIPLE_LETTER_SYMBOL; break;
							case TRIPLE_WORD: boxContent = TRIPLE_WORD_SYMBOL; break;
							default: boxContent = " "; break;
						}
					}
					
					else boxContent = " ";
				}
				
				if(max_indentation_length % 2 == 0)
					this.out.print(COLUMN_SEPARATOR_SYMBOL + createIndentation(max_indentation_length/2) + boxContent + createIndentation(max_indentation_length/2));
				
				else this.out.print(COLUMN_SEPARATOR_SYMBOL + createIndentation(max_indentation_length/2 + 1) + boxContent + createIndentation(max_indentation_length/2 + 1));
			}
				
			this.out.print(COLUMN_SEPARATOR_SYMBOL + StringUtils.LINE_SEPARATOR);
		}
		
		printRowSeparator(row_separator_length - 1, max_left_indentation);
		
		this.out.print(" Doble letra: " + DOUBLE_LETTER_SYMBOL + " || " +
				         "Doble palabra: " + DOUBLE_WORD_SYMBOL + " || " +
				         "Triple letra: " + TRIPLE_LETTER_SYMBOL + " || " + 
				         "Triple palabra: " + TRIPLE_WORD_SYMBOL + 
				         StringUtils.LINE_SEPARATOR);
		
		this.out.print(StringUtils.LINE_SEPARATOR);
	}
	
	private String createIndentation(int length) {
		String indentation = "";
		for(int i = 0; i < length; i++)
			indentation += " ";
		return indentation;
	}
	
	private void printRowSeparator(int row_separator_length, String max_left_indentation) {
		
		this.out.print(max_left_indentation);
		for(int i = 0; i < row_separator_length; i++)
			this.out.print(ROW_SEPARATOR_SYMBOL);
		this.out.print(StringUtils.LINE_SEPARATOR);
	}

	public void showEndMessage(String message) {
		this.out.print(StringUtils.LINE_SEPARATOR);
		this.out.println(message);
		this.out.println("¡Gracias por jugar!");
	}

	@Override
	public void onWordWritten(String currentPlayerName, Board board, String word, int posX, int posY, String direction,
			int points, int extraPoints, int numPlayers, GamePlayers gamePlayers, int currentTurn) {
		this.out.println(String.format(
				"El jugador %s escribe la palabra \"%s\" en la posición (%s, %s) en dirección \"%s\".%n",
				currentPlayerName, word.toUpperCase(), posX, posY, direction.toUpperCase()));
		this.out.print(String.format("¡Gana %s puntos!", points));
		if(extraPoints != 0)
			this.out.println(String.format(" Además, ¡gana %s puntos extra!%n", extraPoints));
		else
			this.out.print(StringUtils.DOUBLE_LINE_SEPARATOR);
			
		pausa();
		
		showBoard(board);
	}

	@Override
	public void onPassed(int numPlayers, Board board, String currentPlayerName) {
		this.out.println(String.format("El jugador %s pasa de turno.%n", currentPlayerName));
		pausa();
		showBoard(board);
	}

	@Override
	public void onSwapped(String currentPlayerName, Board board, int numPlayers, GamePlayers gamePlayers, int currentTurn) {
		this.out.println(String.format("El jugador %s intercambia una ficha.%n", currentPlayerName));
	
		pausa();
		showBoard(board);
	}

	@Override
	public void onRegister(Board board, int numPlayers, boolean gameFinished, GamePlayers gamePlayers, int currentTurn) {
		
		this.out.print("¡Bienvenido al Scrabble!");
		this.out.print(StringUtils.LINE_SEPARATOR);

		Command.gameInitiated(Game.getGameInitiated());
		Command.playersAdded(numPlayers != 0);
		
		if (Game.getGameInitiated() && numPlayers != 0) {
			showBoard(board);
		}
		else {
			executeCommand();
		}
		
		if(!Game.getGameIsFinished())
			controller.update();
	}

	@Override
	public void onReset(Board board, int numPlayers, String currentTurnName, int remainingTiles, GamePlayers gamePlayers, int currentTurn) {
		
		this.out.print(StringUtils.LINE_SEPARATOR);
		this.out.print("Partida iniciada con éxito.");
		this.out.print(StringUtils.LINE_SEPARATOR);

		Command.gameInitiated(Game.getGameInitiated());
		Command.playersAdded(numPlayers != 0);
		
		if (Game.getGameInitiated() && numPlayers != 0) {
			showBoard(board);
		}
		else {
			executeCommand();
		}
		
		controller.update();
	}

	@Override
	public void onUpdate(boolean gameFinished, int numPlayers, String status, int remainingTiles, String currentTurnName, GamePlayers gamePlayers, int currentTurn) {
		
		Command.gameInitiated(Game.getGameInitiated());
		Command.playersAdded(numPlayers != 0);
		
		if(Game.getGameInitiated() && numPlayers != 0) {
			if(!gameFinished) {
				this.out.print(status);
				controller.playTurn();
			}
		}
		else {
			executeCommand();
		}
		
		controller.update();
	}
	
	@Override
	public void onError(String error) {
		this.out.println(error);
		
		controller.update();
	}

	@Override
	public void onEnd(String message) {
		showEndMessage(message);
		
		if(Game.isTestMode()) {
			try {
				controller.saveGame(outFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onFirstTurnDecided(String[] lettersObtained, GamePlayers gamePlayers, Board board, int numPlayers, int currentTurn) {
		showFirstTurn(lettersObtained, gamePlayers, currentTurn);
		pausa();
		showBoard(board);
	}
	
	@Override
	public void onMovementNeeded() {
		executeCommand();
	}
	
	private void pausa() {
		
		if(Game.isPausePermitted()) {
			this.out.print("Pulsa enter para continuar..." + StringUtils.LINE_SEPARATOR);
			this.in.nextLine();
		}
		
	}
	
	private void executeCommand() {
		
		try {
			controller.executeCommand(askCommand(), in , out);
		}
		
		catch(CommandParseException | CommandExecuteException e) {
			this.out.println(e.getMessage());
		}
	}
	
	private String[] askCommand() {
		
		this.out.print(PROMPT);
		String s = this.in.nextLine();
		
		s = StringUtils.removeAccents(s);
		
		if(!isInputFromConsole)
			this.out.print(s + StringUtils.LINE_SEPARATOR);

		String[] parameters = s.toLowerCase().trim().split(" ");
		
		return parameters;		
	}

}