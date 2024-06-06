package logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class WordsBuilder {

	/* Método createWords:
	 * Construye y devuelve una lista de palabras (ya sean las válidas o las usadas),
	 * a partir del objeto JSON recibido por parámetro.
	 */

	public List<String> createWords(JSONObject data) {
		
		JSONArray jsonArrayWords = data.getJSONArray("words");
		
		List<String> words = new ArrayList<String>();
		
		for(int i = 0; i < jsonArrayWords.length(); i++)
			words.add(jsonArrayWords.getString(i));
		
		return words;
	}
}
