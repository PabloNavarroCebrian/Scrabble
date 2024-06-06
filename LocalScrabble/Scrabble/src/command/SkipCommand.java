package command;

import java.io.PrintStream;
import java.util.Scanner;

import scrabble.Controller;

// Ver apuntes de la clase padre Command.
public class SkipCommand extends Command {

	private static final String NAME = "skip";

	private static final String DETAILS = "[s]kip";

	private static final String SHORTCUT = "s";

	private static final String HELP = "pasar de turno";
	
	public SkipCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}
	
	/* Sobrescritura del método execute:
	 * Delega en la clase Controller el paso de turno.
	 */
	
	@Override
	public void execute(Controller controller, Scanner in, PrintStream out) {
		controller.passTurn();
	}
}
