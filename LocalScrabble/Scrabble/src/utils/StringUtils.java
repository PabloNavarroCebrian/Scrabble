package utils;

/* APUNTES GENERALES:

	La clase StringUtils es usada para recoger constantes útiles como el salto de línea,
	no dependiente del sistema operativo. 
	
	Incluye un método estático de eliminado de tildes en palabras.
	Incluye un método estático que cuenta el número de ocurrencias de un string en una otro.
*/

public class StringUtils {
	
	public static final String LINE_SEPARATOR = System.lineSeparator();
	public static final String DOUBLE_LINE_SEPARATOR = System.lineSeparator() + System.lineSeparator();

	public static String removeAccents(String word) {
		
		return word.replace('á', 'a').replace('é', 'e')
		    .replace('í', 'i').replace('ó', 'o')
		    .replace('ú', 'u').replace('A', 'A')
		    .replace('É', 'E').replace('Í', 'I')
		    .replace('Ó', 'O').replace('Ú', 'U');
	}

	public static int numberOfOcurrencesOf(String wordToBeFound, String word) {
		
		int numberOfOcurrences = 0;
		
		for(int i = 0; i < word.length(); i++) {
			if(word.charAt(i) == wordToBeFound.charAt(0)) {
				int rightLimit = i + wordToBeFound.length();
				if(rightLimit <= word.length() && word.substring(i, rightLimit).equals(wordToBeFound))
					numberOfOcurrences++;
			}
		}
			
		return numberOfOcurrences;
	}
}
