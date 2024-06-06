package containers;

import java.util.List;

import simulatedObjects.Box;

/* APUNTES GENERALES
   
   La clase Board es usada para modelizar el tablero de juego.
   
   Consta de tan solo dos atributos: el tablero (matriz de objetos Box),
   y la posición (como un par), de la casilla central.
 */
public class Board {
	
	private List<List<Box>> board;
	
	public Board(List<List<Box>> board) {
		this.board = board; 
	}

	// Getters

	public int getBoardSize() {
		return this.board.size();
	}

	public Box getBoxAt(int posX, int posY) {
		return board.get(posX).get(posY);
	}
}
