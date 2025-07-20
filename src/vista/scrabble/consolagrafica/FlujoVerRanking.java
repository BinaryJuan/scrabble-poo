package vista.scrabble.consolagrafica;

import controlador.scrabble.Controlador;

public class FlujoVerRanking extends Flujo{

	public FlujoVerRanking(ConsolaGrafica vista, Controlador controlador) {
		super(vista, controlador);
	}

	public void mostarMenuTextual() {
		vista.mostrarMensaje("");
		vista.mostrarRanking();
		vista.mostrarMensaje("");
		vista.mostrarMensaje("Presiona Intro para volver al menu principal.");
	}


	public Flujo elegirOpcion(String opcion) {
		return new FlujoMenuPrincipal(vista,controlador);
	}

}
