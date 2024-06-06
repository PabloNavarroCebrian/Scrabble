package simulatedObjects;

import java.util.List;

import strategies.Strategy;

/* APUNTES GENERALES:
   
   La clase Player es una clase abstracta que encapsula el modelo de un jugador.
   
   Sus atributos son:
   
   - Nombre: así será identificado el jugador.
   - Lista de fichas: son las fichas con las que el jugador puede formar palabras.
   - Puntos totales: acumulación de puntos obtenidos de colocar palabras.
   - Objeto de la clase Random: se emplea para establecer una posición de palabra
     aleatoria en los jugadores automáticos.
     
   El atributo final y constante "movingBoxes" es empleado por los jugadores automáticos
   para recorrer las posiciones del tablero de forma ordenada.
   
 */
public class Player {
	
	protected String name;
	protected List<Tile> tiles;
	private int totalPoints;
	
	public Player(String name, int totalPoints, List<Tile> tiles, Strategy strategy) {
		this.name = name;
		this.tiles = tiles;
		this.totalPoints = totalPoints;
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

	public int getPoints() {
		return this.totalPoints;
	}
}
