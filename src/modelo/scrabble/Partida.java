package modelo.scrabble;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Partida implements Serializable, IPartida{
	
	private static final long serialVersionUID = -4243684796308041028L;
	private static int ContadorID = 0;
	private int id;
	private String fechaHora = "";
	private Tablero tablero;
	private BolsaFichas bolsaDeFichas;
	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private int turnoActual = 0;
	
	public Partida(Tablero tablero, BolsaFichas bolsaDeFichas,
			ArrayList<Jugador> jugadores, int turnoActual) {
		LocalDateTime fechaHoy = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");
		this.id = ++ContadorID;
		this.fechaHora = fechaHoy.format(formato);
		this.tablero = tablero;
		this.bolsaDeFichas = bolsaDeFichas;
		this.jugadores = jugadores;
		this.turnoActual = turnoActual;
	}
	public int getId() {
		return id;
	}
	
	public String getFechaHora() {
		return fechaHora;
	}
	
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
				
	public Tablero getTablero() {
		return tablero;
	}
	public void setTablero(Tablero tablero) {
		this.tablero = tablero;
	}
	public BolsaFichas getBolsaDeFichas() {
		return bolsaDeFichas;
	}
	public void setBolsaDeFichas(BolsaFichas bolsaDeFichas) {
		this.bolsaDeFichas = bolsaDeFichas;
	}
	
	public int getTurnoActual() {
		return turnoActual;
	}
	public void setTurnoActual(int turnoActual) {
		this.turnoActual = turnoActual;
	}
	
	public String toString() {

		String jug = "";
		for(Jugador j: jugadores) {
			jug += j.getNombre() + ". ";
		}
		return id
				+ ". FECHA: " + fechaHora + "\n "
				+ "JUGADORES: " + jug + "\n "
				+ "TURNO ACTUAL: " + jugadores.get(turnoActual).getNombre() + "\n";
	}

	public ArrayList<Jugador> getJugadores() {
		return jugadores;
	}
	
	

}
