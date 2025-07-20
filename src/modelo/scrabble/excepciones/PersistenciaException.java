package modelo.scrabble.excepciones;

/**
 * Excepción lanzada cuando hay problemas con la persistencia de datos
 */
public class PersistenciaException extends ScrabbleException {
    
    private static final long serialVersionUID = 1L;
    
    public PersistenciaException(String mensaje) {
        super(mensaje);
    }
    
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
