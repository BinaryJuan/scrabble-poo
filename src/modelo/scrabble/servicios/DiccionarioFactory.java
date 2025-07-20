package modelo.scrabble.servicios;

import modelo.scrabble.Diccionario;

/**
 * Factory para crear instancias del diccionario de manera eficiente
 */
public class DiccionarioFactory {
    
    private static Diccionario instanciaUnica;
    
    /**
     * Obtiene una instancia única del diccionario (Singleton)
     * @return La instancia del diccionario
     */
    public static synchronized Diccionario obtenerInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new Diccionario();
        }
        return instanciaUnica;
    }
    
    /**
     * Reinicia la instancia del diccionario (útil para testing)
     */
    public static synchronized void reiniciarInstancia() {
        instanciaUnica = null;
    }
}
