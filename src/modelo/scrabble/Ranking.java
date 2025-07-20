package modelo.scrabble;

import java.io.*;
import java.util.*;

/**
 * Sistema de ranking persistente que mantiene los mejores 5 jugadores.
 * Se actualiza automáticamente al finalizar cada partida.
 */
public class Ranking implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final String ARCHIVO_RANKING = "RankingJugadores.dat";
    private static final int MAX_JUGADORES = 5;
    
    private ArrayList<EntradaRanking> top5;
    
    public Ranking() {
        this.top5 = new ArrayList<>();
    }
    
    /**
     * Clase interna para representar una entrada en el ranking
     */
    public static class EntradaRanking implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String nombre;
        private int puntaje;
        private String fechaPartida;
        
        public EntradaRanking(String nombre, int puntaje, String fechaPartida) {
            this.nombre = nombre;
            this.puntaje = puntaje;
            this.fechaPartida = fechaPartida;
        }
        
        // Getters
        public String getNombre() { return nombre; }
        public int getPuntaje() { return puntaje; }
        public String getFechaPartida() { return fechaPartida; }
        
        @Override
        public String toString() {
            return nombre + " - " + puntaje + " puntos (" + fechaPartida + ")";
        }
    }
    
    /**
     * Intenta agregar un jugador al ranking.
     * Solo se agrega si está entre los mejores 5 y tiene puntaje mayor a 0.
     * 
     * @param jugador El jugador a evaluar
     * @return true si el jugador entró al ranking
     */
    public boolean evaluarJugador(Jugador jugador) {
        if (jugador == null) return false;
        
        // No agregar jugadores con 0 puntos al ranking
        if (jugador.getPuntaje() <= 0) {
            return false;
        }
        
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date());
        EntradaRanking nuevaEntrada = new EntradaRanking(
            jugador.getNombre(), 
            jugador.getPuntaje(), 
            fecha
        );
        
        // Si hay menos de 5 jugadores, agregar directamente
        if (top5.size() < MAX_JUGADORES) {
            top5.add(nuevaEntrada);
            ordenarRanking();
            return true;
        }
        
        // Si el jugador tiene mejor puntaje que el último del top 5
        EntradaRanking ultimoRanking = top5.get(MAX_JUGADORES - 1);
        if (jugador.getPuntaje() > ultimoRanking.getPuntaje()) {
            top5.remove(MAX_JUGADORES - 1); // Remover el último
            top5.add(nuevaEntrada);         // Agregar el nuevo
            ordenarRanking();
            return true;
        }
        
        return false; // No entró al ranking
    }
    
    /**
     * Ordena el ranking por puntaje descendente
     */
    private void ordenarRanking() {
        top5.sort((e1, e2) -> Integer.compare(e2.getPuntaje(), e1.getPuntaje()));
    }
    
    /**
     * Obtiene el top 5 actual
     */
    public ArrayList<EntradaRanking> getTop5() {
        return new ArrayList<>(top5); // Copia defensiva
    }
    
    /**
     * Convierte el ranking a lista de IJugador para compatibilidad
     */
    public ArrayList<IJugador> getTop5ComoIJugador() {
        ArrayList<IJugador> resultado = new ArrayList<>();
        for (EntradaRanking entrada : top5) {
            // Crear un jugador temporal solo para mostrar
            Jugador jugadorTemporal = new Jugador(entrada.getNombre());
            jugadorTemporal.setPuntaje(entrada.getPuntaje());
            resultado.add(jugadorTemporal);
        }
        return resultado;
    }
    
    /**
     * Guarda el ranking en archivo
     */
    public void guardar() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_RANKING))) {
            oos.writeObject(this);
            System.out.println("Ranking guardado exitosamente");
        }
    }
    
    /**
     * Carga el ranking desde archivo
     */
    public static Ranking cargar() throws IOException, ClassNotFoundException {
        File archivo = new File(ARCHIVO_RANKING);
        if (!archivo.exists()) {
            System.out.println("Archivo de ranking no existe, creando uno nuevo");
            return new Ranking();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_RANKING))) {
            Ranking ranking = (Ranking) ois.readObject();
            System.out.println("Ranking cargado exitosamente: " + ranking.top5.size() + " entradas");
            return ranking;
        }
    }
    
    /**
     * Actualiza el ranking con todos los jugadores de una partida terminada
     */
    public void actualizarConPartida(ArrayList<Jugador> jugadores) {
        boolean huboActualizacion = false;
        
        for (Jugador jugador : jugadores) {
            if (jugador.getPuntaje() <= 0) {
                System.out.println(jugador.getNombre() + " no entra al ranking (0 puntos)");
                continue;
            }
            
            if (evaluarJugador(jugador)) {
                huboActualizacion = true;
                System.out.println("¡" + jugador.getNombre() + " entró al ranking con " + jugador.getPuntaje() + " puntos!");
            }
        }
        
        if (huboActualizacion) {
            try {
                guardar();
            } catch (IOException e) {
                System.err.println("Error al guardar ranking: " + e.getMessage());
            }
        }
    }
    
    /**
     * Muestra el ranking en formato texto
     */
    public String getRankingTexto() {
        if (top5.isEmpty()) {
            return "No hay jugadores en el ranking aún.\n";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("TOP 5 MEJORES JUGADORES\n\n");
        
        for (int i = 0; i < top5.size(); i++) {
            EntradaRanking entrada = top5.get(i);
            String emoji = obtenerEmojiPosicion(i + 1);
            sb.append(String.format("%s %d. %s\n", 
                emoji, i + 1, entrada.toString()));
        }
        
        return sb.toString();
    }
    
    /**
     * Obtiene el emoji correspondiente a la posición
     */
    private String obtenerEmojiPosicion(int posicion) {
        switch (posicion) {
            case 1: return "🥇";
            case 2: return "🥈"; 
            case 3: return "🥉";
            case 4: return "🏅";
            case 5: return "🏅";
            default: return "•";
        }
    }
}
