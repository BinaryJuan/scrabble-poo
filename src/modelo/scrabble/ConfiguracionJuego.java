package modelo.scrabble;

/**
 * Clase que centraliza las configuraciones del juego Scrabble
 */
public class ConfiguracionJuego {
    
    // Configuración del tablero
    public static final int TAMAÑO_TABLERO = 16;  // Array interno 16x16
    public static final int TAMAÑO_TABLERO_VISIBLE = 15;  // Tablero visible 15x15 (1-15)
    public static final int FICHAS_INICIALES_POR_JUGADOR = 7;
    public static final int MIN_VOCALES_INICIALES = 4;
    
    // Configuración de coordenadas (numéricas 1-15)
    public static final int MIN_COORDENADA = 1; // Mínima coordenada (1)
    public static final int MAX_COORDENADA = 15; // Máxima coordenada (15) para tablero visible 15x15
    public static final int COORDENADA_CENTRO = 8; // Centro del tablero (posición 8)
    
    // Configuración de red
    public static final int PUERTO_DEFAULT = 8888;
    public static final int MIN_PUERTO = 1024;
    public static final int MAX_PUERTO = 65535;
    
    // Archivos de persistencia
    public static final String ARCHIVO_PARTIDAS = "PartidasGuardadas.bin";
    public static final String ARCHIVO_RANKING = "Ranking.bin";
    
    // Configuración de fichas en bolsa
    public static final int TOTAL_FICHAS_BOLSA = 100;
    
    // Mensajes de error
    public static final String ERROR_COORDENADAS_INVALIDAS = "Las coordenadas deben estar entre 1 y 15";
    public static final String ERROR_PUERTO_INVALIDO = "Puerto inválido. Debe estar entre " + MIN_PUERTO + " y " + MAX_PUERTO;
    public static final String ERROR_IP_INVALIDA = "Debe seleccionar una IP válida";
    
    // Disposiciones de palabra
    public static final String DISPOSICION_HORIZONTAL = "1";
    public static final String DISPOSICION_VERTICAL = "2";
    
    // Valores de retorno especiales
    public static final int LETRA_NO_ENCONTRADA = -1;
    
    private ConfiguracionJuego() {
        // Clase utilitaria - no debe ser instanciada
    }
}
