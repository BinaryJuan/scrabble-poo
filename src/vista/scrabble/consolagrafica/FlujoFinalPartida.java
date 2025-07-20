package vista.scrabble.consolagrafica;

import controlador.scrabble.Controlador;
import modelo.scrabble.IJugador;

public class FlujoFinalPartida extends Flujo{

	private IJugador jugadorGanador;
	
	public FlujoFinalPartida(ConsolaGrafica vista, Controlador controlador, IJugador jugadorGanador) {
		super(vista, controlador);
		this.jugadorGanador = jugadorGanador;
	}

	public void mostarMenuTextual() {
		vista.mostrarMensaje("El juego ha terminado. ¡Felicidades " + jugadorGanador.getNombre() + ", sos el ganador!");
		vista.mostrarMensaje("");
		vista.mostrarMensaje("Presiona Intro para continuar.");
	}

	public Flujo elegirOpcion(String opcion) {
		return new FlujoMenuPrincipal(vista,controlador);
	}

}
