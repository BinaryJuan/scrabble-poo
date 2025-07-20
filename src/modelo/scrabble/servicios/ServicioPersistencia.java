package modelo.scrabble.servicios;

import java.io.*;
import java.util.ArrayList;
import modelo.scrabble.Partida;
import modelo.scrabble.Jugador;
import modelo.scrabble.ConfiguracionJuego;
import modelo.scrabble.excepciones.PersistenciaException;

/**
 * Servicio encargado de la persistencia de datos del juego
 */
public class ServicioPersistencia {
    
    /**
     * Guarda la lista de partidas en el archivo especificado
     * @param partidas Lista de partidas a guardar
     * @throws PersistenciaException Si hay error en el guardado
     */
    public void guardarPartidas(ArrayList<Partida> partidas) throws PersistenciaException {
        try (FileOutputStream fos = new FileOutputStream(ConfiguracionJuego.ARCHIVO_PARTIDAS);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(partidas);
            
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar las partidas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Carga la lista de partidas desde el archivo
     * @return Lista de partidas cargadas
     * @throws PersistenciaException Si hay error en la carga
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Partida> cargarPartidas() throws PersistenciaException {
        File archivo = new File(ConfiguracionJuego.ARCHIVO_PARTIDAS);
        
        if (!archivo.exists()) {
            return new ArrayList<>(); // Devolver lista vacía si no existe el archivo
        }
        
        try (FileInputStream fis = new FileInputStream(archivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            return (ArrayList<Partida>) ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Error al cargar las partidas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Guarda el ranking de jugadores
     * @param jugadores Lista de jugadores a guardar
     * @throws PersistenciaException Si hay error en el guardado
     */
    public void guardarRanking(ArrayList<Jugador> jugadores) throws PersistenciaException {
        try (FileOutputStream fos = new FileOutputStream(ConfiguracionJuego.ARCHIVO_RANKING);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(jugadores);
            
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar el ranking: " + e.getMessage(), e);
        }
    }
    
    /**
     * Carga el ranking de jugadores
     * @return Lista de jugadores del ranking
     * @throws PersistenciaException Si hay error en la carga
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Jugador> cargarRanking() throws PersistenciaException {
        File archivo = new File(ConfiguracionJuego.ARCHIVO_RANKING);
        
        if (!archivo.exists()) {
            return new ArrayList<>(); // Devolver lista vacía si no existe el archivo
        }
        
        try (FileInputStream fis = new FileInputStream(archivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            return (ArrayList<Jugador>) ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Error al cargar el ranking: " + e.getMessage(), e);
        }
    }
}
