package simulatedObjects;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import strategies.Strategy;
import strategies.StrategyBuilder;

/* APUNTES GENERALES
   
   Ver apuntes de la clase padre Builder.
   
   La clase PlayerBuilder es abstracta, dado que se puede
   reutilizar esta misma clase para construir los jugadores
   clasificados por dificultad.
   
 */
public class PlayerBuilder {
	
	private static final String AUTOMATIC_PLAYER_NAME = "CPU";

	private TileBuilder tileBuilder;
	private List<StrategyBuilder> strategyBuilders;
	
	public PlayerBuilder(TileBuilder tileBuilder, List<StrategyBuilder> strategyBuilders) {
		this.tileBuilder = tileBuilder;
		this.strategyBuilders = strategyBuilders;
	}
	
	/* Sobrescritura del método createTheInstance:
	 * 
	 * El método construye un objecto polimórfico de tipo estático Player,
	 * y de tipo dinámico HumanPlayer, EasyPlayer, MediumPlayer o HardPlayer,
	 * y lo devuelve.
	 * 
	 * En el JSONObject recibido por parámetro, se pueden dar algunas alternativas:
	 * 
	 * - Si el tipo del JSONObject no coincide con el tipo del jugador actual, se devuelve null
	 *   (esto se usa en la clase GamePlayersBuilder para distinguir cuando el fichero .json
	 *   podría tener un error).
	 *   
	 * - Si el jugador tiene una clave "name", se asocia el nombre del jugador a este.
	 *   En caso contrario, se trata de un jugador automático sin nombre, y se le asigna "CPU".
	 *   
	 * - Si el jugador tiene fichas asociadas, se crea el correspondiente array de objetos Tile.
	 */

	public Player createPlayer(JSONObject data, int k) {
		
		String name;
		if(data.has("name"))
			name = data.getString("name");
		else name = AUTOMATIC_PLAYER_NAME + " " + (k + 1);
		
		int totalPoints = data.getInt("total_points");
		List <Tile> tiles = new ArrayList<Tile>();
		
		if(data.has("tiles")) {
			
			JSONArray jsonArrayTiles = data.getJSONArray("tiles");
			for(int i = 0; i < jsonArrayTiles.length(); i++)
				tiles.add(tileBuilder.createTile(jsonArrayTiles.getJSONObject(i)));
		}
		
		Strategy strategy = null;
		for(StrategyBuilder sb : strategyBuilders) {
			strategy = sb.createStrategy(data.getJSONObject("strategy"));
			
			if(strategy != null) {
				String strat = strategy.toString();
				int index = strat.indexOf("_");
				
				name += " (" + strat.substring(0, index) + ")";
				break;
			}
				
		}
		
		if(strategy == null)
			throw new InputMismatchException("El JSON no es válido (strategy).");
		
		return new Player(name, totalPoints, tiles, strategy);
	}
}
