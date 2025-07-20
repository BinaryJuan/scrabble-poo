package modelo.scrabble;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Palabra implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String palabra = "";
	private List<Letra> letras = new ArrayList<>();
	
	public Palabra(String palabra) {
		// Validación básica
		if (palabra == null || palabra.trim().isEmpty()) {
			throw new IllegalArgumentException("La palabra no puede estar vacía");
		}
		
		String palabraLimpia = palabra.trim().toUpperCase();
		this.palabra = palabraLimpia;
		char[] vectorPalabra = palabraLimpia.toCharArray();
		for(Character c: vectorPalabra){
			this.letras.add(new Letra(c + ""));
		}
	}
	
	public List<Letra> getLetras() {
		return Collections.unmodifiableList(letras);
	}

	public String getPalabra() {
		return palabra;
	}
	
	/**
	 * Calcula el puntaje total de la palabra sumando los puntos de cada letra
	 * @return El puntaje total de la palabra
	 */
	public int calcularPuntaje() {
		return letras.stream().mapToInt(Letra::getPuntos).sum();
	}
	
	/**
	 * Obtiene la longitud de la palabra
	 * @return El número de letras en la palabra
	 */
	public int getLongitud() {
		return palabra.length();
	}
	
	@Override
	public String toString() {
		return palabra;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Palabra palabra1 = (Palabra) obj;
		return Objects.equals(palabra, palabra1.palabra);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(palabra);
	}
	
	
	
	

}
