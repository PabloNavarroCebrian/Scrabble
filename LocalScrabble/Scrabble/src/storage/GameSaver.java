package storage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import logic.Game;
import utils.StringUtils;

/* APUNTES GENERALES:
   
   La clase GameSaver es la encargada de guardar el estado del juego actual en un fichero.
   Las partidas guardadas se encuentran en la carpeta resources/existingGames del src.
 */

public class GameSaver {

	public static void saveGame(Game game, String file) throws FileNotFoundException, IllegalArgumentException {
		
		checkFileFormat(file);
		file = StringUtils.removeAccents(file);
		file = file.toLowerCase();
		if(!file.endsWith(".json"))
			file += ".json";
		
		if (("resources/existingGames/" + file).equals(GameLoader.NEW_GAME))
			throw new IllegalArgumentException("No se puede sobrescribir el fichero de nueva partida.");
		
		OutputStream out;
		if(!Game.isTestMode())
			out = new FileOutputStream("resources/existingGames/" + file);
		else out = new FileOutputStream(file);
		
		@SuppressWarnings("resource")
		PrintStream p = new PrintStream(out);
		
		p.print(game.report());
	}
	
	/* Método checkFileFormat:
	 * Se encarga de lanzar excepciones en caso de encontrarse con un formato
	 * de fichero poco deseable (tener más de un punto, no tener una única extension .json,
	 * y que el nombre del fichero sea vacío).
	 */
	private static void checkFileFormat(String file) throws IllegalArgumentException {
		
		if(StringUtils.numberOfOcurrencesOf(".", file) > 1) {
			throw new IllegalArgumentException("Formato incorrecto: el fichero no puede tener extensión múltiple." + StringUtils.LINE_SEPARATOR);
		}
		
		int lastIndex = file.lastIndexOf('.');
		
		if(lastIndex != -1) {
			if(!file.substring(lastIndex, file.length()).equalsIgnoreCase(".json"))
				throw new IllegalArgumentException("Formato incorrecto: extensión no válida." + StringUtils.LINE_SEPARATOR);
		
			else if("".equalsIgnoreCase(file.substring(0, lastIndex)))
				throw new IllegalArgumentException("Formato incorrecto: el nombre del archivo no puede ser vacío." + StringUtils.LINE_SEPARATOR);
		}
	}
}
