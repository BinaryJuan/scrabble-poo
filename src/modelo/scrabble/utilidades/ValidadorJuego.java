package modelo.scrabble.utilidades;

import modelo.scrabble.ConfiguracionJuego;

/**
 * Clase utilitaria para validaciones comunes del juego
 */
public class ValidadorJuego {
    
    /**
     * Valida que una coordenada esté dentro del rango válido del tablero
     * @param coordenada La coordenada a validar (valor numérico 1-15)
     * @return true si es válida, false en caso contrario
     */
    public static boolean esCoordenadaValida(int coordenada) {
        return coordenada >= ConfiguracionJuego.MIN_COORDENADA && 
               coordenada <= ConfiguracionJuego.MAX_COORDENADA;
    }
    
    /**
     * Valida que una cadena no sea null o vacía
     * @param cadena La cadena a validar
     * @return true si es válida, false en caso contrario
     */
    public static boolean esCadenaValida(String cadena) {
        return cadena != null && !cadena.trim().isEmpty();
    }
    
    /**
     * Valida que un nombre de jugador cumpla con el formato esperado
     * @param nombre El nombre a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean esNombreJugadorValido(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        // Mínimo 2 caracteres, máximo 20
        if (nombre.length() < 2 || nombre.length() > 20) {
            return false;
        }
        
        // Permitir letras, números, espacios, guiones y guiones bajos
        return nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s_-]+$");
    }
    
    /**
     * Valida que un puerto esté en el rango válido
     * @param puerto El puerto a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean esPuertoValido(int puerto) {
        return puerto >= ConfiguracionJuego.MIN_PUERTO && puerto <= ConfiguracionJuego.MAX_PUERTO;
    }
    
    /**
     * Valida que una disposición sea válida
     * @param disposicion La disposición a validar
     * @return true si es válida, false en caso contrario
     */
    public static boolean esDisposicionValida(String disposicion) {
        return ConfiguracionJuego.DISPOSICION_HORIZONTAL.equals(disposicion) ||
               ConfiguracionJuego.DISPOSICION_VERTICAL.equals(disposicion);
    }
    
    /**
     * Convierte una coordenada en string a su valor ASCII
     * @param coordenadaStr La coordenada como string
     * @return El valor ASCII de la coordenada, o -1 si es inválida
     */
    public static int coordenadaStringAAscii(String coordenadaStr) {
        if (!esCadenaValida(coordenadaStr)) {
            return -1;
        }
        
        String coordenadaLimpia = coordenadaStr.trim().toUpperCase();
        if (coordenadaLimpia.length() != 1) {
            return -1;
        }
        
        return (int) coordenadaLimpia.charAt(0);
    }
    
    private ValidadorJuego() {
        // Clase utilitaria - no debe ser instanciada
    }
}
