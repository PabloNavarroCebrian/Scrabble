package command;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import org.json.JSONException;

import exceptions.CommandExecuteException;
import exceptions.CommandParseException;
import scrabble.Controller;
import utils.StringUtils;

// Ver apuntes de la clase padre Command.
public class LoadCommand extends Command {

	private static final String NAME = "cargar";

	private static final String DETAILS = "[c]argar [nombre del fichero]";

	private static final String SHORTCUT = "c";

	private static final String HELP = "cargar una partida de fichero";
	
	private String file;
	
	public LoadCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}
	
	/* Sobrescritura del método execute:
	 * Delega en la clase Controller la carga del juego.
	 */
	
	@Override
	public void execute(Controller controller, Scanner in, PrintStream out) throws CommandExecuteException {
		
		try {
			controller.loadGame(this.file);
		}
		
		catch(FileNotFoundException fnfe) {
			throw new CommandExecuteException("El fichero introducido no se ha podido encontrar.", fnfe);
		}
		
		catch(JSONException je) {
			throw new CommandExecuteException("El formato JSON del fichero introducido no es válido." + StringUtils.LINE_SEPARATOR + je.getMessage(), je);
		}
	}
	
	/* Sobrescritura del método parse:
	 * Comprueba que los argumentos recibidos se correspondan con los del comando cargar,
	 * es decir, que el usuario haya introducido exactamente el nombre del comando y un nombre de fichero.
	 */
	
	@Override
	protected Command parse(String[] words) throws CommandParseException {
		
		if (!matchCommandName(words[0])) 
			return null;
		
		if (words.length != 2)
			throw new CommandParseException(String.format("[ERROR]: Comando %s: %s%n", words[0], INCORRECT_NUMBER_OF_ARGS_MSG));

		this.file = words[1];
		
		file = "resources/existingGames/" + file;
		
		if(!file.endsWith(".json"))
			file += ".json";
		
		return this;
	}
}
