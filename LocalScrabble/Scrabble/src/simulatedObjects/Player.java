package simulatedObjects;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Game;
import strategies.Strategy;
import utils.StringUtils;
import wordCheckers.WordChecker;

/* APUNTES GENERALES:
   
   La clase Player es una clase abstracta que encapsula el modelo de un jugador.
   
   Sus atributos son:
   
   - Nombre: así será identificado el jugador.
   - Lista de fichas: son las fichas con las que el jugador puede formar palabras.
   - Puntos totales: acumulación de puntos obtenidos de colocar palabras.
   - Estrategia: objeto de la clase Strategy que define la estrategia del jugador.
        
 */
public class Player {
	
	protected String name;
	protected List<Tile> tiles;
	private int totalPoints;
	private Strategy strategy;
	
	public Player(String name, int totalPoints, List<Tile> tiles, Strategy strategy) {
		this.name = name;
		this.tiles = tiles;
		this.totalPoints = totalPoints;
		this.strategy = strategy;
	}

	/* Método addTile:
	 * Añade al array de tipo Tile la ficha recibida por parámetro.
	 */
	public void addTile(Tile tile) {
		this.tiles.add(tile);
	}
	
	/* Método removeTile:
	 * Elimina del array de tipo Tile la ficha situada en el índice recibido por parámetro.
	 */
	public void removeTile(int tile) {
		this.tiles.remove(tile);		
	}
	
	/* Método removeTile:
	 * Elimina del array de tipo Tile la ficha recibida por parámetro.
	 */
	public void removeTile(Tile tile) {
		this.tiles.remove(tile);
	}
	
	/* Método play:
	 * Llama al método play de su estrategia correspondiente.
	 */
	public void play(Game game, WordChecker wordChecker) {
		this.strategy.play(game, wordChecker, tiles);
	}
	
	/* Método getStatus:
	 * Devuelve un String con la información del jugador:
	 * - Nombre.
	 * - Puntos totales.
	 * - Fichas y sus puntos asociados.
	 */
	public String getStatus() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(StringUtils.LINE_SEPARATOR).append("Turno de ")
		      .append(this.name).append(":")
			  .append(StringUtils.LINE_SEPARATOR);
		
		buffer.append("Puntos totales: ").append(totalPoints).append(StringUtils.LINE_SEPARATOR);
	
		buffer.append("Fichas (letra y puntos asociados a ella):").append(StringUtils.LINE_SEPARATOR);
		for(int i = 0; i < tiles.size(); i++) {
			buffer.append(tiles.get(i));
			if(i != tiles.size() - 1)
				buffer.append(" || ");
		}
		
		buffer.append(StringUtils.LINE_SEPARATOR);
		return buffer.toString();
	}

	/* Método hasLetter:
	 * Devuelve un booleano indicando si un jugador tiene en sus fichas
	 * la letra recibida por parámetro.
	 */
	public boolean hasLetter(String letter) {
		
		for(int i = 0; i < this.tiles.size(); ++i)
			if(this.tiles.get(i).getLetter().equalsIgnoreCase(letter))
				return true;
		
		return false;
	}

	/* Método numberOfTilesOf:
	 * Devuelve el número de fichas con la letra recibida 
	 * por parámetro que tiene el jugador
	 */
	public int numberOfTilesOf(String letter) {
		
		int numberOfTiles = 0;
		for (int i = 0; i < this.tiles.size(); ++i)
			if (this.tiles.get(i).getLetter().equalsIgnoreCase(letter)) 
				++numberOfTiles;
		
		return numberOfTiles;
	}

	/* Método givePoints:
	 * Suma a la cantidad actual de puntos, los recibidos por parámetro.
	 */
	public void givePoints(int points) {
		this.totalPoints += points;
	}
	

	// Getters
	
	public String getName() {
		return name;
	}
	
	public int getNumTiles() {
		return tiles.size();
	}
	
	public Tile getTile(int tile) {
		return this.tiles.get(tile);
	}
	
	public Tile getTile(String letter) {
		
		for(int i = 0; i < this.tiles.size(); ++i)
			if(this.tiles.get(i).getLetter().equalsIgnoreCase(letter))
				return this.tiles.get(i);
		
		return null;
	}

	public int getPoints() {
		return this.totalPoints;
	}

	public List<Tile> getTiles() {
		return Collections.unmodifiableList(this.tiles);
	}
	
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("name", name.substring(0, name.indexOf(" (")));
		
		jo.put("total_points", this.totalPoints);
		
		JSONArray tiles = new JSONArray();
		for(int i = 0; i < this.tiles.size(); ++i)
			tiles.put(this.tiles.get(i).report());
		
		jo.put("tiles", tiles);
		
		JSONObject strategy = new JSONObject();
		strategy.put("strategy_type", this.strategy.toString());
		jo.put("strategy", strategy);
		
		return jo;
	}
}
