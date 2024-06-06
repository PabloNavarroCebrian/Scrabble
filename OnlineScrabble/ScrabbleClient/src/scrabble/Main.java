package scrabble;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import client.Client;

/* APUNTES GENERALES:
   
   La clase Main es la clase donde comienza la ejecución de la aplicación.
  
*/
public class Main {
	
	private static String name;
	private static String IP;
	private static int port;
	
	private static Options buildOptions() {
		
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("n").longOpt("name").hasArg().desc("Nombre del jugador.").build());
		cmdLineOptions.addOption(Option.builder("IP").longOpt("serverIP").hasArg().desc("IP del servidor.").build());
		cmdLineOptions.addOption(Option.builder("p").longOpt("port").hasArg().desc("Puerto.").build());
		cmdLineOptions.addOption(Option.builder("a").longOpt("ayuda").desc("Imprime esta ayuda").build());
		
		return cmdLineOptions;
	}
	
	private static void parseNameOption(CommandLine line) throws ParseException {
		
		if(line.hasOption("n"))
			name = line.getOptionValue("n");
		else
			throw new ParseException("Se debe introducir el nombre con el que quieres jugar.");
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("a")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseIDOption(CommandLine line) throws ParseException {
		if(line.hasOption("IP"))
			IP = line.getOptionValue("IP");
		else
			throw new ParseException("Se debe introducir la IP del servidor.");
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
			
			parseNameOption(line);
			
			parseIDOption(line);
			
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

		Client client = new Client(name, IP, port);
		client.start();
	}

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
}
