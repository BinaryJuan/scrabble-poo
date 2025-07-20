package modelo.scrabble;

import java.io.Serializable;

public class Letra implements Casillero, Serializable{

	private static final long serialVersionUID = 1L;
	private String letra = "  ";
	private int puntos;
	private final int id; // ID único para cada instancia
	private static int contadorId = 0; // Contador global para generar IDs únicos
	
	//CONSTRUCTOR
	public Letra(String letra) {
		// Generar ID único para esta instancia
		synchronized(Letra.class) {
			this.id = ++contadorId;
		}
		
		// Validación básica y asignación
		if (letra == null) {
			this.letra = "  ";
		} else {
			this.letra = letra.trim().toUpperCase();
		}
		
		// Asignar puntaje según la letra
		int puntajeLetra = PuntajeFichas.getPuntaje(this.letra);
		if(puntajeLetra != 0) {
			this.puntos = puntajeLetra;
		} else {
			// Si no está en la tabla de puntajes, asignar 1 punto por defecto
			this.puntos = 1;
		}
	}
	
	@Override
	public String getDescripcion() {
		return letra;
	}
	
	@Override
	public int getPuntos() {
		return puntos;
	}
	
	@Override
	public String toString() {
		return getDescripcion();
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Letra ltr = (Letra) obj;
        return this.id == ltr.id; // Comparar por ID único, no por contenido
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}
	
	/**
	 * Getter para el ID único de la letra
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Implementación requerida por la interfaz Casillero.
	 * Mantiene el comportamiento original para preservar la funcionalidad.
	 */
	@Override
	public void setDescripcion(String descripcion) {
		this.letra = descripcion;
	}

}
