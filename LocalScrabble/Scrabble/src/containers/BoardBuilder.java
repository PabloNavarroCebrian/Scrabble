package containers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulatedObjects.Box;
import simulatedObjects.BoxBuilder;

public class BoardBuilder {

	private BoxBuilder boxBuilder;
	
	public BoardBuilder(BoxBuilder boxBuilder) {
		this.boxBuilder = boxBuilder;
	}
	
	/* Sobrescritura del método createTheInstance:
	 * 
	 * El método construye devuelve un objeto de la clase Board
	 * a partir del JSONObject data.
	 * 
	 * En el fichero JSON, el tablero se representa mediante un JSONArray
	 * de JSONArrays. Para cada casilla del tablero se llama al objeto de
	 * la clase BoxBuilder, y según se van completando las filas, se van
	 * añadiendo a la matriz de objetos Box "board".
	 */

	public Board createBoard(JSONObject data) {
		
		JSONArray jsonArrayRows = data.getJSONArray("board");
		List<List<Box>> board = new ArrayList<List<Box>>();
		
		for(int i = 0; i < jsonArrayRows.length(); i++) {
			
			JSONArray jsonArrayBoxes = jsonArrayRows.getJSONArray(i);
			List<Box> rowOfBoxes = new ArrayList<Box>();
			
			for(int j = 0; j < jsonArrayBoxes.length(); j++)
				rowOfBoxes.add(boxBuilder.createBox(jsonArrayBoxes.getJSONObject(j)));
			
			board.add(rowOfBoxes);
		}
		
		return new Board(board);
	}

}
