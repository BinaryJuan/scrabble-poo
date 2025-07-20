package modelo.scrabble;

import java.io.Serializable;
import java.util.HashMap;

public final class PuntajeFichas implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static HashMap<String, Integer> fichas = new HashMap<>();
	
	//CONSTRUCTOR
	public PuntajeFichas() {
		cargarFichas();
	}
	
	private static void cargarFichas() {
		//FICHAS.
		fichas.put("A", 1); fichas.put("B", 3); fichas.put("C", 3); fichas.put("D", 2); fichas.put("E", 1);
        fichas.put("F", 4); fichas.put("G", 2);fichas.put("H", 4); fichas.put("I", 1); fichas.put("J", 8); fichas.put("L", 1);
        fichas.put("M", 3); fichas.put("N", 1); fichas.put("Ñ", 8); fichas.put("O", 1); fichas.put("P", 3); fichas.put("Q", 5);
        fichas.put("R", 1); fichas.put("S", 1); fichas.put("T", 1); fichas.put("U", 1);
        fichas.put("V", 4); fichas.put("X", 8); fichas.put("Y", 4); fichas.put("Z", 10);
        fichas.put(" ", 0);
	}
	
	public static Integer getPuntaje(String letra) {
		return fichas.getOrDefault(letra, 0);
	}

}
