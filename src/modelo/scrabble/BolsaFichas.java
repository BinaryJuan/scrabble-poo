package modelo.scrabble;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa la bolsa de fichas del juego Scrabble.
 * Maneja la distribución y control de fichas disponibles.
 */
public class BolsaFichas implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final Map<String, Integer> bolsaDeFichas = new HashMap<>();
	private int cantidadFichas = ConfiguracionJuego.TOTAL_FICHAS_BOLSA;
	
	public BolsaFichas() {
		inicializarBolsa();
	}
	
	/**
	 * Inicializa la bolsa con la distribución estándar de fichas del Scrabble en español
	 */
	private void inicializarBolsa() {
		// Distribución oficial del Scrabble en español
		bolsaDeFichas.put("A", 12); bolsaDeFichas.put("B", 2); bolsaDeFichas.put("C", 4); 
		bolsaDeFichas.put("D", 5); bolsaDeFichas.put("E", 12); bolsaDeFichas.put("F", 1); 
		bolsaDeFichas.put("G", 2); bolsaDeFichas.put("H", 2); bolsaDeFichas.put("I", 6); 
		bolsaDeFichas.put("J", 1); bolsaDeFichas.put("L", 4); bolsaDeFichas.put("M", 2); 
		bolsaDeFichas.put("N", 5); bolsaDeFichas.put("Ñ", 1); bolsaDeFichas.put("O", 9); 
		bolsaDeFichas.put("P", 2); bolsaDeFichas.put("Q", 1); bolsaDeFichas.put("R", 5);
		bolsaDeFichas.put("S", 6); bolsaDeFichas.put("T", 4); bolsaDeFichas.put("U", 5);
		bolsaDeFichas.put("V", 1); bolsaDeFichas.put("X", 1); bolsaDeFichas.put("Y", 1); 
		bolsaDeFichas.put("Z", 1);
	}
	
	public int getCantidadFichas() {
		return cantidadFichas;
	}
	
	public void setCantidadFichas(int cantidadFichas) {
		if (cantidadFichas < 0) {
			throw new IllegalArgumentException("La cantidad de fichas no puede ser negativa");
		}
		this.cantidadFichas = cantidadFichas;
	}

	/**
	 * Obtiene la cantidad de fichas disponibles para una letra específica
	 * @param letra La letra a consultar
	 * @return La cantidad disponible, o LETRA_NO_ENCONTRADA si la letra no existe
	 */
	public int get(String letra) {
		if (letra == null || letra.trim().isEmpty()) {
			return ConfiguracionJuego.LETRA_NO_ENCONTRADA;
		}
		
		Integer cantidad = bolsaDeFichas.get(letra.toUpperCase());
		return cantidad != null ? cantidad : ConfiguracionJuego.LETRA_NO_ENCONTRADA;
	}

	/**
	 * Establece la cantidad de fichas para una letra específica
	 * @param letra La letra a modificar
	 * @param cantidad La nueva cantidad (no puede ser negativa)
	 */
	public void put(String letra, int cantidad) {
		if (letra == null || letra.trim().isEmpty()) {
			throw new IllegalArgumentException("La letra no puede ser null o vacía");
		}
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no puede ser negativa");
		}
		
		bolsaDeFichas.put(letra.toUpperCase(), cantidad);
	}
	
	/**
	 * Verifica si hay fichas disponibles de una letra específica
	 * @param letra La letra a verificar
	 * @return true si hay fichas disponibles, false en caso contrario
	 */
	public boolean tieneFichasDisponibles(String letra) {
		return get(letra) > 0;
	}
	
	/**
	 * Verifica si la bolsa está vacía
	 * @return true si no hay fichas, false en caso contrario
	 */
	public boolean estaVacia() {
		return cantidadFichas <= 0;
	}
	
	/**
	 * Obtiene una copia de solo lectura de la distribución actual de fichas
	 * @return Map inmutable con la distribución actual
	 */
	public Map<String, Integer> getDistribucionFichas() {
		return new HashMap<>(bolsaDeFichas);
	}
	
	/**
	 * Reduce la cantidad de una letra específica en la bolsa
	 * @param letra La letra a reducir
	 * @param cantidad La cantidad a reducir
	 * @return true si se pudo reducir, false si no hay suficientes fichas
	 */
	public boolean reducirFicha(String letra, int cantidad) {
		if (letra == null || cantidad <= 0) {
			return false;
		}
		
		String letraUpperCase = letra.toUpperCase();
		Integer fichasDisponibles = bolsaDeFichas.get(letraUpperCase);
		
		if (fichasDisponibles != null && fichasDisponibles >= cantidad) {
			bolsaDeFichas.put(letraUpperCase, fichasDisponibles - cantidad);
			cantidadFichas -= cantidad;
			return true;
		}
		
		return false;
	}

}
