package modelo.scrabble.excepciones;

/**
 * Excepción base para errores relacionados con el juego Scrabble
 */
public class ScrabbleException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public ScrabbleException(String mensaje) {
        super(mensaje);
    }
    
    public ScrabbleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
