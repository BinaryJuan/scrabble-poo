package vista.scrabble;

import java.util.ArrayList;

import modelo.scrabble.Casillero;
import modelo.scrabble.IJugador;

public interface Vista {
	
	 void iniciar();
	 void mostrarComenzarPartida(ArrayList<IJugador> jugadores);
	 void mostrarTablero(Casillero[][] tablero);
	 void mostrarEstadoJugador(IJugador jugador);
	 void mostrarTurnoActual(IJugador jugadorTurno);
	 void mostrarAtrilCliente(IJugador miJugador);
	 void mostrarRanking();
	 void mostrarFinalPartida(IJugador jugador);
	 void mostrarMensaje(String mensaje);
	 void bloquearJuego(); // Bloquea toda la funcionalidad cuando termina la partida
	 boolean esTurnoActual();
	 boolean estaConectado();
	
}
