package modelo.scrabble.utilidades;

import modelo.scrabble.*;
import java.util.List;

/**
 * Validador del estado del juego para verificar consistencia
 */
public class ValidadorEstadoJuego {
    
    /**
     * Valida que el estado del juego sea consistente
     */
    public static boolean validarEstadoJuego(List<Jugador> jugadores, BolsaFichas bolsa, 
                                           int turnoActual) {
        try {
            // Validar jugadores
            if (!validarJugadores(jugadores)) {
                return false;
            }
            
            // Validar turno actual
            if (!validarTurno(turnoActual, jugadores.size())) {
                return false;
            }
            
            // Validar bolsa de fichas
            if (!validarBolsaFichas(bolsa)) {
                return false;
            }
            
            // Validar distribución de fichas
            if (!validarDistribucionFichas(jugadores, bolsa)) {
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error validando estado del juego: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean validarJugadores(List<Jugador> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) {
            return false;
        }
        
        // Validar cada jugador
        for (Jugador jugador : jugadores) {
            if (jugador == null) {
                return false;
            }
            
            if (jugador.getNombre() == null || jugador.getNombre().trim().isEmpty()) {
                return false;
            }
            
            if (jugador.getPuntaje() < 0) {
                return false;
            }
            
            if (jugador.getAtril() == null) {
                return false;
            }
            
            // Validar que no tenga más de 7 fichas
            if (jugador.getAtril().size() > ConfiguracionJuego.FICHAS_INICIALES_POR_JUGADOR) {
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean validarTurno(int turnoActual, int cantidadJugadores) {
        if (cantidadJugadores <= 0) {
            return false;
        }
        
        return turnoActual >= 0 && turnoActual < cantidadJugadores;
    }
    
    private static boolean validarBolsaFichas(BolsaFichas bolsa) {
        if (bolsa == null) {
            return false;
        }
        
        // Validar que las fichas no sean negativas
        String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
                          "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", 
                          "U", "V", "X", "Y", "Z"};
        
        for (String letra : letras) {
            int cantidad = bolsa.get(letra);
            if (cantidad < 0) {
                return false;
            }
        }
        
        return bolsa.getCantidadFichas() >= 0;
    }
    
    private static boolean validarDistribucionFichas(List<Jugador> jugadores, BolsaFichas bolsa) {
        // Contar fichas totales en atriles
        int fichasEnAtriles = 0;
        for (Jugador jugador : jugadores) {
            fichasEnAtriles += jugador.getAtril().size();
        }
        
        // Verificar que la suma sea lógica (no más del total de fichas del juego)
        int totalFichas = fichasEnAtriles + bolsa.getCantidadFichas();
        return totalFichas <= ConfiguracionJuego.TOTAL_FICHAS_BOLSA;
    }
    
    /**
     * Valida que un jugador tenga las fichas que dice tener
     */
    public static boolean validarAtrilJugador(Jugador jugador, char[] fichasRequeridas) {
        if (jugador == null || fichasRequeridas == null) {
            return false;
        }
        
        List<Letra> atril = jugador.getAtril();
        if (atril == null) {
            return false;
        }
        
        // Verificar que el jugador tenga todas las fichas requeridas
        for (char ficha : fichasRequeridas) {
            boolean encontrada = false;
            for (Letra letra : atril) {
                if (letra.getDescripcion().equals(String.valueOf(ficha))) {
                    encontrada = true;
                    break;
                }
            }
            if (!encontrada) {
                return false;
            }
        }
        
        return true;
    }
    
    private ValidadorEstadoJuego() {
        // Clase utilitaria - no debe ser instanciada
    }
}
