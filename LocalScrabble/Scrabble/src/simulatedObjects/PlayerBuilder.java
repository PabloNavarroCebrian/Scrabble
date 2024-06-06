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
	
	/* Método createPlayer:
	 * 
	 * El método construye un objecto de tipo Player.
	 * 
	 * Por un lado, el parámetro k sirve para enumerar a los jugadores automáticos.
	 * 
	 * Por otro lado, en el JSONObject recibido por parámetro, se pueden dar algunas alternativas:
	 * 
	 * - Si el tipo del JSONObject no coincide con el tipo del jugador actual, se devuelve null
	 *   (esto se usa en la clase GamePlayersBuilder para distinguir cuando el fichero .json
	 *   podría tener un error).
	 *   
	 * - Si el jugador tiene una clave "name", se asocia el nombre del jugador a este.
	 *   En caso contrario, se trata de un jugador automático sin nombre, y se le asigna AUTOMATIC_PLAYER_NAME.
	 *   
	 * - Si el jugador tiene fichas asociadas, se crea el correspondiente array de objetos Tile.
	 * 
	 * - También se construye su estrategia a partir de la especificada en el JSON recibido por parámetro.
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
