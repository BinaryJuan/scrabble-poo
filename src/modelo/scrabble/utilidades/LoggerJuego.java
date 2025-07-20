package modelo.scrabble.utilidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger simple para debugging y seguimiento del juego
 */
public class LoggerJuego {
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static boolean debugHabilitado = false;
    
    /**
     * Habilita o deshabilita el logging de debug
     */
    public static void setDebugHabilitado(boolean habilitado) {
        debugHabilitado = habilitado;
    }
    
    /**
     * Log de información general
     */
    public static void info(String mensaje) {
        log("INFO", mensaje);
    }
    
    /**
     * Log de errores
     */
    public static void error(String mensaje) {
        log("ERROR", mensaje);
    }
    
    /**
     * Log de errores con excepción
     */
    public static void error(String mensaje, Throwable e) {
        log("ERROR", mensaje + " - " + e.getMessage());
        if (debugHabilitado) {
            e.printStackTrace();
        }
    }
    
    /**
     * Log de debug (solo se muestra si está habilitado)
     */
    public static void debug(String mensaje) {
        if (debugHabilitado) {
            log("DEBUG", mensaje);
        }
    }
    
    /**
     * Log de advertencias
     */
    public static void warning(String mensaje) {
        log("WARN", mensaje);
    }
    
    /**
     * Log de acciones del juego
     */
    public static void accionJuego(String jugador, String accion) {
        info("JUEGO - " + jugador + ": " + accion);
    }
    
    /**
     * Log de validaciones fallidas
     */
    public static void validacionFallida(String tipo, String razon) {
        warning("VALIDACION - " + tipo + ": " + razon);
    }
    
    private static void log(String nivel, String mensaje) {
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        System.out.println("[" + timestamp + "] [" + nivel + "] " + mensaje);
    }
    
    private LoggerJuego() {
        // Clase utilitaria - no debe ser instanciada
    }
}
