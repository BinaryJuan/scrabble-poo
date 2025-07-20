package principal.scrabble;

import modelo.scrabble.*;

/**
 * Programa de prueba para el nuevo sistema de ranking persistente.
 */
public class PruebaRanking {
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DEL SISTEMA DE RANKING ===\n");
        
        try {
            // Crear jugadores de prueba
            Jugador jugador1 = new Jugador("AliceScr");
            jugador1.setPuntaje(350);
            
            Jugador jugador2 = new Jugador("BobWords");
            jugador2.setPuntaje(420);
            
            Jugador jugador3 = new Jugador("CharlieGame");
            jugador3.setPuntaje(280);
            
            Jugador jugador4 = new Jugador("DianaTop");
            jugador4.setPuntaje(500);
            
            Jugador jugador5 = new Jugador("EdwardPro");
            jugador5.setPuntaje(390);
            
            Jugador jugador6 = new Jugador("FionaMax");
            jugador6.setPuntaje(220);
            
            // Jugador con 0 puntos (no debería entrar al ranking)
            Jugador jugadorCero = new Jugador("ZeroPuntos");
            jugadorCero.setPuntaje(0);
            
            // Crear el sistema de ranking
            Ranking ranking = new Ranking();
            
            System.out.println("1. Ranking inicial:");
            System.out.println(ranking.getRankingTexto());
            System.out.println("-----------------------------------\n");
            
            // Agregar jugadores uno por uno
            System.out.println("2. Agregando jugadores al ranking:");
            
            if (ranking.evaluarJugador(jugador1)) {
                System.out.println("✓ " + jugador1.getNombre() + " entró al ranking con " + jugador1.getPuntaje() + " puntos");
            }
            
            if (ranking.evaluarJugador(jugador2)) {
                System.out.println("✓ " + jugador2.getNombre() + " entró al ranking con " + jugador2.getPuntaje() + " puntos");
            }
            
            if (ranking.evaluarJugador(jugador3)) {
                System.out.println("✓ " + jugador3.getNombre() + " entró al ranking con " + jugador3.getPuntaje() + " puntos");
            }
            
            if (ranking.evaluarJugador(jugador4)) {
                System.out.println("✓ " + jugador4.getNombre() + " entró al ranking con " + jugador4.getPuntaje() + " puntos");
            }
            
            if (ranking.evaluarJugador(jugador5)) {
                System.out.println("✓ " + jugador5.getNombre() + " entró al ranking con " + jugador5.getPuntaje() + " puntos");
            }
            
            System.out.println("\n3. Ranking después de agregar 5 jugadores:");
            System.out.println(ranking.getRankingTexto());
            System.out.println("-----------------------------------\n");
            
            // Intentar agregar un sexto jugador con puntaje bajo
            System.out.println("4. Intentando agregar jugador con puntaje bajo (" + jugador6.getPuntaje() + "):");
            if (ranking.evaluarJugador(jugador6)) {
                System.out.println("✓ " + jugador6.getNombre() + " entró al ranking");
            } else {
                System.out.println("✗ " + jugador6.getNombre() + " NO entró al ranking (puntaje insuficiente)");
            }
            
            // Intentar agregar jugador con 0 puntos
            System.out.println("\n4.1. Intentando agregar jugador con 0 puntos:");
            if (ranking.evaluarJugador(jugadorCero)) {
                System.out.println("✓ " + jugadorCero.getNombre() + " entró al ranking");
            } else {
                System.out.println("✗ " + jugadorCero.getNombre() + " NO entró al ranking (0 puntos no permitidos)");
            }
            
            System.out.println("\n5. Ranking final:");
            System.out.println(ranking.getRankingTexto());
            System.out.println("-----------------------------------\n");
            
            // Crear un jugador súper poderoso
            Jugador superJugador = new Jugador("SuperGamer");
            superJugador.setPuntaje(600);
            
            System.out.println("6. Agregando súper jugador con " + superJugador.getPuntaje() + " puntos:");
            if (ranking.evaluarJugador(superJugador)) {
                System.out.println("✓ " + superJugador.getNombre() + " entró al ranking y desplazó a otros");
            }
            
            System.out.println("\n7. Ranking final con súper jugador:");
            System.out.println(ranking.getRankingTexto());
            
            // Probar el guardado y carga
            System.out.println("-----------------------------------\n");
            System.out.println("8. Probando guardado y carga...");
            ranking.guardar();
            System.out.println("✓ Ranking guardado en archivo");
            
            Ranking rankingCargado = Ranking.cargar();
            System.out.println("✓ Ranking cargado desde archivo");
            
            System.out.println("\n9. Ranking cargado desde archivo:");
            System.out.println(rankingCargado.getRankingTexto());
            
            System.out.println("=== PRUEBA COMPLETADA EXITOSAMENTE ===");
            
        } catch (Exception e) {
            System.err.println("Error en la prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
