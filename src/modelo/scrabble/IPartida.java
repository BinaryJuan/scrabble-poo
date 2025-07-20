package modelo.scrabble;

import java.io.Serializable;
import java.util.ArrayList;

public interface IPartida extends Serializable{

	int getId();

	String getFechaHora();

	void setFechaHora(String fechaHora);

	Tablero getTablero();

	void setTablero(Tablero tablero);

	BolsaFichas getBolsaDeFichas();

	void setBolsaDeFichas(BolsaFichas bolsaDeFichas);

	ArrayList<Jugador> getJugadores();

	int getTurnoActual();

	void setTurnoActual(int turnoActual);

	String toString();

}