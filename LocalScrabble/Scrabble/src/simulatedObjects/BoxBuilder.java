package simulatedObjects;

import org.json.JSONObject;

public class BoxBuilder {
	
	private TileBuilder tileBuilder;

	public BoxBuilder(TileBuilder tileBuilder) {
		this.tileBuilder = tileBuilder;
	}
	
	/* Método createBox:
	 * Este método construye un objeto de la clase Box, es decir,
	 * una casilla del tablero. La casilla tiene la posibilidad de
	 * tener asociados un efecto especial y una letra, las cuales vienen
	 * especificadas en el JSONObject recibido por parámetro.
	 */
	
	public Box createBox(JSONObject data) {
		
		SpecialEffects specialEffect = null;
		
		if(data.has("special_effect"))
			specialEffect = SpecialEffects.valueOf(data.getString("special_effect"));
		
		if(data.has("tile")) {
			Tile tile = tileBuilder.createTile(data.getJSONObject("tile"));
			return new Box(specialEffect, tile, true);
		}
		
		else return new Box(specialEffect, null, false);
	}

}
