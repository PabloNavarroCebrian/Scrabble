package command;

import java.io.PrintStream;
import java.util.Scanner;

import scrabble.Controller;

// Ver apuntes de la clase padre Command.
public class SwapTileCommand extends Command {
	
	private static final String NAME = "intercambio";

	private static final String DETAILS = "[i]ntercambio";

	private static final String SHORTCUT = "i";

	private static final String HELP = "intercambiar una ficha por una del saco";
	
	public SwapTileCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}
	
	/* Sobrescritura del método execute:
	 * Delega en la clase Controller la acción de cambio de ficha.
	 * El método swapTile() devuelve falso si no se puede robar ficha.
	 * Por tanto, en este caso, se debe devolver true para que el
	 * actual jugador pueda realizar otra acción si lo desea.
	 */
	
	@Override
	public void execute(Controller controller, Scanner in, PrintStream out) {
		controller.swapTile();
	}
}
