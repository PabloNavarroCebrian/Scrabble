package command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exceptions.CommandExecuteException;
import exceptions.CommandParseException;
import scrabble.Controller;

/* APUNTES GENERALES:
   
   La clase Command es una clase abstracta que 
   encapsula la principal funcionalidad de los comandos.
   
   Cada comando tiene un nombre (name), un atajo (shortcut),
   su formato de entrada (details) y una descripcion (help).
   
   El metodo parse es sobreescrito en LoadCommand, SaveCommand y WriteWordCommand.
   
   El metodo execute, al ser abstracto, es sobreescrito por todas los comandos que
   heredan de esta clase.
   
 */
public abstract class Command {

	private static final String UNKNOWN_COMMAND_MSG = "Comando desconocido";
	protected static final String INCORRECT_NUMBER_OF_ARGS_MSG = "Número incorrecto de argumentos";

	protected static List<Command> AVAILABLE_COMMANDS = new ArrayList<Command>() {
		
		private static final long serialVersionUID = 1L;

		{
			add(new ExitCommand());
			add(new HelpCommand());
			add(new NewGameCommand());
			add(new LoadCommand());
		}
	};

	private static final List<Command> playersAddedCommands = new ArrayList<Command>() {
		
		private static final long serialVersionUID = 1L;

		{
			add(new SkipCommand());
			add(new NewGameCommand());
			add(new ResetCommand());
			add(new SwapTileCommand());
			add(new SaveCommand());
			add(new WriteWordCommand());
		}
	};
	
	private static final Command addPlayersCommand = new AddPlayersCommand();

	private final String name;

	private final String shortcut;

	private final String details;

	private final String help;

	public Command(String name, String shortcut, String details, String help) {
		this.name = name;
		this.shortcut = shortcut;
		this.details = details;
		this.help = help;
	}

	// Métodos comunes (posiblemente sobrescribibles) para todos los comandos

	/*
	 * Método matchCommandName: Devuelve un booleano indicando si el nombre del
	 * comando introducido se corresponde con el nombre establecido o con su atajo.
	 * Este método nunca es sobrescrito.
	 */
	protected boolean matchCommandName(String name) {
		return this.shortcut.equalsIgnoreCase(name) || this.name.equalsIgnoreCase(name);
	}

	/*
	 * Método parse: Para cualquier comando que no sobreescriba este método, el
	 * método parse devuelve el comando correspondiente en caso de que haya sido
	 * introducido correctamente por el usuario, y en caso contrario, devuelve null.
	 * Este método es sobrescrito por LoadCommand, SaveCommand y WriteWordCommand.
	 */
	protected Command parse(String[] words) throws CommandParseException {
		if (matchCommandName(words[0])) {

			if (words.length != 1)
				throw new CommandParseException(
						String.format("[ERROR]: Comando %s: %s%n", name, INCORRECT_NUMBER_OF_ARGS_MSG));
			else
				return this;

		}

		return null;
	}

	/* Método getCommand:
	 * Recibe un supuesto comando del usuario. Si no existe, se
	 * devuelve null. Si existe, se devuelve el comando asociado. 
	 * Este método nunca es sobrescrito.
	 */
	public static Command getCommand(String[] commandWords) throws CommandParseException {

		Command command = null;

		int i = 0;

		while (command == null && i < AVAILABLE_COMMANDS.size()) {
			command = AVAILABLE_COMMANDS.get(i).parse(commandWords);
			++i;
		}

		if (command == null) {
			throw new CommandParseException(String.format("[ERROR]: %s%n", UNKNOWN_COMMAND_MSG));
		}

		return command;
	}

	/* Método gameInitiated:
	 * Este método es empleado para añadir nuevos comandos disponibles al
	 * usuario en función de si el juego ha sido iniciado.
	 * 
	 * Si el juego ha sido iniciado, se elimina el comando de añadir jugadores.
	 * En caso contrario, se añade (si no lo tuviera ya).
	 */
	public static void gameInitiated(boolean gameInitiated) {
		if(gameInitiated) {
			if (!AVAILABLE_COMMANDS.contains(addPlayersCommand))
				AVAILABLE_COMMANDS.add(addPlayersCommand);
		}
		
		else AVAILABLE_COMMANDS.remove(addPlayersCommand);
	}
	
	/* Método playersAdded:
	 * Este método añade o quita los comandos asociados a una partida en la que
	 * los jugadores han sido añadidos.
	 * 
	 * Si los jugadores han sido añadidos, se añaden (si nos lo contienen ya).
	 * En caso contrario, se eliminan.
	 */
	public static void playersAdded(boolean playersAdded) {
		
		if(playersAdded) {
			for(Command command : playersAddedCommands) {
				if(!AVAILABLE_COMMANDS.contains(command))
					AVAILABLE_COMMANDS.add(command);
			}
		}
		else AVAILABLE_COMMANDS.removeAll(playersAddedCommands);
	}
	
	// Getters

	public String getDetails() {
		return this.details;
	}

	public String getHelp() {
		return this.help;
	}

	// Métodos abstractos

	/*
	 * Método execute: Cada comando que hereda de esta clase sobreescribe su propio
	 * método execute con su funcionalidad concreta. El método devuelve un booleano
	 * indicando si el jugador actual debe seguir jugando su turno o pasar al
	 * siguiente.
	 */
	public abstract void execute(Controller controller, Scanner in, PrintStream out) throws CommandExecuteException;
}
