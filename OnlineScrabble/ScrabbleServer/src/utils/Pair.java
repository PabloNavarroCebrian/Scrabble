package utils;

/* APUNTES GENERALES:
   
   La clase Pair es usada para representar pares de valores <T1, T2>.
   Contiene dos únicos atributos: first (de tipo T1) y second (de tipo T2).
 */
public class Pair <T1, T2> {
	
	private T1 first;
	private T2 second;

	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

}
