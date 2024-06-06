package command;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import exceptions.CommandExecuteException;
import exceptions.CommandParseException;
import logic.Game;
import scrabble.Controller;

// Ver apuntes de la clase padre Command.
public class ResetCommand extends Command {

	private static final String NAME = "reset";

	private static final String DETAILS = "[r]eset [semilla]";

	private static final String SHORTCUT = "r";

	private static final String HELP = "resetear la partida";
	
	public ResetCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}
	
	/* Sobrescritura del método execute:
	 * Delega en la clase Controller el reseteo del juego.
	 */
	
	@Override
	public void execute(Controller controller, Scanner in, PrintStream out) throws CommandExecuteException {
		
		try {
			controller.reset();
		}
		
		catch(FileNotFoundException fnfe) {
			throw new CommandExecuteException("El fichero de reseteo no se ha podido encontrar.", fnfe);
		}
	}
	
	
	@Override
	protected Command parse(String[] words) throws CommandParseException {
		
		if (!matchCommandName(words[0])) 
			return null;
		
		if (words.length != 2)
			throw new CommandParseException(String.format("[ERROR]: Comando %s: %s%n", words[0], INCORRECT_NUMBER_OF_ARGS_MSG));
		
		try {
			Game.setSeed(Integer.parseInt(words[1]));
		}
		
		catch(NumberFormatException nfe) {
			throw new CommandParseException("[ERROR]: la semilla debe ser un número");
		}
		
		return this;
	}
	
 
}
