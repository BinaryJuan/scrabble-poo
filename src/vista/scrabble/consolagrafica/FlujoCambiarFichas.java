package vista.scrabble.consolagrafica;

import controlador.scrabble.Controlador;


public class FlujoCambiarFichas extends Flujo{

	public FlujoCambiarFichas(ConsolaGrafica vista, Controlador controlador) {
		super(vista, controlador);
	}
	
    public void mostarMenuTextual() {
        vista.mostrarMensaje("Ingrese una palabra que contenga las letras que desea cambiar (en cualquier orden): ");
    }

    public Flujo elegirOpcion(String opcion) {
    	if(!controlador.cambiarFichas(opcion)) {
			return new FlujoCambiarFichas(vista,controlador);
		}
		return new FlujoOpcionesJuego(vista,controlador);
	}
    
}
	

