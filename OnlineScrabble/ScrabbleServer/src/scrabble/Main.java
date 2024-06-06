package scrabble;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import logic.Game;
import server.Server;
import storage.GameLoader;

/* APUNTES GENERALES:
   
   La clase Main es la clase donde comienza la ejecución de la aplicación.
  
 */
public class Main {
	
	private static int numHumanPlayers;
	private static int numAutomaticPlayers;
	private static String strategy1;
	private static String strategy2;
	private static int port;
	
	private static Options buildOptions() {
		
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("hp").longOpt("humanPlayers").hasArg().desc("Número de jugadores humanos.").build());
		cmdLineOptions.addOption(Option.builder("ap").longOpt("automaticPlayers").hasArg().desc("Número de jugadores automáticos.").build());
		cmdLineOptions.addOption(Option.builder("s1").longOpt("strategy1").hasArg().desc("Estrategia del jugador automático 1.").build());
		cmdLineOptions.addOption(Option.builder("s2").longOpt("strategy2").hasArg().desc("Estrategia del jugador automático 2.").build());
		cmdLineOptions.addOption(Option.builder("p").longOpt("port").hasArg().desc("Puerto.").build());
		cmdLineOptions.addOption(Option.builder("a").longOpt("ayuda").desc("Imprime esta ayuda").build());
		
		return cmdLineOptions;
	}
	
	private static void parseHumanPlayersOption(CommandLine line) throws ParseException {
		
		if(line.hasOption("hp")) {
			try {
				numHumanPlayers = Integer.parseInt(line.getOptionValue("hp"));
				if(numHumanPlayers < 2 || numHumanPlayers > 4)
					throw new ParseException("El número de jugadores humanos debe ser un número entre 2 y 4.");
			}
			catch(NumberFormatException nfe) {
				throw new ParseException("El número de jugadores humanos debe ser un número.");
			}
		}
		else
			throw new ParseException("Se debe introducir el puerto del servidor.");
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("a")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseAutomaticPlayersOption(CommandLine line) throws ParseException {
		
		if(line.hasOption("ap")) {
			try {
				numAutomaticPlayers = Integer.parseInt(line.getOptionValue("hp"));
				if(numAutomaticPlayers < 2 || numAutomaticPlayers > 4)
					throw new ParseException("El número de jugadores automaticos debe ser un número entre 1 y 2.");
			}
			catch(NumberFormatException nfe) {
				throw new ParseException("El número de jugadores automaticos debe ser un número.");
			}
		}
		else
			throw new ParseException("Se debe introducir el puerto del servidor.");
	}
	
	private static void parseStrategiesOptions(CommandLine line) throws ParseException {
		
		if(numAutomaticPlayers == 1 || numAutomaticPlayers == 2) {
			if(line.hasOption("s1")) {
				strategy1 = checkStrategy(line.getOptionValue("s1"));
				if(strategy1 == null)
					throw new ParseException("La estrategia del jugador automático 1 no es válida [facil (easy), media(medium), dificil(hard)]");
			}
			else 
				throw new ParseException("Se debe introducir la estrategia del jugador automático 1 [facil (easy), media(medium), dificil(hard)].");
		}
		
		if(numAutomaticPlayers == 2) {
			if(line.hasOption("s2")) {
				strategy2 = checkStrategy(line.getOptionValue("s2"));
				if(strategy2 == null)
					throw new ParseException("La estrategia del jugador automatico 2 no es valida [facil (easy), media(medium), dificil(hard)]");
			}
			else 
				throw new ParseException("Se debe introducir la estrategia del jugador automatico 2 [facil (easy), media(medium), dificil(hard)].");
		}
	}
	
	private static String checkStrategy(String strategy) {

		if(strategy.equalsIgnoreCase("facil") || strategy.equalsIgnoreCase("easy"))
			return "easy";
		
		if(strategy.equalsIgnoreCase("media") || strategy.equalsIgnoreCase("medium"))
			return "medium";
		
		if(strategy.equalsIgnoreCase("dificil") || strategy.equalsIgnoreCase("hard"))
			return "hard";
		
		return null;
	}

	private static void parsePortOption(CommandLine line) throws ParseException {
		if(line.hasOption("p")) {
			try {
				port = Integer.parseInt(line.getOptionValue("p"));
			}
			catch(NumberFormatException nfe) {
				throw new ParseException("Se debe introducir el puerto del servidor.");
			}
		}
		else
			throw new ParseException("Se debe introducir el puerto del servidor.");
	}
	
	private static void parseArgs(String[] args) {

		// Definimos las lineas de comando válidas
		Options cmdLineOptions = buildOptions();

		// Parseamos la línea de comandos introducida.
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			
			parseHelpOption(line, cmdLineOptions);
			
			parseHumanPlayersOption(line);
			
			parseAutomaticPlayersOption(line);
			
			parseStrategiesOptions(line);
			
			parsePortOption(line);
			
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
	}
	
	private static void start(String[] args) throws IOException {
		
		parseArgs(args);
		
		GameLoader.initBuilders();
		Game.initWordList();

		Server server = new Server(numHumanPlayers, numAutomaticPlayers, strategy1, strategy2, port);
		server.start();
	}

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
