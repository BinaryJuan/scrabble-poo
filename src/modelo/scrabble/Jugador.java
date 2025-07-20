package modelo.scrabble;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Jugador implements Serializable, IJugador{
	
	private int id = new Random().nextInt(10000);
	private static final long serialVersionUID = -1267351262799502699L;
	private String nombre = "Jugador " + new Random().nextInt(600);
	private int puntaje = 0;
	private List<Letra> atril = new ArrayList<>();
	private boolean conectado = true;
	
	public Jugador(String nombre) {
		this.nombre = nombre;
	}
	
	
	public int getId() {
		return id;
	}
	
	
	public String getNombre() {
		return nombre;
	}

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public int getPuntaje() {
		return puntaje;
	}

	
	public void setPuntaje(int puntaje) {
		if (puntaje < 0) {
			throw new IllegalArgumentException("El puntaje no puede ser negativo");
		}
		if (puntaje > 10000) { // Límite razonable para Scrabble
			throw new IllegalArgumentException("Puntaje excesivamente alto: " + puntaje);
		}
		this.puntaje = puntaje;
	}

	
	public List<Letra> getAtril() {
		return atril; // Volver al comportamiento original para mantener funcionalidad
	}
	
	
	public void setAtril(List<Letra> atril) {
		if (atril == null) {
			throw new IllegalArgumentException("El atril no puede ser null");
		}
		this.atril = new ArrayList<>(atril); // Mantener copia defensiva aquí es seguro
	}
	
	/**
	 * Agrega una letra al atril del jugador
	 * @param letra La letra a agregar
	 * @throws IllegalArgumentException si el atril está lleno o la letra es null
	 */
	public void agregarLetraAlAtril(Letra letra) {
		if (letra == null) {
			throw new IllegalArgumentException("La letra no puede ser null");
		}
		if (atril.size() >= 7) {
			throw new IllegalStateException("El atril está lleno (máximo 7 fichas)");
		}
		atril.add(letra);
	}
	
	/**
	 * Remueve una letra del atril del jugador
	 * @param letra La letra a remover
	 * @return true si se removió exitosamente, false si no se encontró
	 */
	public boolean removerLetraDelAtril(Letra letra) {
		return atril.remove(letra);
	}
	
	/**
	 * Obtiene el número de fichas en el atril
	 * @return El número de fichas en el atril
	 */
	public int getCantidadFichasEnAtril() {
		return atril.size();
	}
	
	
	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}
	
	
	public boolean isConectado() {
		return conectado;
	}
	

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Jugador jugador = (Jugador) obj;
        return Objects.equals(nombre, jugador.nombre);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
    
    /**
     * Método de utilidad para debugging - muestra el atril del jugador
     */
    public void mostrarAtril() {
        System.out.println("ATRIL DE " + nombre.toUpperCase());
        System.out.print("Fichas: ");
        for(Letra letra : atril) {
            System.out.print(letra.getDescripcion() + " ");
        }
        System.out.println();
        System.out.println("Total fichas: " + atril.size());
        System.out.println("Puntaje: " + puntaje);
        System.out.println("-----------------------");
    }

}
