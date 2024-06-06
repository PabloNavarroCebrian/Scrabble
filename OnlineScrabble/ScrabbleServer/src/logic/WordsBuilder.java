package logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class WordsBuilder {

	/* Sobrescritura del método createTheInstance:
	 * Construye y devuelve la lista que contiene las palabras usadas del juego.
	 */

	public List<String> createWords(JSONObject data) {
		
		JSONArray jsonArrayWords = data.getJSONArray("words");
		
		List<String> words = new ArrayList<String>();
		
		for(int i = 0; i < jsonArrayWords.length(); i++)
			words.add(jsonArrayWords.getString(i));
		
		return words;
	}
}
