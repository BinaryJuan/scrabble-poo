package modelo.scrabble.excepciones;

/**
 * Excepción lanzada cuando se intenta realizar una jugada inválida
 */
public class JugadaInvalidaException extends ScrabbleException {
    
    private static final long serialVersionUID = 1L;
    
    public JugadaInvalidaException(String mensaje) {
        super(mensaje);
    }
    
    public JugadaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
