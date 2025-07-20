package vista.scrabble.consolagrafica;

import java.io.IOException;
import java.util.ArrayList;
import controlador.scrabble.Controlador;
import modelo.scrabble.Partida;

public class FlujoElegirPartida extends Flujo{
	
	public FlujoElegirPartida(ConsolaGrafica vista, Controlador controlador) {
		super(vista, controlador);
	}

	public void mostarMenuTextual() {
		vista.mostrarPartidasGuardadas(new Object());
		vista.mostrarMensaje("Seleccione una partida: ");
	}

	public Flujo elegirOpcion(String opcion) {
		int idPartida = -1;
		try {
			idPartida = Integer.parseInt(opcion);
			controlador.cargarPartida(idPartida);
			return new FlujoOpcionesJuego(vista,controlador);
		} catch (IOException e) {
			return this;
		}
		catch (NumberFormatException e) {
			return this;
		}
	}

}
