package modelo.scrabble;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public interface IModeloRemoto extends IObservableRemoto, Serializable{
	
	void addJugador(Jugador jugador) throws RemoteException;
	
	Jugador desconectarJugador(Jugador jugador) throws RemoteException;

	void comenzarPartida() throws RemoteException;

	boolean cambiarFichas(char[] fichasACambiar) throws RemoteException;

	boolean agregarPalabra(String x, String y, Palabra palabraActual, String disposicion) throws RemoteException, IOException;

	void siguienteTurno() throws RemoteException;

	int obtenerGanador() throws RemoteException;

	boolean isPrimerMovimiento() throws RemoteException;
	
	void pasarTurno() throws RemoteException;

	Casillero[][] getTablero() throws RemoteException;

	BolsaFichas getBolsaDeFichas() throws RemoteException;

	ArrayList<Jugador> getJugadores() throws RemoteException;

	boolean isVacia() throws RemoteException;

	int getCantidadFichas() throws RemoteException;

	int getTurnoActual() throws RemoteException;
	
	void notificarObservadores(Object obj) throws RemoteException;

	ArrayList<Jugador> getTop5Jugadores() throws RemoteException, ClassNotFoundException, IOException;
	
	ArrayList<IJugador> getTop5JugadoresInterfaz() throws RemoteException, ClassNotFoundException, IOException;
	
	String getRankingTexto() throws RemoteException;

	boolean validarPalabra(int x, int y, Palabra palabraActual, boolean horizontal) throws RemoteException;
	
	int getCantidadJugadores() throws RemoteException;
	
	Jugador obtenerJugadorGanador() throws RemoteException;
	
	String obtenerMensajeFinPartida() throws RemoteException;
	


}