package command;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import exceptions.CommandExecuteException;
import scrabble.Controller;

// Ver apuntes de la clase padre Command.
public class NewGameCommand extends Command{

	private static final String NAME = "nueva";

	private static final String DETAILS = "[n]ueva";

	private static final String SHORTCUT = "n";

	private static final String HELP = "empezar una nueva partida";
	
	public NewGameCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}
	
	/* Sobrescritura del método execute:
	 * Delega en la clase Controller la creación de una nueva partida.
	 */
	
	@Override
	public void execute(Controller controller, Scanner in, PrintStream out) throws CommandExecuteException {
		
		try {
			controller.newGame();
		}
		catch(FileNotFoundException fnfe) {
			throw new CommandExecuteException("El fichero de nueva partida no se ha podido encontrar.", fnfe);
		}
	}
}
