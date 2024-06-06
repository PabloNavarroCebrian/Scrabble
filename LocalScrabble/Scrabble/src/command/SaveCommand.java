package command;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import exceptions.CommandExecuteException;
import exceptions.CommandParseException;
import scrabble.Controller;
import utils.StringUtils;

// Ver apuntes de la clase padre Command.
public class SaveCommand extends Command {

	private static final String NAME = "guardar";

	private static final String DETAILS = "[g]uardar [nombre del fichero]";

	private static final String SHORTCUT = "g";

	private static final String HELP = "guardar una partida en un fichero";
	
	private String file;
	
	public SaveCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}
	
	/* Sobrescritura del método execute:
	 * Delega en la clase Controller el guardado del juego.
	 */
	
	@Override
	public void execute(Controller controller, Scanner in, PrintStream out) throws CommandExecuteException {
		
		try {
			controller.saveGame(this.file);
			
			out.print(StringUtils.LINE_SEPARATOR);
			out.println("La partida ha sido guardada con éxito.");
			out.print(StringUtils.LINE_SEPARATOR);
		}
		
		catch(FileNotFoundException fnfe) {
			throw new CommandExecuteException("El fichero introducido no es válido", fnfe);
		}
		
		catch(IllegalArgumentException iae) {
			throw new CommandExecuteException(iae.getMessage(), iae);
		}
	}
	
	/* Sobrescritura del método parse:
	 * Comprueba que los argumentos recibidos se correspondan con los del comando guardar,
	 * es decir, que el usuario haya introducido exactamente el nombre del comando y un nombre de fichero.
	 */
	@Override
	protected Command parse(String[] words) throws CommandParseException {
		
		if (!matchCommandName(words[0])) 
			return null;
		
		if (words.length != 2)
			throw new CommandParseException(String.format("[ERROR]: Comando %s: %s%n", words[0] ,INCORRECT_NUMBER_OF_ARGS_MSG));
		
		this.file = words[1];
		
		return this;
	}
	
	
}
