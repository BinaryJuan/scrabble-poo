package modelo.scrabble;

import java.io.Serializable;

/**
 * Representa un casillero vacío en el tablero de Scrabble
 */
public class CasilleroVacio implements Casillero, Serializable {
    
    private static final long serialVersionUID = 1L;
    private String descripcion;
    
    /**
     * Constructor para casillero vacío
     * @param descripcion La descripción del casillero vacío (ej: "__")
     */
    public CasilleroVacio(String descripcion) {
        this.descripcion = descripcion != null ? descripcion : "__";
    }
    
    /**
     * Constructor por defecto
     */
    public CasilleroVacio() {
        this("__");
    }
    
    @Override
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public int getPuntos() {
        return 0;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CasilleroVacio that = (CasilleroVacio) obj;
        return descripcion.equals(that.descripcion);
    }
    
    @Override
    public int hashCode() {
        return descripcion.hashCode();
    }
}
